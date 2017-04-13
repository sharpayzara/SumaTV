package com.ddm.live.activities;

import android.os.Bundle;
import android.view.View;

import com.ddm.live.R;

import butterknife.ButterKnife;

/**
 * Created by ytzys on 2017/2/24.
 */
public class AboutUsActivity extends UserCenterBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        title.setText("关于我们");
        rightTitle.setVisibility(View.GONE);
    }
}
