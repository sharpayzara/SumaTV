package com.ddm.live.models.bean.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class RefreshTokenRequest extends AuthorizationBaseRequest {
    @Expose
    @SerializedName("grant_type")
    private String grantType;

    @Expose
    @SerializedName("client_id")
    private String clientId;

    @Expose
    @SerializedName("client_secret")
    private String clientSecret;

    @Expose
    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest(String grantType, String clientId, String clientSecret, String refreshToken) {
        setMethodName("refreshToken");
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
