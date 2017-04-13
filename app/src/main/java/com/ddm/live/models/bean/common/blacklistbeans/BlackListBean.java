package com.ddm.live.models.bean.common.blacklistbeans;

import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/11.
 */
public class BlackListBean {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("black_id")
    @Expose
    private Integer blackId;
    @SerializedName("user")
    @Expose
    private List<BlackUserBean> user = new ArrayList<BlackUserBean>();
    @SerializedName("created_at")
    @Expose
    private CreatedAtBean createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlackId() {
        return blackId;
    }

    public void setBlackId(Integer blackId) {
        this.blackId = blackId;
    }

    public List<BlackUserBean> getUser() {
        return user;
    }

    public void setUser(List<BlackUserBean> user) {
        this.user = user;
    }

    public CreatedAtBean getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAtBean createdAt) {
        this.createdAt = createdAt;
    }

}
