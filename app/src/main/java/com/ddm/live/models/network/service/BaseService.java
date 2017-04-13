package com.ddm.live.models.network.service;

import com.google.gson.Gson;

/**
 * Created by cxx on 2016/8/12.
 */
public class BaseService {

    public static String TAG = "cxx";
    public static Gson gson = new Gson();
    public static void print(String str){
//        System.out.println(str);
//        Log.d(TAG,str);
    }
}
