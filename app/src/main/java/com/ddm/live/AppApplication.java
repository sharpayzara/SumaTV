package com.ddm.live;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerAppComponent;
import com.ddm.live.inject.modules.AppModule;
import com.ddm.live.models.bean.msgtype.LiveContentMessage;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserListMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.utils.RongYunListenerContext;
import com.ddm.live.utils.SharePreferenceHelper;
import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;

/**
 * Created by wsheng on 16/1/12.
 */
public class AppApplication extends Application {

    private static AppApplication appApplication;

    public static AppApplication getInstance() {
        return appApplication;
    }

    private static SharePreferenceHelper sharePreferenceHelper;


    public static int total = 0;

    public String TAG = this.getClass().getName();

    private AppComponent appComponent;

    private AppUser appUser;

    private PushAgent mPushAgent;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        total++;
        //设置
        //七牛SDK初始化
        StreamingEnv.init(getApplicationContext());
//        Log.d(TAG, "Application 初始化次数为:" + total);
        /**
         * 初始化Bugly
         * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
         ● 输出详细的Bugly SDK的Log；
         ● 每一条Crash都会被立即上报；
         ● 自定义日志将会在Logcat中输出。
         建议在测试阶段建议设置成true，发布时设置为false。
         */
        CrashReport.initCrashReport(getApplicationContext(), "900055329", false);
//        LeakCanary.install(this);
        //启用用户程序时判断用户是否已经登录

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(this);

        appUser = new AppUser(this);

        //判断版本号,如果文件存储的是旧版本号,则把内容登录内容清空


//        Log.d(TAG, "appUser: " + appUser.toString());

        /*if (sharePreferenceHelper.checkIsLogined()){
            //如果已经登录过了
            appUser.setIsLogined(true);
//            appUser.setUsername(sharePreferenceHelper.getUserName());
        }else{
            //如果没有登录
            appUser.setIsLogined(false);
        }*/


        /**
         *  IMLib SDK调用第一步 初始化
         * context上下文
         */
//        RongIMClient.init(this);

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
            registerMessageType();
        }

        RongYunListenerContext.getInstance().init(this);
        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                if(connectionStatus.name().equals("KICKED_OFFLINE_BY_OTHER_CLIENT")){
                    Log.e("cxx","您的账号已在另一台设备上登录");
                }
            }
        });
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        PlatformConfig.setWeixin("wx81ec8ff55666bb9b", "f25b93b820f6372155e5464998a4c9eb");

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setContentTitle(msg.title)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setContentText(msg.text)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static AppApplication get(Context context) {
        return (AppApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static SharePreferenceHelper getSharePreferenceHelper() {
        sharePreferenceHelper = new SharePreferenceHelper(AppApplication.getInstance());
        return sharePreferenceHelper;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void registerMessageType() {
        try {
            RongIMClient.registerMessageType(LiveContentMessage.class);
            RongIMClient.registerMessageType(LiveGiftMessage.class);
            RongIMClient.registerMessageType(LiveStatusMessage.class);
            RongIMClient.registerMessageType(LiveUserMessage.class);
            RongIMClient.registerMessageType(LiveUserListMessage.class);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
