package com.ddm.live.models.bean.common.authorizationbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 * 持久化数据bean
 */
public class PersistentBean {

        @Expose
        @SerializedName("access_token")
        private String accessToken;

        @Expose
        @SerializedName("token_type")
        private String tokenType;

        @Expose
        @SerializedName("expires_in")
        private Integer expiresIn;

        @Expose
        @SerializedName("refresh_token")
        private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

