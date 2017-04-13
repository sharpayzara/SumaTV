package com.ddm.live.utils;

import android.content.res.Resources;

import com.ddm.live.AppApplication;

/**
 * Created by cxx on 2016/11/1.
 */
public class DrawableSettingUtils {
    /**
     * 根据图片的名称获取对应的资源id
     *
     * @param resourceName
     * @return
     */
    public static int getDrawResourceID(String resourceName) {
        Resources res = AppApplication.getInstance().getResources();
        int picid = res.getIdentifier(resourceName, "mipmap", AppApplication.getInstance().getPackageName());
        return picid;
    }
}
