package com.ddm.live.models.bean.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class PresentVirtualCoinsRequest extends AccountBaseRequest {
    @SerializedName("coins")
    @Expose
    private Integer coins;
    @SerializedName("getter_id")
    @Expose
    private Integer getterId;

    public PresentVirtualCoinsRequest(Integer coins, Integer getterId) {
        setMethodName("presentVirtualCoins");
        this.coins = coins;
        this.getterId = getterId;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getGetterId() {
        return getterId;
    }

    public void setGetterId(Integer getterId) {
        this.getterId = getterId;
    }

}
