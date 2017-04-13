package com.ddm.live.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/14.
 * ava-正则表达式判断手机号
 */

public class CheckIsUserNumber {
    public static boolean isMobileNO(String mobiles){

        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
}
