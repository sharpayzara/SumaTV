package com.ddm.live.models.bean.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class TurnDiamondsToVirtualCoinsRequest extends AccountBaseRequest {
    @Expose
    @SerializedName("diamond")
    private Integer diamond;

    public TurnDiamondsToVirtualCoinsRequest(Integer diamond) {
        setMethodName("turnDiamondsToVirtualCoins");
        this.diamond = diamond;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }
}
