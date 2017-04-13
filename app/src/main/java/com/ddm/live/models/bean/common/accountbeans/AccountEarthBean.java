package com.ddm.live.models.bean.common.accountbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class AccountEarthBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    @Expose
    private AccountEarthDataBean data;

    public AccountEarthDataBean getData() {
        return data;
    }

    public void setData(AccountEarthDataBean data) {
        this.data = data;
    }
}
