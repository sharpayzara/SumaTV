package com.ddm.live.models.bean.common.userbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/19.
 */
public class SpecUserFansInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    @Expose
    private FansFocusBean data;

    public FansFocusBean getData() {
        return data;
    }

    public void setData(FansFocusBean data) {
        this.data = data;
    }
}
