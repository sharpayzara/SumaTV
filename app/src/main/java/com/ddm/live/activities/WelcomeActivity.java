package com.ddm.live.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.utils.camera.util.DisplayUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @BindView(R2.id.welcom_img)
    ImageView imagBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarTranslucent(this);//设置状态栏透明
        setContentView(R.layout.activity_welcome1);
        UILApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        initView();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (appUserActivity.isLogined()) {
//                    if(!NetworkUtils.isNetworkAvailable(WelcomeActivity.this)){
//                        Looper.prepare();
//                        Toast.makeText(WelcomeActivity.this,"当前无网络",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(WelcomeActivity.this, BFLoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                        Looper.loop();
//                    }else {}
                        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);

                } else {
                    Intent intent = new Intent(WelcomeActivity.this, BFLoginActivity.class);
                    startActivity(intent);
                }

            }
        };
        timer.schedule(task, 1000);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    public void initView() {
//        Picasso.with(this)
//                .load(R.mipmap.welcome2)
//                .placeholder(R.mipmap.welcome2)
//                .error(R.mipmap.welcome2)
//                .into(imagBackground);
        RelativeLayout welcomeLoading = (RelativeLayout) findViewById(R.id.welcome_loading);
        ViewGroup.LayoutParams lp = welcomeLoading.getLayoutParams();
        lp.width = DisplayUtil.getScreenW(this);
        lp.height = DisplayUtil.getDeviceScreenHeight(this);
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            UILApplication.getInstance().exit();
        }
    }
}
