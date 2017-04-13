package com.ddm.live.models.bean.blacklist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class AddUserIntoBlackListByIDRequest extends BlackListBaseRequest {
    @Expose
    @SerializedName("black_id")
    private String blackId;

    public AddUserIntoBlackListByIDRequest(String blackId) {
        setMethodName("addUserIntoBlacklistByID");
        this.blackId = blackId;
    }

    public String getBlackId() {
        return blackId;
    }

    public void setBlackId(String blackId) {
        this.blackId = blackId;
    }
}
