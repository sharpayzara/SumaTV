package com.ddm.live.models.bean.common.fansbeans;

import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class MasterFansBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("data")
    @Expose
    private UserInfoBean data;

    public UserInfoBean getData() {
        return data;
    }

    public void setData(UserInfoBean data) {
        this.data = data;
    }
}
