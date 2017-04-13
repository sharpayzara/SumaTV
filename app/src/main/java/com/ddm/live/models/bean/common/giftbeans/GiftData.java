package com.ddm.live.models.bean.common.giftbeans;

import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.ddm.live.models.bean.common.commonbeans.UpdatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/17.
 */


public class GiftData {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("mode")
    private String mode;

    @Expose
    @SerializedName("coins")
    private int coins;

    @Expose
    @SerializedName("to_credits")
    private String toCredits;

    @Expose
    @SerializedName("to_gold")
    private String toGold;

    @Expose
    @SerializedName("from_gold")
    private String fromGold;
    @Expose
    @SerializedName("to_exp")
    private String toExp;
    @Expose
    @SerializedName("onebyone")
    private String oneByOne;

    @Expose
    @SerializedName("pic_url")
    private String picUrl;

    @Expose
    @SerializedName("enabled")
    private int enabled;

    @Expose
    @SerializedName("min_send")
    private int minSend;

    @Expose
    @SerializedName("rank")
    private int rank;

    @Expose
    @SerializedName("info")
    private String info;

    @Expose
    @SerializedName("created_at")
    private CreatedAtBean createdAt;

    @Expose
    @SerializedName("updated_at")
    private UpdatedAtBean updatedAt;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public int getCoins() {
        return coins;
    }

    public String getToCredits() {
        return toCredits;
    }

    public String getToGold() {
        return toGold;
    }

    public String getFromGold() {
        return fromGold;
    }

    public String getOneByOne() {
        return oneByOne;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getEnabled() {
        return enabled;
    }

    public int getMinSend() {
        return minSend;
    }

    public int getRank() {
        return rank;
    }

    public String getInfo() {
        return info;
    }

    public CreatedAtBean getCreatedAt() {
        return createdAt;
    }

    public UpdatedAtBean getUpdatedAt() {
        return updatedAt;
    }

    public String getToExp() {
        return toExp;
    }
}