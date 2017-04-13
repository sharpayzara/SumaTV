package com.ddm.live.models.bean.common.userbeans;

import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/19.
 */
public class SpecUserInfoBean extends UserInfoBean implements Serializable{

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("fans")
    private SpecUserFansInfoBean fans;

    public SpecUserFansInfoBean getFans() {
        return fans;
    }

    public void setFans(SpecUserFansInfoBean fans) {
        this.fans = fans;
    }

}
