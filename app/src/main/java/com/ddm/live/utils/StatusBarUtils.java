package com.ddm.live.utils;

/**
 * Created by Administrator on 2016/11/23.
 */

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏相关工具类
 */
public class StatusBarUtils {

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowBottomStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//版本不低于5.0  api21
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarTranslucent(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4.2 api19
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}