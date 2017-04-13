package com.ddm.live.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ytzys on 2017/2/22.
 */
public abstract class UserCenterBaseActivity extends BaseActivity {
    @BindView(R.id.header_user_center_back_img)
    ImageView backImg;

    @BindView(R.id.header_user_center_title)
    TextView title;

    @BindView(R.id.header_user_center_right_txt)
    TextView rightTitle;

    AppComponent appComponent;
    PresenterComponent presenterComponent;

    @OnClick(R.id.header_user_center_back_img)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule()).build();
    }
}
