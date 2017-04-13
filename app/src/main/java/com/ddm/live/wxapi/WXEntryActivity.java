package com.ddm.live.wxapi;

import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends WXCallbackActivity {

    public WXEntryActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        }catch (Exception e){
        }finally {
            finish();

        }
    }

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
//        Log.e("ww", resp.toString());
    }

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }
}
