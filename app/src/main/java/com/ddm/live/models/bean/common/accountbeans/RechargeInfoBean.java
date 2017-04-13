package com.ddm.live.models.bean.common.accountbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/25.
 */
public class RechargeInfoBean {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("freeze")
    @Expose
    private Integer freeze;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("account_id")
    @Expose
    private Integer accountId;
    @SerializedName("bill_id")
    @Expose
    private Integer billId;
    @SerializedName("amt")
    @Expose
    private String amt;
    @SerializedName("bill_info")
    @Expose
    private String billInfo;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("bill_type")
    @Expose
    private String billType;
    @SerializedName("bill_type_number")
    @Expose
    private String billTypeNumber;
    @SerializedName("bill_channel")
    @Expose
    private String billChannel;
    @SerializedName("bill_ret")
    @Expose
    private String billRet;
    @SerializedName("old_balance")
    @Expose
    private String oldBalance;
    @SerializedName("price_info")
    @Expose
    private String priceInfo;
    @SerializedName("src_ip")
    @Expose
    private String srcIp;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The type
     */
    public Integer getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The freeze
     */
    public Integer getFreeze() {
        return freeze;
    }

    /**
     *
     * @param freeze
     * The freeze
     */
    public void setFreeze(Integer freeze) {
        this.freeze = freeze;
    }

    /**
     *
     * @return
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     *
     * @param accountId
     * The account_id
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     *
     * @return
     * The billId
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     *
     * @param billId
     * The bill_id
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     *
     * @return
     * The amt
     */
    public String getAmt() {
        return amt;
    }

    /**
     *
     * @param amt
     * The amt
     */
    public void setAmt(String amt) {
        this.amt = amt;
    }

    /**
     *
     * @return
     * The billInfo
     */
    public String getBillInfo() {
        return billInfo;
    }

    /**
     *
     * @param billInfo
     * The bill_info
     */
    public void setBillInfo(String billInfo) {
        this.billInfo = billInfo;
    }

    /**
     *
     * @return
     * The operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     *
     * @param operator
     * The operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     *
     * @return
     * The billType
     */
    public String getBillType() {
        return billType;
    }

    /**
     *
     * @param billType
     * The bill_type
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     *
     * @return
     * The billTypeNumber
     */
    public String getBillTypeNumber() {
        return billTypeNumber;
    }

    /**
     *
     * @param billTypeNumber
     * The bill_type_number
     */
    public void setBillTypeNumber(String billTypeNumber) {
        this.billTypeNumber = billTypeNumber;
    }

    /**
     *
     * @return
     * The billChannel
     */
    public String getBillChannel() {
        return billChannel;
    }

    /**
     *
     * @param billChannel
     * The bill_channel
     */
    public void setBillChannel(String billChannel) {
        this.billChannel = billChannel;
    }

    /**
     *
     * @return
     * The billRet
     */
    public String getBillRet() {
        return billRet;
    }

    /**
     *
     * @param billRet
     * The bill_ret
     */
    public void setBillRet(String billRet) {
        this.billRet = billRet;
    }

    /**
     *
     * @return
     * The oldBalance
     */
    public String getOldBalance() {
        return oldBalance;
    }

    /**
     *
     * @param oldBalance
     * The old_balance
     */
    public void setOldBalance(String oldBalance) {
        this.oldBalance = oldBalance;
    }

    /**
     *
     * @return
     * The priceInfo
     */
    public String getPriceInfo() {
        return priceInfo;
    }

    /**
     *
     * @param priceInfo
     * The price_info
     */
    public void setPriceInfo(String priceInfo) {
        this.priceInfo = priceInfo;
    }

    /**
     *
     * @return
     * The srcIp
     */
    public String getSrcIp() {
        return srcIp;
    }

    /**
     *
     * @param srcIp
     * The src_ip
     */
    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

}