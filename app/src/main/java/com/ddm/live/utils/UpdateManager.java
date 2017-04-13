package com.ddm.live.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.activities.HomeActivity;
import com.ddm.live.fragments.UpdataVersionDialogFragment;
import com.ddm.live.models.bean.common.versionbeans.Versionbean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cxx on 2016/10/20.
 * 版本检测更新工具类
 */
public class UpdateManager {

    //服务器端最新版本信息
    private String mVersionCode;//软件升级标示号
    private String mVersionName = "小主直播.apk";//apk名
    private String mVersionDesc;//更新详情
    private String mVersionPath = "http://124.193.230.157/imtt.dd.qq.com/16891/D4AF7B0C3FC82AD1D97A5671B660D4ED.apk?mkey=5807781416e3ec89&f=d688&c=0&fsname=com.ddm.live_1.0.1_106.apk&hsr=4d5s&p=.apk";//apk下载地址

    //本地存储的版本信息（如果有的话）
    private String lVersionCode;//软件升级标示号
    private String lVersionName = "小主直播.apk";//apk名
    private String lVersionDesc;//更新详情
    private String lVersionPath;//apk下载地址
    private String lVersionPackageName;//包名

    //用户当前安装版本信息
    private String cVersionCode;//软件升级标示号
    private String cVersionName = "小主直播.apk";//apk名
    private String cVersionDesc;//更新详情
    private String cVersionPath;//apk下载地址

    private Versionbean mVersionBean;//服务器端最新版本信息
    private Versionbean lVersionBean;//本地存储的版本信息（如果有的话）
    private Versionbean cVersionBean;//用户当前安装版本信息
    private Context mContext;//上下文
    private ProgressBar mProgressBar;//进度条
    private TextView tv1;
    private TextView tv2;
    private Dialog mDownloadDialog;//对话框
    private String mSavePath;//apk保存地址
    private int mProgress;//进度值
    private boolean mIsCancel = false;//是否取消下载标示符
    private static final int DOWNLOADING = 1;//apk下载中
    private static final int DOWNLOAD_FINISH = 2;//apk下载完毕
    private float length;
    private static final String PATH = "http://www.jcpeixun.com/app_client_api/app_version.aspx";//更新地址
    private File apkFile;
    private UpdataVersionDialogFragment updataVersionDialogFragment;
    private File dir;

    public UpdateManager(Context context) {
        mContext = context;
    }


    /**
     * 检查版本是否需要更新
     */
    public void checkUpdate(Versionbean versionbean) {
        mVersionBean = versionbean;
        getCurrentVersionInfo();//获取当前安装的版本信息
        //最新版本与用户版本比较
        if (isUpdate(mVersionBean.getVersionCode(), cVersionBean.getVersionCode())) {//当前安装的不是最新版本
            Bundle bundle = new Bundle();
            bundle.putString("version", mVersionBean.getVersionName());
            bundle.putString("updateTime", "2016/10/20");
            bundle.putString("updateContent", mVersionBean.getVersionDesc());
            bundle.putString("mustUpdate", mVersionBean.getMustDownload());
            updataVersionDialogFragment = new UpdataVersionDialogFragment();
            updataVersionDialogFragment.setArguments(bundle);
            updataVersionDialogFragment.attachFramgent((HomeActivity) mContext);
            updataVersionDialogFragment.show(((HomeActivity) mContext).getSupportFragmentManager(), "updataVersion");
        } else {//当前安装的已是最新版本

        }
    }

    /**
     * 获取用户当前版本信息
     */

    public void getCurrentVersionInfo() {
        String appName = "";
        String packageName = "";
        String versionName = "";
        int versionCode = 0;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            int labelRes = info.applicationInfo.labelRes;
            appName = mContext.getResources().getString(labelRes);
            versionName = info.versionName;
            versionCode = info.versionCode;
            cVersionBean = new Versionbean(appName, versionCode, versionName, "", "", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            cVersionCode = null;
        }
    }

    public static String getCurrentVersionInfo(Context context) {
        String versionName = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            int labelRes = info.applicationInfo.labelRes;
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 从服务器拉取最新版本信息
     */
    //@Override
    public void getNewVersionInfo(Versionbean mVersionBean) {
        this.mVersionBean = mVersionBean;

        //最新版本与用户版本比较
        if (isUpdate(mVersionBean.getVersionCode(), cVersionBean.getVersionCode())) {//当前安装的不是最新版本
//            new UpdataVersionDialogFragment().show(((HomeActivity) mContext).getSupportFragmentManager(), "updataVersion");
            //查询本地存储中是否有最新安装包
            search();
        } else {//当前安装的已是最新版本

        }
    }

    public boolean isUpdate(int versionCode1, int versionCode2) {

        if (versionCode1 > versionCode2) {
            return true;
        }
        return false;
    }

    private Handler mGetLoacalVersionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://本地没有可供安装的最新版本安装包,执行远程下载并安装
                    showDownloadDialog();
                    break;
                case 1://安装本地
                    File apkFile = new File(lVersionBean.getVersionPath());
                    installAPK(apkFile);
                    break;
            }
        }
    };

    // 搜索本地是否有安装包
    public void search() {
        new Thread() {
            @Override
            public void run() {
                updataVersionDialogFragment.dismiss();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String sdPath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdPath + "MyDownload3";
                    dir = new File(mSavePath);
                    if (!dir.exists())
                        dir.mkdir();
                }


                if (fileIsExist(dir)) {
                    mGetLoacalVersionHandler.sendEmptyMessage(1);//本地有可供安装的最新版本安装包
                } else {
                    mGetLoacalVersionHandler.sendEmptyMessage(0);//本地没有可供安装的最新版本安装包
                }
            }
        }.start();
    }


    public boolean fileIsExist(File fileName) {
//        System.out.println("在哪:" + fileName);
        File[] files = fileName.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    fileIsExist(file);
                } else {
                    if (file.getName().endsWith(".apk")) {
                        lVersionBean = getApkInfo(file.getAbsolutePath());

//                        /**
//                         * 测试代码
//                         */
//                        return false;
////                        if(lVersionBean.getAppName().equals("小主直播")){
////                            return true;
////                        }

                        if (lVersionBean.getAppName().equals(mVersionBean.getAppName())) {
                            if (!isUpdate(mVersionBean.getVersionCode(), lVersionBean.getVersionCode())) {//本地存储的不是最新版本
                                return true;
                            }
                        }

                    }
                }
            }
        }
        return false;
    }

    //获取APK信息
    public Versionbean getApkInfo(String path) {
        String appName = "";
        String packageName = "";
        String versionName = "";
        int versionCode = 0;
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appName = pm.getApplicationLabel(appInfo).toString();
            packageName = appInfo.packageName;  //得到安装包名称
            versionName = info.versionName;       //得到版本信息
            versionCode = info.versionCode;
        }
        return new Versionbean(appName, versionCode, versionName, "", path, "");
    }

    /*
* 安装本地安装包
*/
    protected void installAPK(File apkFile) {
        PackageManager packageManager = mContext.getPackageManager();
        if (!apkFile.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//确保最后安装好了后，点击打开的是新版应用
        Uri uri = Uri.parse("file://" + apkFile.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//安装后提示完成打开
    }

    /*
 * 显示正在下载对话框
 */
    protected void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("下载中");
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.id_progress);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        builder.setView(view);

        // 设置按钮(左)
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                // 设置下载状态为取消
                mIsCancel = true;
            }
        });
        // 设置按钮(右)
        builder.setNeutralButton("隐藏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.show();

        // 下载文件
        downloadAPK();
    }

    /*
   * 开启新线程下载文件
   */
    private void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {//检查sd是否挂载
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        // 下载文件
                        HttpURLConnection conn = (HttpURLConnection) new URL(mVersionBean.getVersionPath()).openConnection();
//                        HttpURLConnection conn = (HttpURLConnection) new URL(mVersionPath).openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        length = conn.getContentLength();
                        apkFile = new File(mSavePath, mVersionBean.getAppName() + mVersionBean.getVersionName() + ".apk");
                        if (!apkFile.exists()) {
                            apkFile.createNewFile();
                        }
//                        apkFile = new File(mSavePath, mVersionName);//测试代码
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while (!mIsCancel) {
                            int numread = is.read(buffer);
                            count += numread;
                            // 计算进度条的当前位置
                            mProgress = (int) ((count / length) * 100);
                            // 更新进度条
                            mUpdateProgressHandler.sendEmptyMessage(DOWNLOADING);

                            // 下载完成
                            if (numread < 0) {
                                mUpdateProgressHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                            fos.write(buffer, 0, numread);
                            fos.flush();
                        }

                        fos.close();
                        is.close();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mUpdateProgressHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING:
                    // 设置进度条
                    mProgressBar.setProgress(mProgress);
                    tv1.setText(mProgress + "%");
                    float len = length / 1024 / 1024;//将length转换为M单位
                    float b = (float) (Math.round(len * 100)) / 100;//保留两位小数点
                    tv2.setText(b + "M");
                    break;
                case DOWNLOAD_FINISH:
                    // 隐藏当前下载对话框
                    mDownloadDialog.dismiss();
                    if (!apkFile.exists())
                        return;
                    installAPK(apkFile);
                    break;
                default:
                    break;
            }
        }
    };

}
