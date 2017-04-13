package com.ddm.live.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.UserFansListAdapter;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.fansbeans.FansBean;
import com.ddm.live.models.bean.common.fansbeans.MasterBean;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.ui.widget.LoadingView;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IUserFansView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FansListActivity extends BaseActivity implements IUserFansView {
    private UserCenterPresenter userCenterPresenter;
    private Integer uID;
    private AppComponent appComponent;
    private UserFansListAdapter userFansListAdapter;
    private List<FansBean> fansInfoList;
    @BindView(R2.id.user_fans_list)
    ListView lvFansList;
    //    @BindView(R2.id.indicator)
//    AVLoadingIndicatorView indicatorView;
    @BindView(R2.id.loadView)
    LoadingView loadingView;
    @BindView(R2.id.user_fans_list_no_data_layout)
    LinearLayout llEmptyView;

    @BindView(R2.id.list_no_data_text)
    TextView txEmptyText;

    @OnClick(R2.id.user_fans_list_title_back_img)
    public void goBack() {
//        if (fansInfoList != null) {
//            for (FansBean info : fansInfoList) {
//                Uri uri = Uri.parse(info.getFans().getData().getAvatarSmall());
//                Picasso.with(this).invalidate(uri);
//            }
//        }
        finish();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UILApplication.getInstance().addActivity(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.xiaozhu_top_status_bar);
        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.xiaozhu_bottom_status_bar);
        setContentView(R.layout.activity_fans_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        userCenterPresenter.attachView(this);
        Intent intent = getIntent();
        uID = intent.getIntExtra("uID", 0);
    }

    private void init() {
//        indicatorView.setIndicatorColor(R.color.purple);
        loadingView.setVisibility(View.VISIBLE);
        userCenterPresenter.getFansUserInfo(uID);
    }

    private void setUserInfo2List(List<FansBean> infoList) {
        fansInfoList = infoList;
        userFansListAdapter = new UserFansListAdapter(FansListActivity.this, infoList);
        lvFansList.setAdapter(userFansListAdapter);
        lvFansList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();

                intent.putExtra("uID", userFansListAdapter.getItem(position).getFans().getData().getId());
                intent.putExtra("userImg", userFansListAdapter.getItem(position).getFans().getData().getAvatarSmall());
                intent.putExtra("username", userFansListAdapter.getItem(position).getFans().getData().getName());
                intent.putExtra("gender", userFansListAdapter.getItem(position).getFans().getData().getGender());
                intent.putExtra("level", userFansListAdapter.getItem(position).getFans().getData().getLevel());
                intent.putExtra("userNumber", userFansListAdapter.getItem(position).getFans().getData().getUserNumber());
                intent.putExtra("sign", userFansListAdapter.getItem(position).getFans().getData().getSign());

                intent.setClass(FansListActivity.this, UserInfoHomeActivity.class);
                startActivity(intent);
//                finish();
            }
        });

    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        userCenterPresenter = presenterComponent.getUserCenterPresenter();
    }

    @Override
    public void getFansUserInfo(List<FansBean> userInfoList) {
//        indicatorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        if (userInfoList.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
        }
        setUserInfo2List(userInfoList);
    }

    @Override
    public void getFocusUserInfo(List<MasterBean> userInfoList) {

    }

    @Override
    public void onfailed(String message) {
        loadingView.setVisibility(View.GONE);
        super.onfaild(message);
//       if (message.equals(Constants.FAILED_CONNECT)) {
        lvFansList.setVisibility(View.GONE);
        llEmptyView.setVisibility(View.VISIBLE);

//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (fansInfoList != null) {
//                for (FansBean info : fansInfoList) {
//                    Uri uri = Uri.parse(info.getFans().getData().getAvatarSmall());
//                    Picasso.with(this).invalidate(uri);
//                }
//            }
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (fansInfoList != null) {
            fansInfoList.clear();
            fansInfoList = null;
        }
        userFansListAdapter = null;
        System.gc();
        super.onDestroy();
    }
}
