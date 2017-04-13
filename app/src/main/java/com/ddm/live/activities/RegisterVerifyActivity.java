package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.ddm.live.presenter.RegisterPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.utils.CheckIsUserNumber;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IRegisterResetPwmView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegisterVerifyActivity extends BaseActivity implements IRegisterResetPwmView {
    private RegisterPresenter presenter;

    private AppComponent appComponent;
    private ProgressBarDialogFragment progressBarDialogFragment;
    private RongYunPresenter presenterRongYun;

    public AppComponent getAppComponent() {
        return appComponent;
    }


    @BindView(R2.id.register_captcha)
    EditText registerCaptcha;
    @BindView(R2.id.send_text)
    TextView sendText;
    @BindView(R2.id.sl_center)
    ScrollView scrollView;
    @BindView(R2.id.phone_num_tv)
    TextView phoneNumTv;
    @OnClick(R2.id.return_btn)
    public void onReturn() {
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
    }

    String phoneNum;
    String password;
    String code;
    private boolean timerFlag = true;


    //获取验证码
    @OnClick(R2.id.send_text)
    public void send() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timerFlag) {
            if (CheckIsUserNumber.isMobileNO(phoneNum)) {
                if (!progressBarDialogFragment.isAdded()) {
                    progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
                }
                presenter.getRegisterCode(phoneNum);
            }else {
                toast("手机号错误");
            }
        }

    }

    @OnClick(R2.id.regist_btn_text)
    public void next() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        code = registerCaptcha.getEditableText().toString();
//        if (phoneNum.equals("") || password.equals("") || code.equals("")) {
//            Toast.makeText(this, "请填写完整的注册信息", Toast.LENGTH_SHORT).show();
//        } else {
//            presenter.registerByPhoneNumber(phoneNum, password, code);
//        }
//
        if (phoneNum.length() == 11 && password.length() > 0 && code.length() > 0) {
            if (!progressBarDialogFragment.isAdded()) {
                progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
            }

            presenter.registerByPhoneNumber(phoneNum, password, code);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_user_fans_list_title_background);
        setContentView(R.layout.activity_register_verify);
        progressBarDialogFragment = new ProgressBarDialogFragment();
        ButterKnife.bind(this);
        presenter.attachView(this);
        phoneNum = getIntent().getStringExtra("phoneNum");
        password = getIntent().getStringExtra("password");
        phoneNumTv.setText(phoneNum.substring(0,3) + " " +phoneNum.substring(3,7) + " " + phoneNum.substring(7,11));
        //文本框监听
        sendText.performClick();
    }

    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getHeight());
            }
        }, 300);
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
        }
    };
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            sendText.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            timerFlag = true;
            sendText.setText("获取验证码");
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        presenter = presenterComponent.getRegisterPresenter();
    }

    @Override
    public void onResult() {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }

        //登录成功
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * SDK打包时：打开连接融云代码
         */
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
                        Intent intent = new Intent(RegisterVerifyActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {

            }
        });

    }

    @Override
    public void obtainCodeResult() {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        toast("验证码发送成功！");
        timer.start();
        timerFlag = false;
    }

    @Override
    public void onfailed(String message) {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        super.onfaild(message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(RegisterActivity.this, LoginByPwdActivity.class);
//            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
        }
        return false;
    }
}
