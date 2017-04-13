package com.ddm.live.utils;

/**
 * Created by zhangjx on 2017/2/22.
 */
public class DateUtils {

    public static String formatTime(int second) {
        if (second < 0) {
            second = 0;
        }
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }
}
