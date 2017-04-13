package com.ddm.live.models.bean.account;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cxx on 2016/8/11.
 */
public class GetRechargeListResponse implements ResponseBaseInterface {
    @SerializedName("data")
    @Expose
    private List<GetRechargeListResponseData> data;

    public List<GetRechargeListResponseData> getData() {
        return data;
    }

    public void setData(List<GetRechargeListResponseData> data) {
        this.data = data;
    }
}
