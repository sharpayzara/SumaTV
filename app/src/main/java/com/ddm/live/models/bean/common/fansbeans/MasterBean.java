package com.ddm.live.models.bean.common.fansbeans;

import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class MasterBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("master_id")
    @Expose
    private Integer masterId;
    @SerializedName("fans_id")
    @Expose
    private Integer fansId;
    @SerializedName("created_at")
    @Expose
    private CreatedAtBean createdAt;
    @SerializedName("master")
    @Expose
    private MasterFansBean master;

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

    public MasterFansBean getMaster() {
        return master;
    }

    public void setMaster(MasterFansBean master) {
        this.master = master;
    }
}
