package com.ddm.live.models.bean.fans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class FansFocusOnMasterByIDRequest extends  FansBaseRequest{
    @Expose
    @SerializedName("master_ids")
    private Integer[] masterIds;

    public FansFocusOnMasterByIDRequest(Integer[] masterIds) {
        setMethodName("focusOnMasterByID");
        this.masterIds = masterIds;
    }

    public Integer[] getMasterIds() {
        return masterIds;
    }

    public void setMasterIds(Integer[] masterIds) {
        this.masterIds = masterIds;
    }
}
