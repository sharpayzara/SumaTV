package com.ddm.live.models.bean.common.fansbeans;

import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JiGar on 2016/10/18.
 */
public class FansInfoDataBean extends UserInfoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("fans")
    private FansInfoDataFollowBean fans;

    public FansInfoDataFollowBean getFans() {
        return fans;
    }

    public void setFans(FansInfoDataFollowBean fans) {
        this.fans = fans;
    }
}
