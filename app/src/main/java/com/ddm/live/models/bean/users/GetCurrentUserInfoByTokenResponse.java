package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.authorizationbeans.RongyunmetaBean;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class GetCurrentUserInfoByTokenResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("data")
    private UserInfoBean data;

    @Expose
    @SerializedName("meta")
    private RongyunmetaBean meta;

    public void setData(UserInfoBean data) {
        this.data = data;
    }

    public UserInfoBean getData() {
        return data;
    }

    public RongyunmetaBean getMeta() {
        return meta;
    }

    public void setMeta(RongyunmetaBean meta) {
        this.meta = meta;
    }
}