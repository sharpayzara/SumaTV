package com.ddm.live.models.bean.gift;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.giftbeans.GiftData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cxx on 2016/8/10.
 */
public class GetGiftsListResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("data")
    private List<GiftData> data;

    public void setData(List<GiftData> data) {
        this.data = data;
    }

    public List<GiftData> getData() {
        return data;
    }

}