package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ddm.live.utils.CheckIsUserNumber;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IRegisterResetPwmView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrievePasswordActivity extends BaseActivity implements IRegisterResetPwmView {
    private RegisterPresenter presenter;
    private ProgressBarDialogFragment progressBarDialogFragment;
    private AppComponent appComponent;
    private boolean timerFlag = true;
    String phoneNum;
    String password;
    String code;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @BindView(R2.id.retrieve_phone)
    EditText retrievePhone;

    @BindView(R2.id.retrieve_captcha)
    EditText retrieveCaptcha;

    @BindView(R2.id.retrieve_pwm)
    EditText retrievePwm;
    @BindView(R2.id.retrieve_sent)
    TextView sendText;

    @BindView(R2.id.retrieve_text)
    TextView retriveText;


    @OnClick(R2.id.retrieve_sent)
    public void retrieveSend() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timerFlag) {
            phoneNum = retrievePhone.getEditableText().toString();
//        if (phoneNum.equals("")) {
//            Toast.makeText(this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
//        } else {
//            presenter.getResetPwmCode(phoneNum);
//        }
            if (CheckIsUserNumber.isMobileNO(phoneNum)) {
                if (!progressBarDialogFragment.isAdded()) {
                    progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
                }
                presenter.getResetPwmCode(phoneNum);
            }else {
                toast("手机号错误");
            }
        }
    }

    @BindView(R2.id.retrieve_next_btn)
    LinearLayout retrieveNextBtn;

    @BindView(R2.id.sl_center)
    ScrollView scrollView;


    @OnClick(R2.id.return_btn)
    public void onReturn() {
        Intent intent = new Intent(RetrievePasswordActivity.this, LoginByPwdActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
    }

    @OnClick(R2.id.retrieve_next_btn)
    public void next() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        phoneNum = retrievePhone.getEditableText().toString();
        password = retrievePwm.getEditableText().toString();
        code = retrieveCaptcha.getEditableText().toString();
//        if (phoneNum.equals("") || password.equals("") || code.equals("")) {
//            Toast.makeText(this, "请填写完整的注册信息", Toast.LENGTH_SHORT).show();
//        } else {
//            presenter.retriecePassword(phoneNum,password, code);
//        }
        if (phoneNum.length() == 11 && password.length() > 0 && code.length() > 0) {
            if (!progressBarDialogFragment.isAdded()) {
                progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
            }
            presenter.retriecePassword(phoneNum, password, code);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_user_fans_list_title_background);
        setContentView(R.layout.activity_retrieve_password);
        ButterKnife.bind(this);
        presenter.attachView(this);
        progressBarDialogFragment = new ProgressBarDialogFragment();
        //文本框监听
        retrievePhone.addTextChangedListener(textWatcher);
        retrievePhone.addTextChangedListener(textWatcher2);
        retrievePwm.addTextChangedListener(textWatcher);
        retrieveCaptcha.addTextChangedListener(textWatcher);
        retriveText.setEnabled(false);
        sendText.setEnabled(false);

        retrievePhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });

        retrieveCaptcha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });

        retrievePwm.setOnTouchListener(new View.OnTouchListener() {
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
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            phoneNum = retrievePhone.getEditableText().toString();
            password = retrievePwm.getEditableText().toString();
            code = retrieveCaptcha.getEditableText().toString();

            if (phoneNum.length() == 11 && code.length() > 0 && password.length() > 0) {
                retriveText.setEnabled(true);
//                retrieveNextBtn.setBackground(ContextCompat.getDrawable(RetrievePasswordActivity.this, R.mipmap.denglu_btn_focus));
                retrieveNextBtn.setClickable(true);
            } else {
                retriveText.setEnabled(false);
//                retrieveNextBtn.setBackground(ContextCompat.getDrawable(RetrievePasswordActivity.this, R.mipmap.denglu_btn));
                retrieveNextBtn.setClickable(false);
            }
        }
    };

    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            phoneNum = retrievePhone.getEditableText().toString();

            if (phoneNum.length() == 11) {
                sendText.setEnabled(true);
            } else {
                sendText.setEnabled(false);
            }
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
    public void onfailed(String message) {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        super.onfaild(message);
    }

    @Override
    public void onResult() {
        if (progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        toast("密码修改成功");
        Intent intent = new Intent(RetrievePasswordActivity.this, LoginByPwdActivity.class);
        intent.putExtra("phoneNum", retrievePhone.getText().toString());
        intent.putExtra("password", retrievePwm.getText().toString());
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(RetrievePasswordActivity.this, LoginByPwdActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
        }
        return false;
    }

}
