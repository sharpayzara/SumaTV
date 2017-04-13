package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class ModifySpecUserGenderRequest extends UsersBaseRequest {
    @Expose
    @SerializedName("gender")
    private int gender;

    public ModifySpecUserGenderRequest(int gender) {
        setMethodName("modifySpecUserGender");
        this.gender = gender;
    }

    public int getName() {
        return gender;
    }

    public void setName(int name) {
        this.gender = gender;
    }

}
