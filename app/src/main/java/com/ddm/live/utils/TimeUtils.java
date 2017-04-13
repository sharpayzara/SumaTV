package com.ddm.live.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JiGar on 2016/11/24.
 * 时间转换相关方法
 */

public class TimeUtils {

    public TimeUtils() {
    }

    /**
     * 计算时间长度
     *
     * @param eTime 结束时间(时间戳)
     * @param sTime 开始时间(时间戳)
     * @return
     */
    public static String toTime(int eTime, int sTime) {
        String result = "";
        int videoTimeInt = eTime - sTime;
        long timeLong = videoTimeInt * 1000;
        long hours = timeLong / (1000 * 60 * 60);
        long minutes = (timeLong - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeLong - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        String hour;
        String minute;
        String second;
        if (hours < 10) {
            hour = "0" + hours;
        } else {
            hour = hours + "";
        }
        if (minutes < 10) {
            minute = "0" + minutes;
        } else {
            minute = minutes + "";
        }
        if (seconds < 10) {
            second = "0" + seconds;
        } else {
            second = seconds + "";
        }
        result = hour + ":" + minute + ":" + second;
        return result;
    }

    /**
     * 时间格式转换
     *
     * @param dataFormat 显示格式
     * @param timeStamp  时间戳
     * @return
     */
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }
}
