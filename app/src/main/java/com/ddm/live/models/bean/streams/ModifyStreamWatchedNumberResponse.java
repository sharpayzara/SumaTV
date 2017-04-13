package com.ddm.live.models.bean.streams;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class ModifyStreamWatchedNumberResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("result")
    private boolean result;

    @Expose
    @SerializedName("message")
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
