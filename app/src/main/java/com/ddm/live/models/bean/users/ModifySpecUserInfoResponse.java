package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 * 修改指定用户信息
 */
public class ModifySpecUserInfoResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("data")
    private UserInfoBean data;

    public UserInfoBean getData() {
        return data;
    }

    public void setData(UserInfoBean data) {
        this.data = data;
    }
}
