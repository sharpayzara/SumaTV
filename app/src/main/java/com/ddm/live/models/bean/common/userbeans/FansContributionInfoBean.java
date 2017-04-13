package com.ddm.live.models.bean.common.userbeans;

import com.ddm.live.models.bean.common.commonbeans.UserInfoBaseBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/11/3.
 */
public class FansContributionInfoBean extends UserInfoBaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("total_coins")
    private String totalCoins;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("update_at")
    private String updateAt;
    //排名类型，0表示前三，1表示普通
    private int type = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        this.totalCoins = totalCoins;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }


}
