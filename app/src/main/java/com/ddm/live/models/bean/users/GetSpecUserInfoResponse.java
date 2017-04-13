package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/10.
 */
//获取指定用户信息
public class GetSpecUserInfoResponse implements ResponseBaseInterface{
    @SerializedName("data")
    @Expose
    private List<SpecUserInfoBean> data = new ArrayList<SpecUserInfoBean>();

    public List<SpecUserInfoBean> getData() {
        return data;
    }

    public void setData(List<SpecUserInfoBean> data) {
        this.data = data;
    }

}