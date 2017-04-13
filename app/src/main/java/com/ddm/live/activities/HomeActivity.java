package com.ddm.live.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.animation.EmptyFragment;
import com.ddm.live.constants.Constants;
import com.ddm.live.fragments.LiveListFragment;
import com.ddm.live.fragments.RefreshListEvent;
import com.ddm.live.fragments.RefreshUserCenterEvent;
import com.ddm.live.fragments.UserCenterFragment;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.versionbeans.Versionbean;
import com.ddm.live.models.bean.msgtype.LiveContentMessage;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;
import com.ddm.live.presenter.GetNewVersionInfoPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.utils.UpdateManager;
import com.ddm.live.views.entity.TabEntity;
import com.ddm.live.views.iface.IGetUpdateVersionView;
import com.ddm.live.views.utils.ViewFindUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.tag.TagManager;
import com.umeng.update.UmengUpdateAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JiGar on 2016/1/14.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, IGetUpdateVersionView {

    private AppComponent appComponent;

    private Context mContext = this;
    private int currentTab = 0;
    private String tabString = "";
    private ImageView userBtn;
    private TextView title;
    private RelativeLayout titleLayout;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

    //    private String[] mTitles = {"精选", "", "我的"};
    private String[] mTitles = {"", "", ""};
    private int[] mIconUnselectIds = {
            R.mipmap.home_list_icon, R.mipmap.home_live_icon, R.mipmap.home_user_center_icon};
    private int[] mIconSelectIds = {
            R.mipmap.home_list_icon_check, R.mipmap.home_live_icon_check, R.mipmap.home_user_center_icon_check};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private CommonTabLayout mTabLayout;
    private RongYunPresenter presenterRongYun;
    private PushAgent mPushAgent;
    private UpdateManager updateManager;
    //    public Toolbar toolbar;
    private int recordVisiableFragmentPosition;//当前显示fragment的position,用于界面呈现于数据刷新

    public ArrayList<Fragment> getmFragmentList() {
        return mFragmentList;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public CommonTabLayout getmTabLayout() {
        return mTabLayout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String connectionStatus = RongIMClient.getInstance().getCurrentConnectionStatus().name();
        /**
         * SDK打包时异常以下代码
         */
        if (appUserActivity.isLogined() && !connectionStatus.equals("CONNECTED")) {//进程里杀死app，融云即断开连接，但此时用户并未退出登录，因此重新启动app时，需重新连接融云
            initRongyun();
        }
        EventBus.getDefault().register(this);
        UILApplication.getInstance().addActivity(this);
        //检测版本更新
        UmengUpdateAgent.update(this);
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_font_black);
//        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.bottom_status_bar_color);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.xiaozhu_top_status_bar);
        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.xiaozhu_bottom_status_bar);
        setContentView(R.layout.activity_home_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        CrashReport.testJavaCrash();//测试：制造Crash检测Bugly

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.home_title_text);

//        title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, VideoPlayerActivity.class));
//            }
//        });

        titleLayout = (RelativeLayout) findViewById(R.id.home_page_title_layout);
        userBtn = (ImageView) findViewById(R.id.home_title_user_img);
        userBtn.setOnClickListener(this);

        mFragmentList.add(new LiveListFragment());
        mFragmentList.add(new EmptyFragment());
        mFragmentList.add(new UserCenterFragment());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        /** with Fragments */
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.tl);
        mTabLayout.setTabData(mTabEntities, this, R.id.fl_change, mFragmentList);

        mTabLayout.setCurrentTab(currentTab);
        tabString = tabString + currentTab;

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (tabString.length() > 3) {
                    tabString = tabString.substring(tabString.length() - 3, tabString.length());
                }
                if (position == 1) {
                    boolean isGranted = true;
                    currentTab = Integer.parseInt(tabString.substring(tabString.length() - 1, tabString.length() - 0));
                    mTabLayout.setCurrentTab(currentTab);

                    //检查是否开启相机录音权限
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    for (String str : permissions) {
                        if (ActivityCompat.checkSelfPermission(HomeActivity.this, str) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            isGranted = false;
                            ActivityCompat.requestPermissions(HomeActivity.this, permissions, Constants.REQUEST_CODE_CAMERA_AUDIO);
                            return;
                        }
                    }
                    if (isGranted) {
                        if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
                            Toast.makeText(HomeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("NETWORK_UNAVAILABLE")) {
                            Toast.makeText(HomeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                        } else if (!appUserActivity.isLogined() || RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("KICKED_OFFLINE_BY_OTHER_CLIENT")) {
                            appUserActivity.setIsLogined(false);
                            new SweetAlertDialog(HomeActivity.this)
                                    .setTitleText("请重新登录")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            startActivity(new Intent(HomeActivity.this, BFLoginActivity.class));
                                        }
                                    })
                                    .show();

                        } else {
                            startActivity(new Intent(HomeActivity.this, MasterStartLiveActivity.class));
                            overridePendingTransition(R.anim.activity_enter_from_bottom, 0);

                        }

                    }

                } else if (position == 2) {
//                    title.setText(mTitles[position]);
                    recordVisiableFragmentPosition = 2;
                    StatusBarUtils.setWindowStatusBarColor(HomeActivity.this, R.color.xz_font_purple);//设置状态栏颜色
                    titleLayout.setVisibility(View.GONE);
                    tabString = tabString + 2;
                    EventBus.getDefault().post(new RefreshUserCenterEvent());
//                    if(!ButtonUtils.isFastSelectedClick(2000)){
//
//                    }
                } else if (position == 0) {
//                    titleLayout.setVisibility(View.VISIBLE);
                    recordVisiableFragmentPosition = 0;
//                    StatusBarUtils.setWindowStatusBarColor(HomeActivity.this, R.color.le_font_black);
                    StatusBarUtils.setWindowStatusBarColor(HomeActivity.this, R.color.xiaozhu_top_status_bar);
                    tabString = tabString + 0;
                    EventBus.getDefault().post(new RefreshListEvent(1));
//                    if(!ButtonUtils.isFastSelectedClick(2000)){
//
//                    }
                }
            }

            @Override
            public void onTabReselect(int position) {
//                if(ButtonUtils.isFastSelectedClick(3000)){
//                 return;
//                }
                switch (position) {
                    case 0:
                        EventBus.getDefault().post(new RefreshListEvent(0));
                        break;
                    case 2:
                        EventBus.getDefault().post(new RefreshUserCenterEvent());
                        break;
                }
            }
        });

        /**
         * umeng推送
         */
        mPushAgent = PushAgent.getInstance(this);
//		mPushAgent.setPushCheck(true);    //默认不检查集成配置文件
//		mPushAgent.setLocalNotificationIntervalLimit(false);  //默认本地通知间隔最少是10分钟

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

//		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        //应用程序启动统计
        //参考集成文档的1.5.1.2
        //http://dev.umeng.com/push/android/integration#1_5_1
        mPushAgent.onAppStart();

        //开启推送并设置注册的回调处理
        mPushAgent.enable(mRegisterCallback);

        new Thread() {
            @Override
            public void run() {
                try {
                    TagManager.Result result = mPushAgent.getTagManager().add("test", "Test");
                    if (result.status.equals("ok")) {
                        // TODO 调关注接口
//                        Log.e("zz", mPushAgent.getTagManager().list().toString());
                    } else {
                        // TODO 弹出框交互
//                        Log.e("zz", "tag添加失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

/**
 * SDK打包时：注释掉版本检测更新代码
 */
        //A：版本检测更新
        GetNewVersionInfoPresenter presenter = new GetNewVersionInfoPresenter();
        presenter.attachView(this);
        updateManager = new UpdateManager(this);
        presenter.getNewVerwsionInfo();

    }

    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
//            Log.d("zz", "registrationId-----" + registrationId);
        }
    };

    //
    @Override
    protected void onResume() {
        super.onResume();
        switch (recordVisiableFragmentPosition) {
            case 0:
                EventBus.getDefault().post(new RefreshListEvent(1));
                break;

            case 2:
                EventBus.getDefault().post(new RefreshUserCenterEvent());
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        tabString = tabString + currentTab;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
//        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.home_title_user_img:
//                Intent intent = new Intent(HomeActivity.this, UserCenterHomeActivity.class);
//                startActivity(intent);
//                break;

        }
    }

    public void initRongyun() {

/**
 * SDK打包时：注释掉版连接融云代码
 */
        //连接融云
        AppComponent appComponent = AppApplication.get(this).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();
        presenterRongYun = presenterComponent.getRongYunPresenter();

        presenterRongYun.initConnection(appUserActivity, this)
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<RongIMClient>() {
                    @Override
                    public void call(RongIMClient rongIMClient) {
                        try {
                            rongIMClient.registerMessageType(LiveContentMessage.class);
                            rongIMClient.registerMessageType(LiveGiftMessage.class);
                            rongIMClient.registerMessageType(LiveStatusMessage.class);
                            rongIMClient.registerMessageType(LiveUserMessage.class);
                        } catch (AnnotationNotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        appUserActivity.setRongIMClient(rongIMClient);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /**
             * SDK打包时：注释掉双击退出函数代码
             */
            //调用双击退出函数
            exitBy2Click();

            //SDK打包时：打开提示代码
//            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("确定退出")
//                    .setCancelText("取消")
//                    .setConfirmText("退出")
//                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                            sDialog.dismiss();
//                        }
//                    })
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                            sDialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .show();
        }
        return false;
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

    @Override
    public void getNewVersionInfo(Versionbean versionbean) {
        updateManager.checkUpdate(versionbean);
    }

    /**
     * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
     */
    @Override
    public void updateNewVersion() {
        boolean isGranted = true;
//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_CONTACT = 100;
//            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
//            //验证是否许可权限
//            for (String str : permissions) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    //申请权限
//                    isGranted=false;
//                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
//                    return;
//                }
//            }
//        }

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        for (String str : permissions) {
            if (ActivityCompat.checkSelfPermission(this, str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                isGranted = false;
                ActivityCompat.requestPermissions(this, permissions, Constants.REQUEST_CODE_READ_WRITE);
                return;
            }
        }

        if (isGranted) {
            updateManager.search();
        }
        //ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isGranted = true;

        switch (requestCode) {

            case Constants.REQUEST_CODE_READ_WRITE:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                    }
                }
                if (isGranted) {
                    updateManager.search();
                }
                break;
            case Constants.REQUEST_CODE_CAMERA_AUDIO:
                for (int i = 0; i < permissions.length; i++) {
                    if ((permissions[i].equals(Manifest.permission.CAMERA) || permissions[i].equals(Manifest.permission.RECORD_AUDIO)) &&
                            grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                    }
                }
                if (isGranted) {
                    if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
                        Toast.makeText(HomeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("NETWORK_UNAVAILABLE")) {
                        Toast.makeText(HomeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                    } else if (!appUserActivity.isLogined() || RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("KICKED_OFFLINE_BY_OTHER_CLIENT")) {
                        appUserActivity.setIsLogined(false);
                        new SweetAlertDialog(HomeActivity.this)
                                .setTitleText("请重新登录")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        startActivity(new Intent(HomeActivity.this, BFLoginActivity.class));
                                    }
                                })
                                .show();

                    } else {
                        startActivity(new Intent(HomeActivity.this, MasterStartLiveActivity.class));
                    }
                }
                break;
        }

    }


   /*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }

        return_back super.onTouchEvent(event);
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(RefreshUserCenterEvent tmp) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }
}
