package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.AccountPresenter;
import com.ddm.live.utils.DataCleanManager;
import com.ddm.live.utils.UpdateManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ytzys on 2017/2/22.
 */
public class SettingActivity extends UserCenterBaseActivity {

    @BindView(R.id.check_new_version)
    RelativeLayout checkNewVersinonRl;

    @BindView(R.id.clean_cache)
    RelativeLayout cleanCacheRl;

    @BindView(R.id.about_us)
    RelativeLayout aboutUsRl;

    @BindView(R.id.current_version)
    TextView currentVersion;

    @BindView(R.id.cache_size)
    TextView cacheSize;

    @BindView(R.id.quit_button)
    TextView quitButton;

    @OnClick(R.id.quit_button)
    public void quit() {
        if (appUser.isLogined()) {
            appUser.clearUserInfo();
            /**
             * SDK打包时：注释掉跳转代码
             */
            //跳转代码
            Intent intent = new Intent(this, BFLoginActivity.class);
            startActivity(intent);
            //关闭代码
            finish();
            overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
        }
    }

    @OnClick(R.id.about_us)
    public void aboutUs() {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.clean_cache)
    public void cleanCache() {
        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(this);
            Toast.makeText(this, "成功清除" + cacheSize + "缓存", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("zz", e.getMessage());
        }
        DataCleanManager.clearAllCache(this);
        DataCleanManager.cleanInternalCache(this);
    }

    AccountPresenter accountPresenter;
    AppUser appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        appUser = ((AppApplication) getApplication()).getAppUser();
        rightTitle.setVisibility(View.GONE);
        title.setText("设置");
        String version = UpdateManager.getCurrentVersionInfo(this);
        currentVersion.setText(version);
        try {
            String cacheSizeStr = DataCleanManager.getTotalCacheSize(this);
            cacheSize.setText(cacheSizeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        super.setupActivityComponent(appComponent);
        accountPresenter = presenterComponent.getAccountPresenter();
    }
}
