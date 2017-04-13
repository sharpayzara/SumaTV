package com.ddm.live.models.bean.common.accountbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/11.
 */
public class AccountTypesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    @Expose
    private List<AccountTypesDataBean> data = new ArrayList<AccountTypesDataBean>();

    public List<AccountTypesDataBean> getData() {
        return data;
    }

    public void setData(List<AccountTypesDataBean> data) {
        this.data = data;
    }
}
