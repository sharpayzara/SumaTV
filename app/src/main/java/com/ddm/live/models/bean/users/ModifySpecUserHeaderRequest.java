package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class ModifySpecUserHeaderRequest extends UsersBaseRequest {
    @Expose
    @SerializedName("avatar")
    private String avatar;

    public ModifySpecUserHeaderRequest(String avatar) {
        setMethodName("modifySpecUserHeader");
        this.avatar = avatar;
    }

    public String getName() {
        return avatar;
    }

    public void setName(String name) {
        this.avatar = avatar;
    }

}
