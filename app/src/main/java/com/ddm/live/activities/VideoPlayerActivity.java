package com.ddm.live.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.WatcherMainFragment;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.RongYunPresenter;

public class VideoPlayerActivity extends FragmentActivity {
    private AppUser appUser;
    private RongYunPresenter rongPresenter;
    LiveViewFragment liveViewFragment;
    WatcherMainFragment watcherMainFragment;

    public LiveViewFragment getLiveViewFragment() {
        return liveViewFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragmentComponent();
        UILApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        //设置状态颜色
//        StatusBarUtils.setWindowStatusBarColor(this,R.color.tatusbar_half_trans);
        setContentView(R.layout.video_play_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppApplication application = (AppApplication) getApplication();
        appUser = application.getAppUser();
//        initRongYun();
        addFragments();
    }

    /*这里可以看到的就是我们将初始化直播的Fragment添加到了这个页面作为填充
           * 并且将MainDialogFragment显示在该页面的顶部已达到各种不同交互的需求*/
    public void addFragments() {
        Bundle bundle = getIntent().getExtras();
        liveViewFragment = new LiveViewFragment();
        watcherMainFragment = new WatcherMainFragment();

        liveViewFragment.setArguments(bundle);
        liveViewFragment.attachFragment(watcherMainFragment);
        watcherMainFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.flmain2, liveViewFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragments, watcherMainFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            watcherMainFragment.getWatcherLayerFragment().onKeyDown(keyCode, event);
        }
        return false;
    }

    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(this).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();
        rongPresenter = presenterComponent.getRongYunPresenter();
    }
}