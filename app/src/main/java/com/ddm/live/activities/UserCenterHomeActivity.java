package com.ddm.live.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterHomeActivity extends BaseActivity implements View.OnClickListener {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @BindView(R2.id.user_center_img)
    ImageView imgUserImg;

    @BindView(R2.id.user_center_name)
    TextView txUserName;

    @BindView(R2.id.user_center_id)
    TextView txUserID;

    @OnClick(R2.id.user_center_home_back_img)
    public void clickToBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UILApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_user_center_home);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        if (appUserActivity.getHeadimgurl().isEmpty()) {
            imgUserImg.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(this)
                    .load(appUserActivity.getHeadimgurl())
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(imgUserImg);
        }
        txUserName.setText(appUserActivity.getNickname());
        txUserID.setText(appUserActivity.getId());

    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    @Override
    public void onClick(View v) {

    }
}
