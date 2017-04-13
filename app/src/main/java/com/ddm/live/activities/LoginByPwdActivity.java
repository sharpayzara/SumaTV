package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ddm.live.presenter.LoginPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.utils.CheckIsUserNumber;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.ILoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginByPwdActivity extends BaseActivity implements ILoginView {

    String phoneNum;
    String password;
    private RongYunPresenter presenterRongYun;
    private ProgressBarDialogFragment progressBarDialogFragment;

    private AppComponent appComponent;
    private LoginPresenter presenter;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @BindView(R2.id.login_phone)
    EditText phoneNumBt;

    @BindView(R2.id.login_password)
    EditText pwmBtn;


    @BindView(R2.id.login_by_phone_btn)
    LinearLayout loginBtn;

    @BindView(R2.id.denglu_text)
    TextView dengluText;
    @BindView(R2.id.sl_center)
    ScrollView scrollView;


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            phoneNum = phoneNumBt.getEditableText().toString();
            password = pwmBtn.getEditableText().toString();
            if (phoneNum.length() == 11 && password.length() > 0) {
//                loginBtn.setBackground(ContextCompat.getDrawable(LoginByPwdActivity.this, R.mipmap.denglu_btn_focus));
                dengluText.setEnabled(true);
                loginBtn.setClickable(true);
            } else {
//                loginBtn.setBackground(ContextCompat.getDrawable(LoginByPwdActivity.this, R.mipmap.denglu_btn));
                dengluText.setEnabled(false);
                loginBtn.setClickable(false);
            }
        }
    };

    @OnClick(R2.id.login_by_phone_btn)
    public void login() {
//        if (!NetworkUtils.isNetworkAvailable(this)) {
//            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
//            return;
//        }

        phoneNum = phoneNumBt.getEditableText().toString();
        password = pwmBtn.getEditableText().toString();
//        if (phoneNum.equals("") || password.equals("")) {
//            Toast.makeText(this, "请填写完整的登录信息", Toast.LENGTH_SHORT).show();
//        } else {
//            presenter.login(phoneNum, password);
//        }
        if (CheckIsUserNumber.isMobileNO(phoneNum) && password.length() > 0) {
            progressBarDialogFragment = new ProgressBarDialogFragment();
            if (!progressBarDialogFragment.isAdded()) {
                progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
            }

            presenter.login(phoneNum, password);
        }else {
            toast("手机号错误");
        }
    }

    @OnClick(R2.id.forget_pwm)
    public void retrievePassword() {
        Intent intent = new Intent(LoginByPwdActivity.this, RetrievePasswordActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R2.id.return_btn)
    public void onReturn() {
        /**
         * SDK打包时：将A代码块注释掉，将B代码块打开
         */
        //A代码块
        Intent intent = new Intent(LoginByPwdActivity.this, BFLoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
        //B代码块
//                Intent intent = new Intent(LoginByPwdActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
    }

    @OnClick(R.id.register_btn)
    public void onRegister() {
        Intent intent = new Intent(LoginByPwdActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.forget_pwm)
    public void onForget() {
        Intent intent = new Intent(LoginByPwdActivity.this, RetrievePasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_user_fans_list_title_background);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter.attachView(this);

        //文本框监听
        phoneNumBt.addTextChangedListener(textWatcher);
        pwmBtn.addTextChangedListener(textWatcher);
        dengluText.setEnabled(false);

        Intent intent = getIntent();
        if (null != intent) {
            phoneNum = intent.getStringExtra("phoneNum");
            password = intent.getStringExtra("password");
            if (phoneNum != null && password != null) {
                phoneNumBt.setText(phoneNum);
                pwmBtn.setText(password);
            }
//            if(appUserActivity.isLogined()){//SDK中打开
//                Intent intent1=new Intent(LoginByPwdActivity.this,HomeActivity.class);
//                startActivity(intent1);
//                finish();
//            }
        }

        phoneNumBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
        pwmBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
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

    @Override
    protected void toast(String message) {
        super.toast(message);
    }


    @Override
    public void registerResult() {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }

        //登录成功
//        if (appUserActivity.isLogined()) {
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
                        Intent intent = new Intent(LoginByPwdActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    @Override
    public void onfailed(String message) {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        super.onfaild(message);
    }
}

