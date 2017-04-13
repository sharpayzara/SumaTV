package com.ddm.live.models.bean.file;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.MetaBean;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cxx on 2016/8/10.
 */
public class QiniuUploadTokenResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("token")
    private String token;

    @Expose
    @SerializedName("host")
    private String host;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}