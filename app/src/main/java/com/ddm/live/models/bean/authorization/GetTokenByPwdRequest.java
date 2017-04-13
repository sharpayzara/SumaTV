package com.ddm.live.models.bean.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class GetTokenByPwdRequest extends AuthorizationBaseRequest {
    @Expose
    @SerializedName("grant_type")
    private String grantType;

    @Expose
    @SerializedName("client_id")
    private String clientId;

    @SerializedName("links.pre_page")
    private String prePage;

    @SerializedName("links.next_page")
    private String nextPage;

    @Expose
    @SerializedName("client_secret")
    private String clientSecret;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    @SerializedName("password")
    private String password;

    public GetTokenByPwdRequest(String grantType, String clientId, String clientSecret, String phoneNumber, String password) {
        setMethodName("getTokenByPassword");
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}