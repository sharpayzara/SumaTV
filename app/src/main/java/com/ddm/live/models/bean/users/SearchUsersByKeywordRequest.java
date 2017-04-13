package com.ddm.live.models.bean.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 * 通过关键字查找用户请求bean
 */
public class SearchUsersByKeywordRequest extends UsersBaseRequest{
    @Expose
    @SerializedName("keyword")
    private String keyword;

    public SearchUsersByKeywordRequest(String keyword) {
        setMethodName("searchUsersByKeyword");
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
