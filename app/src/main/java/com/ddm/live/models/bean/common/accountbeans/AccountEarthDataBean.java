package com.ddm.live.models.bean.common.accountbeans;

import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.ddm.live.models.bean.common.commonbeans.UpdatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class AccountEarthDataBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("account_id")
    @Expose
    private Integer accountId;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("seqid")
    @Expose
    private Integer seqid;
    @SerializedName("created_at")
    @Expose
    private CreatedAtBean createdAt;
    @SerializedName("updated_at")
    @Expose
    private UpdatedAtBean updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getSeqid() {
        return seqid;
    }

    public void setSeqid(Integer seqid) {
        this.seqid = seqid;
    }

    public CreatedAtBean getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAtBean createdAt) {
        this.createdAt = createdAt;
    }

    public UpdatedAtBean getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(UpdatedAtBean updatedAt) {
        this.updatedAt = updatedAt;
    }
}
