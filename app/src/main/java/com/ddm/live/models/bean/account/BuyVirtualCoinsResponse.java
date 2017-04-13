package com.ddm.live.models.bean.account;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class BuyVirtualCoinsResponse  implements ResponseBaseInterface{

    @Expose
    @SerializedName("appid")
    private String appid;

    @Expose
    @SerializedName("partnerid")
    private String partnerid;

    @Expose
    @SerializedName("prepayid")
    private String prepayId;

    @Expose
    @SerializedName("noncestr")
    private String nonceStr;

    @Expose
    @SerializedName("package")
    private String package2;

    @Expose
    @SerializedName("sign")
    private String sign;

    @Expose
    @SerializedName("timestamp")
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackage2() {
        return package2;
    }

    public void setPackage2(String package2) {
        this.package2 = package2;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
