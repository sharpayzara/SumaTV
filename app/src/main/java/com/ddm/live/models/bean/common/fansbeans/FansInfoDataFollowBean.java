package com.ddm.live.models.bean.common.fansbeans;

import com.ddm.live.models.bean.common.userbeans.FansFocusBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JiGar on 2016/10/18.
 */
public class FansInfoDataFollowBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("data")
    private FansFocusBean data;

    public FansFocusBean getData() {
        return data;
    }

    public void setData(FansFocusBean data) {
        this.data = data;
    }
}
