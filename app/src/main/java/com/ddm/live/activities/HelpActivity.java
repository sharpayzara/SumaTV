package com.ddm.live.activities;

import android.os.Bundle;
import android.view.View;

import com.ddm.live.R;
import com.ddm.live.inject.components.AppComponent;

import butterknife.ButterKnife;

/**
 * Created by ytzys on 2017/2/22.
 */
public class HelpActivity extends UserCenterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        rightTitle.setVisibility(View.GONE);
        title.setText("帮助与反馈");
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }
}
