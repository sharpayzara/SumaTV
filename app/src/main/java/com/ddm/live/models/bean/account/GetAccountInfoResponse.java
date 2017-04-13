package com.ddm.live.models.bean.account;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.accountbeans.AccountInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class GetAccountInfoResponse implements ResponseBaseInterface {
    @SerializedName("data")
    @Expose
    private AccountInfoBean data;

    public AccountInfoBean getData() {
        return data;
    }

    void setData(AccountInfoBean data) {
        this.data = data;
    }

}
