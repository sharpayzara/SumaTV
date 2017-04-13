package com.ddm.live.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.fragments.ProgressBarDialogFragment;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.presenter.LoginPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.ILoginView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.InputStream;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BFLoginActivity extends BaseActivity implements ILoginView {

    private UMShareAPI mShareAPI = null;
    private SHARE_MEDIA platform;
    private LoginPresenter presenter;
    private boolean isLoading = false;
    private RongYunPresenter presenterRongYun;
    private Dialog loginDialog;
    private static final int CHANGE_IMAGE_FIRST = 1000;
    private static final int CHANGE_IMAGE = 1001;
    private static final int CHANGE_TIME_FIRST = 500; // 第一次更换时间间隔
    private static final int CHANGE_TIME = 3000; // 更换时间间隔
    private static final int SCREEN_PROTECT_IMAGES_COUNT = 3;
    int[] images = {R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3};
    int index = 0;
    private Animation myShow;
    private Animation myHide;
    private Animation myFirstShow;
    private Animation myFirstHide;
    private boolean img1Flag; // 表示img1是否是当前显示的图片

    Bitmap[] bitmaps = new Bitmap[SCREEN_PROTECT_IMAGES_COUNT];
    Bitmap[] localBitmaps = new Bitmap[SCREEN_PROTECT_IMAGES_COUNT];

    private ProgressBarDialogFragment progressBarDialogFragment;

    public ProgressBarDialogFragment getProgressBarDialogFragment() {
        return progressBarDialogFragment;
    }

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }


    // 用于和img轮流显示，实现渐入渐出效果
    @BindView(R.id.xz_login_img1)
    ImageView loginBgImg1;

    @BindView(R2.id.xz_login_img2)
    ImageView loginBgImg2;

    @OnClick(R2.id.butter_phone_login_btn)
    public void loginByMima() {
        Intent intent = new Intent(BFLoginActivity.this, LoginByPwdActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.butter_qq_login_btn)
    public void loginByQQ() {
    /*    Intent intent = new Intent(BFLoginActivity.this, LoginByPwdActivity.class);
        startActivity(intent);*/
    }

    @OnClick(R2.id.butter_wechat_login_btn)
    public void loginByWx() {
        progressBarDialogFragment = new ProgressBarDialogFragment();
        isLoading = true;
        userWechatLogin();
    }

    @OnClick(R2.id.butter_phone_login_btn)
    public void loginBySina() {
       /* Intent intent = new Intent(BFLoginActivity.this, LoginByPwdActivity.class);
        startActivity(intent);*/
    }

/*
    @OnClick(R2.id.register_by_phone)
    public void registerByPhone() {
        Intent intent = new Intent(BFLoginActivity.this, RegisterActivity.class);
//        Intent intent = new Intent(BFLoginActivity.this, RegisterByPhoneNumActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.butter_login_btn)
    public void clickToLoginBtn() {
//        if (!NetworkUtils.isNetworkAvailable(this)) {
//            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
//            return;
//        }
        progressBarDialogFragment = new ProgressBarDialogFragment();
        isLoading = true;
        userWechatLogin();
    }*/

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            index++;
            if (index >= SCREEN_PROTECT_IMAGES_COUNT) {
                index = 0;
            }
            switch (msg.what) {
                case CHANGE_IMAGE_FIRST:
// 之前显示的那张图片，加渐变动画，消失，消失后设置下一张图；之前未显示的那张图片，渐变动画，出现；
                    loginBgImg1.startAnimation(myFirstHide);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loginBgImg1.setVisibility(View.INVISIBLE);
                        }
                    }, 400);

                    setImage(loginBgImg2, index);
                    loginBgImg2.setVisibility(View.VISIBLE);
                    loginBgImg2.startAnimation(myFirstShow);
                    img1Flag = false;

                    mHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, CHANGE_TIME);
                    break;

                case CHANGE_IMAGE:
// 之前显示的那张图片，加渐变动画，消失，消失后设置下一张图；之前未显示的那张图片，渐变动画，出现；
//            img1.setImageResource(images[index]);
                    if (img1Flag) {
//                        Log.e("ScreenProtectActivity", "img1消失，img2显示");
                        loginBgImg1.startAnimation(myHide);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loginBgImg1.setVisibility(View.INVISIBLE);
                            }
                        }, 400);
//                img2.setImageResource(images[index]);
                        setImage(loginBgImg2, index);
                        loginBgImg2.setVisibility(View.VISIBLE);
                        loginBgImg2.startAnimation(myShow);
                        img1Flag = false;
                    } else {
//                        Log.e("ScreenProtectActivity", "img2消失，img1显示");
                        loginBgImg2.startAnimation(myHide);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loginBgImg2.setVisibility(View.INVISIBLE);
                            }
                        }, 400);
                        setImage(loginBgImg1, index);
                        loginBgImg1.setVisibility(View.VISIBLE);
                        loginBgImg1.startAnimation(myShow);
                        img1Flag = true;
                    }

                    mHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, CHANGE_TIME);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarTranslucent(this);//设置状态栏透明
        UILApplication.getInstance().finishAll();
        UILApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_bflogin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        presenter.attachView(this);
        initView();
        mHandler.sendEmptyMessageDelayed(CHANGE_IMAGE_FIRST, CHANGE_TIME_FIRST);
        if (getIntent().getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK) {
            Toast.makeText(this, "登录过期，请重新登录", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        setImage(loginBgImg1, index);
        myShow = AnimationUtils.loadAnimation(this, R.anim.xz_login_visiable);
        myHide = AnimationUtils.loadAnimation(this, R.anim.xz_login_invisiable);
        myFirstShow = AnimationUtils.loadAnimation(this, R.anim.xz_login_first_visiable);
        myFirstHide = AnimationUtils.loadAnimation(this, R.anim.xz_login_first_invisiable);

        img1Flag = true;
    }

    private void setImage(ImageView img, int index) {
        if (index >= SCREEN_PROTECT_IMAGES_COUNT) {
            index = 0;
        }

        for (int i = 0; i < SCREEN_PROTECT_IMAGES_COUNT; i++) {
            localBitmaps[i] = readBitMap(this, images[i]);
        }
        img.setImageBitmap(localBitmaps[index]);
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

        presenter = presenterComponent.getLoginPresenter();
    }

    private void userWechatLogin() {
        //微信授权登录
        platform = SHARE_MEDIA.WEIXIN;
        mShareAPI = UMShareAPI.get(this);
        if(!mShareAPI.isInstall(this,SHARE_MEDIA.WEIXIN)){
            toast("请安装WEIXIN客户端");
            return;
        }
        if (!progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
        }
        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                if (data == null) {//第三方接口调用先判空
                    return;
                }
                appUserActivity.setExpires_in(data.get("expires_in"));
                appUserActivity.setOpenid(data.get("openid"));
                appUserActivity.setRefresh_token(data.get("refresh_token"));
                appUserActivity.setAccess_token(data.get("access_token"));
                appUserActivity.setUnionid(data.get("unionid"));

                //根据微信返回的信息判断是注册还是登录

                mShareAPI.getPlatformInfo(BFLoginActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        if (map == null) {
                            return;
                        }
                        if (map.get("sex")!=null && map.get("sex").equals("1")) {
                            appUserActivity.setGender(1);
                        } else {
                            appUserActivity.setGender(0);
                        }
                        appUserActivity.setCity(map.get("city"));
                        appUserActivity.setProvince(map.get("province"));
                        appUserActivity.setLanguage(map.get("language"));
                        appUserActivity.setHeadimgurl(map.get("headimgurl"));
                        appUserActivity.setCountry(map.get("country"));
                        appUserActivity.setKey(map.get("key"));
                        appUserActivity.setNickname(map.get("nickname"));
                        presenter.loginByWX();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
                            progressBarDialogFragment.dismiss();
                        }
//                        Log.e(TAG, "onError: 微信登录" + throwable.toString());

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
                            progressBarDialogFragment.dismiss();
                        }
//                        Log.e(TAG, "onCancel: ");

                    }
                });

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                toast("授权失败");
                if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
                    progressBarDialogFragment.dismiss();
                }
                isLoading = false;
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
                    progressBarDialogFragment.dismiss();
                }
                isLoading = false;
            }
        });
    }

    @Override
    public void registerResult() {
        //如果注册成功
//        if (appUserActivity.isLogined()) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        //连接融云
        AppComponent appComponent = AppApplication.get(this).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();
        presenterRongYun = presenterComponent.getRongYunPresenter();

        presenterRongYun.initConnection(appUserActivity, this).observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<RongIMClient>() {
                    @Override
                    public void call(RongIMClient rongIMClient) {
                        appUserActivity.setRongIMClient(rongIMClient);
                        appUserActivity.setIsLogined(true);
                        Intent intent = new Intent(BFLoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
//            showLoaddingDialog.cancel();
//        } else {
//            toast("登录失败，请稍后重试");
////            showLoaddingDialog.cancel();
//        }
    }

    @Override
    public void onfailed(String message) {
        if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        super.onfaild(message);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isLoading) {
                exitBy2Click(); //调用双击退出函数
            }
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            UILApplication.getInstance().exit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        mHandler.removeMessages(CHANGE_IMAGE_FIRST);
        mHandler.removeMessages(CHANGE_IMAGE);
        mHandler.removeCallbacksAndMessages(null);
        for (int i = 0; i < bitmaps.length; i++) {
            if (bitmaps[i] != null && !bitmaps[i].isRecycled()) {
                // 回收并且置为null
                bitmaps[i].recycle();
                bitmaps[i] = null;
            }
        }
        System.gc();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < bitmaps.length; i++) {
            if (bitmaps[i] != null && !bitmaps[i].isRecycled()) {
                // 回收并且置为null
                bitmaps[i].recycle();
                bitmaps[i] = null;
            }
        }
        System.gc();
        super.onDestroy();
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
