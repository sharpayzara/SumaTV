package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/15.
 */
public class GetSpecUserInfoRequest extends UsersBaseRequest{
    @Expose
    @SerializedName("ids")
    private Integer[] ids;
    public GetSpecUserInfoRequest(Integer[] ids) {
        setMethodName("getSpecUserInfo");
        this.ids = ids;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }
}
