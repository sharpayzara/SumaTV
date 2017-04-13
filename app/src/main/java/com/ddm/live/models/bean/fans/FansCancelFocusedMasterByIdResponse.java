package com.ddm.live.models.bean.fans;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class FansCancelFocusedMasterByIdResponse implements ResponseBaseInterface {
    @SerializedName("result")
    @Expose
    private Integer result;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

}