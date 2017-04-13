package com.ddm.live.models.bean.streams;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class CreateStreamsResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("data")
    private SpecStreamBean data;

    public void setData(SpecStreamBean data) {
        this.data = data;
    }

    public SpecStreamBean getData() {
        return data;
    }

}