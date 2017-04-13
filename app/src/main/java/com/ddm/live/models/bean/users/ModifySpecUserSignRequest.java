package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class ModifySpecUserSignRequest extends UsersBaseRequest {
    @Expose
    @SerializedName("sign")
    private String sign;

    public ModifySpecUserSignRequest(String sign) {
        setMethodName("modifySpecUserSign");
        this.sign = sign;
    }

    public String getName() {
        return sign;
    }

    public void setName(String name) {
        this.sign = sign;
    }

}
