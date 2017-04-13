package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class ModifySpecUserNameRequest extends UsersBaseRequest {
    @Expose
    @SerializedName("name")
    private String name;

    public ModifySpecUserNameRequest(String name) {
        setMethodName("modifySpecUserName");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
