package com.ddm.live.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.activities.BlueUserVideoManageActivity;
import com.ddm.live.activities.CerfityActivity;
import com.ddm.live.activities.FansContributionListActivity;
import com.ddm.live.activities.FansListActivity;
import com.ddm.live.activities.FocusListActivity;
import com.ddm.live.activities.HelpActivity;
import com.ddm.live.activities.MyGradeActivity;
import com.ddm.live.activities.MyStarActivity;
import com.ddm.live.activities.RechargeActivity;
import com.ddm.live.activities.SettingActivity;
import com.ddm.live.activities.UserInfoChangeActivity;
import com.ddm.live.activities.UserInfoHomeActivity;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.utils.DataCleanManager;
import com.ddm.live.views.iface.IUserLoginOutView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by JiGar on 2016/1/18.
 */
public class UserCenterLoginoutFragment extends BaseFragment implements IUserLoginOutView {

    private static final String GET_FANS_NUM = "0";
    private static final String GET_FOCUS_NUM = "1";

    @BindView(R2.id.user_center_login_fragment)
    RelativeLayout rlUserCentFragment;

    @BindView(R2.id.user_center_user_gender)
    ImageView ivUserGender;
    @BindView(R2.id.user_center_user_level)
    ImageView ivUserLevel;
    @BindView(R2.id.user_center_user_le_id)
    TextView txUserLeID;
    @BindView(R2.id.user_center_user_sign)
    TextView txUserSign;

    @OnClick(R2.id.user_center_editor_user_info)
    public void editorUserInfo() {
        Intent intent = new Intent(getActivity(), UserInfoChangeActivity.class);
        startActivity(intent);
    }

    @BindView(R2.id.user_center_user_img)
    ImageView ivUser;

    @OnClick(R2.id.user_center_fans_contribution_layout)
    public void toFansContributionList() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FansContributionListActivity.class);
        intent.putExtra("uID", appUser.getId());
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_user_img)
    public void toUserHome() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), UserInfoHomeActivity.class);
        intent.putExtra("uID", appUser.getId());
        intent.putExtra("userImg",appUser.getSmallHeadimgurl());
        intent.putExtra("username",appUser.getNickname());
        intent.putExtra("gender",appUser.getGender());
        intent.putExtra("level",appUser.getLevel());
        intent.putExtra("userNumber",appUser.getLeNum());
        intent.putExtra("sign",appUser.getSign());
        startActivity(intent);
    }

    @BindView(R2.id.user_center_user_name)
    TextView tvUser;

    @BindView(R2.id.blue_user_login_fan_num)
    TextView txFansNum;

    @OnClick(R2.id.user_center_fans_layout)
    public void toUserFansList() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FansListActivity.class);
        intent.putExtra("uID", appUser.getId());
        startActivity(intent);
    }

    @BindView(R2.id.blue_user_login_focus_num)
    TextView txFocusNum;

    @OnClick(R2.id.user_center_focus_layout)
    public void toUserFocusList() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FocusListActivity.class);
        intent.putExtra("uID", appUser.getId());
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_login_out_layout)
    public void clickTologinOut() {
        //如果登录成功
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (appUser.isLogined()) {
            appUser.clearUserInfo();
            /**
             * SDK打包时：注释掉跳转代码
             */
            //跳转代码
            Intent intent = new Intent(getActivity(), BFLoginActivity.class);
            startActivity(intent);
            //关闭代码
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
        } else {
            UserCenterLoginoutFragment userCenterLoginoutFragment = new UserCenterLoginoutFragment();
            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginoutFragment);
        }

        fragmentTransaction.commit();
    }

    @OnClick(R2.id.user_center_video_manage_layout)
    public void clickToUserLiveList() {

        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserLiveListFragment userLiveListFragment = new UserLiveListFragment();
        fragmentTransaction.replace(R.id.user_center_content, userLiveListFragment);
        fragmentTransaction.commit();*/

        Intent intent = new Intent(getActivity(), BlueUserVideoManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_clear_cache_layout)
    public void clearCache() {
        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(getActivity());
            Toast.makeText(getActivity(), "成功清除" + cacheSize + "缓存", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("zz", e.getMessage());
        }
        DataCleanManager.clearAllCache(getActivity());
        DataCleanManager.cleanInternalCache(getActivity());
    }

    @OnClick(R2.id.user_center_about_us_layout)
    public void aboutUs() {

    }

    @OnClick(R2.id.user_center_buy_lebi_layout)
    public void buyLebi() {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_mystar)
    public void myStar() {
        Intent intent = new Intent(getActivity(), MyStarActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_grade)
    public void myGrade() {
        Intent intent = new Intent(getActivity(), MyGradeActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_certify)
    public void certify() {
        Intent intent = new Intent(getActivity(), CerfityActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_help)
    public void help() {
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.user_center_setting)
    public void setting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @Inject
    UserCenterPresenter userPresenter;
    private SpecUserInfoBean specUserInfoBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center_login, container, false);

        EventBus.getDefault().register(this);

        ButterKnife.bind(this, view);

        userPresenter.attachView(this);

        if (appUser.isLogined()) {
            initAppUI();
        }
        return view;
    }

    public void initData() {
        Integer[] ids = {appUser.getId()};
        userPresenter.getUserFocusOrFansInfo(ids);
    }

    private void initUI() {
        tvUser.setText(specUserInfoBean.getName());
        if (specUserInfoBean.getAvatarSmall()==null || specUserInfoBean.getAvatarSmall().isEmpty()) {
            ivUser.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(getContext())
                    .load(specUserInfoBean.getAvatarSmall())
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(ivUser);
        }
        txFansNum.setText(String.valueOf(specUserInfoBean.getFans().getData().getFansCount()));
        txFocusNum.setText(String.valueOf(specUserInfoBean.getFans().getData().getMastersCount()));
        if (0==specUserInfoBean.getGender()) {
            ivUserGender.setImageResource(R.mipmap.ic_profile_female);
        } else {
            ivUserGender.setImageResource(R.mipmap.ic_profile_male);
        }
        Integer level = specUserInfoBean.getLevel();
        String drawableName = "level_" + level;
        int drawResId = getDrawResourceID(drawableName);
        ivUserLevel.setImageResource(drawResId);
        txUserLeID.setText(String.valueOf(specUserInfoBean.getUserNumber()));
        txUserSign.setText(specUserInfoBean.getSign());
    }

    private void initAppUI() {
        tvUser.setText(appUser.getNickname());
        if (appUser.getSmallHeadimgurl()==null || appUser.getSmallHeadimgurl().isEmpty()) {
            ivUser.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(getContext())
                    .load(appUser.getSmallHeadimgurl())
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(ivUser);
        }
        txFansNum.setText(String.valueOf(appUser.getFansNum()));
        txFocusNum.setText(String.valueOf(appUser.getFocusNum()));
        if (0 == appUser.getGender()) {
            ivUserGender.setImageResource(R.mipmap.ic_profile_female);
        } else {
            ivUserGender.setImageResource(R.mipmap.ic_profile_male);
        }
        Integer level = appUser.getLevel();
        String drawableName = "level_" + level;
        int drawResId = getDrawResourceID(drawableName);
        ivUserLevel.setImageResource(drawResId);
        txUserLeID.setText(String.valueOf(appUser.getLeNum()));
        txUserSign.setText(appUser.getSign());
    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenterComponent.inject(this);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        initData();
//    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void getFocusOrFansInfo(SpecUserInfoBean userInfo) {
        specUserInfoBean = userInfo;
        initUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUserCenterEvent(RefreshUserCenterEvent event) {
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 根据图片的名称获取对应的资源id
     *
     * @param resourceName
     * @return
     */
    public int getDrawResourceID(String resourceName) {
        Resources res = getContext().getResources();
        int picid = res.getIdentifier(resourceName, "mipmap", getContext().getPackageName());
        return picid;
    }

}
