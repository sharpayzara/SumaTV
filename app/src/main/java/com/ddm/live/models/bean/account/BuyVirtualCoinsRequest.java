package com.ddm.live.models.bean.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class BuyVirtualCoinsRequest extends AccountBaseRequest {

    @SerializedName("money")
    @Expose
    private Integer money;

    public BuyVirtualCoinsRequest(Integer money) {
        this.money = money;
        setMethodName("buyVirtualCoins");
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

}
