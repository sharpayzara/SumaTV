package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity{

    @BindView(R2.id.register_phone)
    EditText registerPhoneBtn;
    @BindView(R2.id.register_pwm)
    EditText registerPwm;
    @BindView(R2.id.regist_btn_text)
    TextView registBtnText;
    String phoneNum;
    String password;
    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }
    @OnClick(R2.id.regist_btn_text)
    public void onVerify() {
        Intent intent = new Intent(RegisterActivity.this, RegisterVerifyActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("password",password);
        startActivity(intent);
    }
    @OnClick(R2.id.return_btn)
    public void onReturn() {
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_user_fans_list_title_background);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        registBtnText.setEnabled(false);
        registerPhoneBtn.addTextChangedListener(textWatcher);
        registerPwm.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            phoneNum = registerPhoneBtn.getEditableText().toString();
            password = registerPwm.getEditableText().toString();

            if (phoneNum.length() == 11 && password.length() > 0) {
                registBtnText.setEnabled(true);
//                nextBtn.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.mipmap.denglu_btn_focus));
            } else {
                registBtnText.setEnabled(false);
//                nextBtn.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.mipmap.denglu_btn));
            }
        }
    };
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
