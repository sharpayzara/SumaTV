package com.ddm.live.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.activities.HomeActivity;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.presenter.LoginPresenter;
import com.ddm.live.views.iface.ILoginView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
//import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * 还没有登录的界面
 * Created by JiGar on 2016/1/18.
 */
public class UserCenterLoginFragment extends BaseFragment implements ILoginView {

    private String TAG = "zz";
    private UMShareAPI mShareAPI = null;
    private SHARE_MEDIA platform;
    private LoginPresenter presenter;
//    private SweetAlertDialog showLoaddingDialog;


    @OnClick(R2.id.user_login_btn)
    public void toLogin() {
        startActivity(new Intent(getActivity(), BFLoginActivity.class));
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center_logout, container, false);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = ((HomeActivity) getActivity()).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        presenter = presenterComponent.getLoginPresenter();
    }

    private void userWechatLogin() {
        //微信授权登录
        platform = SHARE_MEDIA.WEIXIN;
        mShareAPI = UMShareAPI.get(getActivity());
        mShareAPI.doOauthVerify(getActivity(), platform, new UMAuthListener() {

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

                appUser.setExpires_in(data.get("expires_in"));
                appUser.setOpenid(data.get("openid"));
                appUser.setRefresh_token(data.get("refresh_token"));
                appUser.setAccess_token(data.get("access_token"));
                appUser.setUnionid(data.get("unionid"));

                //根据微信返回的信息判断是注册还是登录

                mShareAPI.getPlatformInfo(getActivity(), platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        if (map.get("sex").equals("1")) {
                            appUser.setGender(1);
                        } else {
                            appUser.setGender(0);
                        }
                        appUser.setCity(map.get("city"));
                        appUser.setNickname(map.get("nickname"));
                        appUser.setProvince(map.get("province"));
                        appUser.setLanguage(map.get("language"));
                        appUser.setHeadimgurl(map.get("headimgurl"));
                        appUser.setCountry(map.get("country"));
                        appUser.setKey(map.get("key"));

                        presenter.loginByWX();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Log.e(TAG, "onError: 微信登录" + throwable.toString());

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Log.e(TAG, "onCancel: ");

                    }
                });

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
//                Toast.makeText(getActivity(), "取消了授权", Toast.LENGTH_SHORT).show();
//                showLoaddingDialog.cancel();
            }
        });
    }

    @Override
    public void registerResult() {
        //如果登录成功
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (appUser.isLogined()) {
            UserCenterLoginoutFragment userCenterLoginoutFragment = new UserCenterLoginoutFragment();
            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginoutFragment);
        } else {
            UserCenterLoginFragment userCenterLoginFragment = new UserCenterLoginFragment();
            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginFragment);
        }

        fragmentTransaction.commit();
//        showLoaddingDialog.cancel();
    }


    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }
}
