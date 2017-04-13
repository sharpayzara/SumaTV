package com.ddm.live.models.bean.common.fansbeans;

import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class FansBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("master_id")
    @Expose
    private Integer masterId;
    @SerializedName("fans_id")
    @Expose
    private Integer fansId;
    @SerializedName("created_at")
    @Expose
    private CreatedAtBean createdAt;
    @SerializedName("fans")
    @Expose
    private FansInfoBean fans;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getFansId() {
        return fansId;
    }

    public void setFansId(Integer fansId) {
        this.fansId = fansId;
    }

    public CreatedAtBean getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAtBean createdAt) {
        this.createdAt = createdAt;
    }

    public FansInfoBean getFans() {
        return fans;
    }

    public void setFans(FansInfoBean fans) {
        this.fans = fans;
    }
}
