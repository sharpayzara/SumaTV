package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/10/28.
 * 随机获取一批用户测试
 */
public class GetRandomUsersResponse implements ResponseBaseInterface {
    @SerializedName("data")
    @Expose
    private List<UserInfoBean> data = new ArrayList<UserInfoBean>();

    public List<UserInfoBean> getData() {
        return data;
    }

    public void setData(List<UserInfoBean> data) {
        this.data = data;
    }

}