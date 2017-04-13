package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class ModifySpecUserInfoRequest extends UsersBaseRequest {
    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("sign")
    private String sign;

    public ModifySpecUserInfoRequest(String name, String sign) {
        setMethodName("modifySpecUserInfo");
        this.name = name;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
