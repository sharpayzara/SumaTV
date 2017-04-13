package com.ddm.live.models.bean.account;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class GetRechargeListResponseData implements ResponseBaseInterface {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("money")
    @Expose
    private String money;

    @SerializedName("diamonds")
    @Expose
    private int diamonds;

    @SerializedName("rank")
    @Expose
    private int rank;

    @SerializedName("enabled")
    @Expose
    private int enabled;

    @SerializedName("remark")
    @Expose
    private String remark;

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int isEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
