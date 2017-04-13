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
import com.ddm.live.adapters.UserFocusListAdapter;
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

public class FocusListActivity extends BaseActivity implements IUserFansView {
    private UserCenterPresenter userCenterPresenter;
    private Integer uID;
    private AppComponent appComponent;
    private UserFocusListAdapter userFocusListAdapter;
    private List<MasterBean> focusInfoList;
    @BindView(R2.id.user_focus_list)
    ListView lvFocusList;
    //    @BindView(R2.id.indicator)
//    AVLoadingIndicatorView indicatorView;
    @BindView(R2.id.loadView)
    LoadingView loadingView;

    @BindView(R2.id.user_focus_list_no_data_layout)
    LinearLayout llEmptyView;

    @BindView(R2.id.focus_list_no_data_text)
    TextView txEmptyText;

    @OnClick(R2.id.user_focus_list_title_back_img)
    public void goBack() {
//        if (focusInfoList != null) {
//            for (MasterBean info : focusInfoList) {
//                Uri uri = Uri.parse(info.getMaster().getData().getAvatarSmall());
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
        setContentView(R.layout.activity_focus_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        userCenterPresenter.attachView(this);
        Intent intent = getIntent();
        uID = intent.getIntExtra("uID", 0);
    }

    private void init() {
//        indicatorView.setIndicatorColor(R.color.purple);
        loadingView.setVisibility(View.VISIBLE);
        userCenterPresenter.getFocusUserInfo(uID);
    }

    private void setUserInfo2List(final List<MasterBean> infoList) {
        focusInfoList = infoList;
        userFocusListAdapter = new UserFocusListAdapter(FocusListActivity.this, infoList);
        lvFocusList.setAdapter(userFocusListAdapter);
        lvFocusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                intent.putExtra("uID", userFocusListAdapter.getItem(position).getMaster().getData().getId());
                intent.putExtra("userImg", userFocusListAdapter.getItem(position).getMaster().getData().getAvatarSmall());
                intent.putExtra("username", userFocusListAdapter.getItem(position).getMaster().getData().getName());
                intent.putExtra("gender", userFocusListAdapter.getItem(position).getMaster().getData().getGender());
                intent.putExtra("level", userFocusListAdapter.getItem(position).getMaster().getData().getLevel());
                intent.putExtra("userNumber", userFocusListAdapter.getItem(position).getMaster().getData().getUserNumber());
                intent.putExtra("sign", userFocusListAdapter.getItem(position).getMaster().getData().getSign());

                intent.setClass(FocusListActivity.this, UserInfoHomeActivity.class);
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
    }

    @Override
    public void getFocusUserInfo(List<MasterBean> userInfoList) {
//        indicatorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        if (userInfoList.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
        }
        setUserInfo2List(userInfoList);
    }

    @Override
    public void onfailed(String message) {
        loadingView.setVisibility(View.GONE);
//  if (message.equals(com.ddm.live.constants.Constants.FAILED_CONNECT)) {
        lvFocusList.setVisibility(View.GONE);
        llEmptyView.setVisibility(View.VISIBLE);
//        }
        toast(message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (focusInfoList != null) {
//                for (MasterBean info : focusInfoList) {
//                    Uri uri = Uri.parse(info.getMaster().getData().getAvatarSmall());
//                    Picasso.with(this).invalidate(uri);
//                }
//            }
            finish();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        if (focusInfoList != null) {
            focusInfoList.clear();
            focusInfoList = null;
        }
        userFocusListAdapter = null;
        System.gc();
        super.onDestroy();
    }
}
