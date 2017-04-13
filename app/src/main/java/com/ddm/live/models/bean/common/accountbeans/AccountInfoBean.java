package com.ddm.live.models.bean.common.accountbeans;

import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class AccountInfoBean extends UserInfoBean {
    @SerializedName("buy_coins")
    @Expose
    private String buyCoins;
    @SerializedName("have_coins")
    @Expose
    private String haveCoins;
    @SerializedName("account_types")
    @Expose
    private AccountTypesBean accountTypes;

    public String getBuyCoins() {
        return buyCoins;
    }

    public void setBuyCoins(String buyCoins) {
        this.buyCoins = buyCoins;
    }

    public String getHaveCoins() {
        return haveCoins;
    }

    public void setHaveCoins(String haveCoins) {
        this.haveCoins = haveCoins;
    }

    public AccountTypesBean getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(AccountTypesBean accountTypes) {
        this.accountTypes = accountTypes;
    }
}
