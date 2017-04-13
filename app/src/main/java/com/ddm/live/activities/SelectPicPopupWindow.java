package com.ddm.live.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.live.PlayItem;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.views.iface.IUserVideoListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPicPopupWindow extends Activity implements IUserVideoListView {

    @BindView(R2.id.user_not_self_pop_layout)
    LinearLayout llNotSelfPop;

    @BindView(R2.id.user_self_pop_layout)
    LinearLayout llSelfPop;

    @OnClick(R2.id.user_not_self_pop_report_btn)
    public void reportMaster() {
//        Toast.makeText(SelectPicPopupWindow.this, "举报成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(SelectPicPopupWindow.this, UserInfoHomeActivity.class);
        intent.putExtra("actionType", "report");
        startActivity(intent);
        finish();
    }

    @OnClick(R2.id.user_not_self_pop_cancel_btn)
    public void notSelfCancel() {
        finish();
    }

    @OnClick(R2.id.user_self_pop_delete_btn)
    public void deletedVideo() {
        liveListPresenter.deleteStream2(streamID);
    }

    @OnClick(R2.id.user_self_pop_cancel_btn)
    public void SelfCancel() {
        finish();
    }

    private boolean isSelf;
    private String streamID;
    private LiveListPresenter liveListPresenter;

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_user_video_pop_menus);
        ButterKnife.bind(this);

        isSelf = getIntent().getBooleanExtra("isSelf", false);
        streamID = getIntent().getStringExtra("streamID");
//        setupActivityComponent(appComponent);

        init();

        llNotSelfPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llSelfPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init() {
        if (isSelf) {
            llNotSelfPop.setVisibility(View.GONE);
            llSelfPop.setVisibility(View.VISIBLE);
        } else {
            llNotSelfPop.setVisibility(View.VISIBLE);
            llSelfPop.setVisibility(View.GONE);
        }
    }

    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        liveListPresenter = presenterComponent.getLiveListPresenter();
        liveListPresenter.attachIView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.user_home_push_bottom_in, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.user_home_push_bottom_out, 0);
    }

    @Override
    public void userListIntentPlayerActivity(boolean result, PlayItem playItem) {

    }

    @Override
    public void userListDeleteStreamResult(String rs) {
        Intent intent = new Intent();
        intent.setClass(SelectPicPopupWindow.this, UserInfoHomeActivity.class);
        intent.putExtra("result", rs);
        startActivity(intent);
        finish();
    }

    @Override
    public void onfailed(String message) {

    }
}
