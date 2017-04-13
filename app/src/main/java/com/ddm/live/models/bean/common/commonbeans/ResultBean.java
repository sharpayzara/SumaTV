package com.ddm.live.models.bean.common.commonbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 * 验证结果返回bean
 */
public class ResultBean {
    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("result")
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
