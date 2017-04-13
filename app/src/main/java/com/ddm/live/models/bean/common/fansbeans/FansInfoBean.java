package com.ddm.live.models.bean.common.fansbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JiGar on 2016/10/18.
 */
public class FansInfoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("data")
    private FansInfoDataBean data;

    public FansInfoDataBean getData() {
        return data;
    }

    public void setData(FansInfoDataBean data) {
        this.data = data;
    }
}
