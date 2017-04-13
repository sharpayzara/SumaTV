package com.ddm.live.models.bean.rechargeinfo;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.basebean.response.BaseResponse;
import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.ddm.live.models.bean.common.commonbeans.UpdatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cxx on 2016/8/7.
 */
public class GetRechargeInfoListResponse extends BaseResponse implements ResponseBaseInterface {
        @Expose
        @SerializedName("data")
        private List<RechargeInfoData> data;
        public void setRechargeInfoData(List<RechargeInfoData> data) {
            this.data = data;
        }
        public List<RechargeInfoData> getData() {
            return data;
        }

    public class RechargeInfoData {
        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("money")
        private String money;

        @Expose
        @SerializedName("coins")
        private int coins;

        @Expose
        @SerializedName("rank")
        private int rank;

        @Expose
        @SerializedName("enabled")
        private int enabled;

        @Expose
        @SerializedName("remark")
        private String remark;

        @Expose
        @SerializedName("created_at")
        private CreatedAtBean createdAt;

        @Expose
        @SerializedName("updated_at")
        private UpdatedAtBean updatedAt;

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setMoney(String money) {
            this.money = money;
        }
        public String getMoney() {
            return money;
        }

        public void setCoins(int coins) {
            this.coins = coins;
        }
        public int getCoins() {
            return coins;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
        public int getRank() {
            return rank;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }
        public int getEnabled() {
            return enabled;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
        public String getRemark() {
            return remark;
        }

        public void setCreatedAt(CreatedAtBean createdAt) {
            this.createdAt = createdAt;
        }
        public CreatedAtBean getCreatedAt() {
            return createdAt;
        }

        public void setUpdatedAt(UpdatedAtBean updatedAt) {
            this.updatedAt = updatedAt;
        }
        public UpdatedAtBean getUpdatedAt() {
            return updatedAt;
        }

    }

}
