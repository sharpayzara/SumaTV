package com.ddm.live.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.utils.MaxLengthWatcher;
import com.ddm.live.views.iface.IUserCenterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoChangeItemActivity extends BaseActivity implements IUserCenterView {

    private AppComponent appComponent;
    private UserCenterPresenter userCenterPresenter;
    private String changeTitle;

    @OnClick(R2.id.user_change_info_back_btn)
    public void goBack() {
        finish();
    }

    @BindView(R2.id.user_change_info_title_text)
    TextView txChangeTitle;

    @BindView(R2.id.user_change_info_submit_text)
    TextView txSubmitBtn;

    @BindView(R2.id.user_change_info_editor)
    EditText etChangeText;

    @BindView(R2.id.user_change_info_editor_text_num)
    TextView txEditorTextNum;

    @BindView(R2.id.user_change_info_editor_text_max)
    TextView txEditorTextMaxNum;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_change_item);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        userCenterPresenter.attachView(this);

        Intent intent = getIntent();
        changeTitle = intent.getStringExtra("title");
        init();
    }

    private void init() {

        switch (changeTitle) {
            case "name":
                txChangeTitle.setText("昵称");
                etChangeText.setText(appUserActivity.getNickname());
                txEditorTextNum.setText(String.valueOf(appUserActivity.getNickname().length()));
                txEditorTextMaxNum.setText("16");
                etChangeText.addTextChangedListener(new MaxLengthWatcher(etChangeText, txEditorTextNum, 16));
                txSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etChangeText.getText().toString().equals("")) {
                            toast("请输入用户名");
                        } else if (etChangeText.getText().toString().equals(appUserActivity.getNickname())) {
                            changeResult();
                        } else {
                            userCenterPresenter.changeUserName(etChangeText.getText().toString());
                        }
                    }
                });
                break;

            case "sign":
                txChangeTitle.setText("个性签名");
                txEditorTextMaxNum.setText("50");
                if (appUserActivity.getSign().equals("")) {
                    etChangeText.setText(R.string.user_introduction);
                    txEditorTextNum.setText(String.valueOf(this.getString(R.string.user_introduction).length()));
                } else {
                    etChangeText.setText(appUserActivity.getSign());
                    txEditorTextNum.setText(String.valueOf(appUserActivity.getSign().length()));
                }
                etChangeText.addTextChangedListener(new MaxLengthWatcher(etChangeText, txEditorTextNum, 50));
                txSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userCenterPresenter.changeUserSign(etChangeText.getText().toString());
                    }
                });
                break;
        }

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
    public void changeResult() {
        finish();
    }

    @Override
    public void getUploadToken(String token, String host) {

    }

    @Override
    public void getUserInfo(SpecUserInfoBean userInfo) {

    }

    @Override
    public void addFocusResult(boolean result, String msg) {

    }

    @Override
    public void cancelFocusResult(boolean result, String msg) {

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }
}
