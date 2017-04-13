package com.ddm.live.activities;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.fragments.FansContributonListFragment;
import com.ddm.live.fragments.LiveMainFragment;
import com.ddm.live.fragments.PreStartLiveFragment;
import com.ddm.live.fragments.SWCodeCameraStreamingFragment;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.views.iface.ISWcodeCameraStreamingView;

public class MasterStartLiveActivity extends FragmentActivity implements ISWcodeCameraStreamingView {
    private SWCodeCameraStreamingFragment swCodeCameraStreamingFragment;
    private PreStartLiveFragment preStartLiveFragment;
    private LiveMainFragment liveMainFragment;
    private FansContributonListFragment fansContributonListFragment;
    private AppUser appUser;

    public SWCodeCameraStreamingFragment getSwCodeCameraStreamingFragment() {
        return swCodeCameraStreamingFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UILApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        //设置状态颜色
//        StatusBarUtils.setWindowStatusBarColor(this,R.color.tatusbar_half_trans);
        setContentView(R.layout.activity_master_start_live_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        appUser = AppApplication.getInstance().getAppUser();

        swCodeCameraStreamingFragment = new SWCodeCameraStreamingFragment();
        swCodeCameraStreamingFragment.attachISWcodeCameraStreamingView(this);
        liveMainFragment = new LiveMainFragment();
        swCodeCameraStreamingFragment.attachFragment(liveMainFragment);
        preStartLiveFragment = new PreStartLiveFragment();
        preStartLiveFragment.attachView(swCodeCameraStreamingFragment);


        getSupportFragmentManager().beginTransaction().add(R.id.master_live_view, swCodeCameraStreamingFragment).commit();//添加直播相机层fragment
        getSupportFragmentManager().beginTransaction().add(R.id.live_fragments, preStartLiveFragment).commit();//添加直播准备层fragment
    }

    //进入直播界面
    @Override
    public void showLiveLayer(Bundle bundle) {

        getSupportFragmentManager().beginTransaction().remove(preStartLiveFragment).commit();//移除直播准备层fragment
        liveMainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.live_fragments, liveMainFragment).commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(liveMainFragment.isVisible()){
                liveMainFragment.getLiveLayerFragment().onKeyDown(keyCode, event);
            }else {
                finish();
                overridePendingTransition(0, R.anim.activity_close_form_bottom);
            }

        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
        }

        if (isGranted) {
            preStartLiveFragment.reSetLocation();
        }
    }
    @Override
    public void onfailed(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
