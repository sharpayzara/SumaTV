package com.ddm.live.models.bean.version;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.versionbeans.Versionbean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/10/20.
 */
public class UpdateVersionResponse implements ResponseBaseInterface{
    @Expose
    @SerializedName("data")
    private Versionbean versionbean;

    public Versionbean getVersionbean() {
        return versionbean;
    }
}
