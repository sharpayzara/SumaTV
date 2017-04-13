package com.ddm.live.models.bean.common.accountbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class AccountTypesDataBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("freeze")
    @Expose
    private Boolean freeze;
    @SerializedName("account_earns")
    @Expose
    private AccountEarthBean accountEarth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getFreeze() {
        return freeze;
    }

    public void setFreeze(Boolean freeze) {
        this.freeze = freeze;
    }

    public AccountEarthBean getAccountEarth() {
        return accountEarth;
    }

    public void setAccountEarth(AccountEarthBean accountEarth) {
        this.accountEarth = accountEarth;
    }
}
