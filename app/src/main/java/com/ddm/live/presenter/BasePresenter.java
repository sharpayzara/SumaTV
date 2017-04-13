package com.ddm.live.presenter;

import com.google.gson.Gson;

/**
 * Created by wsheng on 16/2/2.
 */
public class BasePresenter {
    protected String TAG = this.getClass().getName();
    public static Gson gson = new Gson();
    public static void print(String str) {
//        System.out.println(str);
    }
}