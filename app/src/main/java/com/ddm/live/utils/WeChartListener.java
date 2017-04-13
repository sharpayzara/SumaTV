package com.ddm.live.utils;

import android.util.Log;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by wsheng on 16/1/29.
 */
public class WeChartListener {


    private String TAG = "WeChartListener";

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                Log.d(TAG, "key= " + entry.getKey());
                Log.d(TAG, "value= " + entry.getValue());
            }


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * delauth callback interface
     **/
    private UMAuthListener umdelAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "delete Authorize succeed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText(getApplicationContext(), "delete Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(getApplicationContext(), "delete Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

}
