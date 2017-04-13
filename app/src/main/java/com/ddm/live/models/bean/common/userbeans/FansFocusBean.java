package com.ddm.live.models.bean.common.userbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/29.
 */
public class FansFocusBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @SerializedName("fans_count")
    @Expose
    private Integer fansCount;
    @SerializedName("masters_count")
    @Expose
    private Integer mastersCount;
    @SerializedName("is_followed")
    @Expose
    private Boolean isFollowed;

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getMastersCount() {
        return mastersCount;
    }

    public void setMastersCount(Integer mastersCount) {
        this.mastersCount = mastersCount;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

}
