package com.ddm.live.models.bean.common.commonbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JiGar on 2016/10/27.
 */
public class ErrorBean {
    @Expose
    @SerializedName("name")
    String[] name;

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }
}
