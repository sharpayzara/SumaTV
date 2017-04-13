package com.ddm.live.models.bean.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class LoginByWXRequest extends AuthorizationBaseRequest {

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
    @SerializedName("type")
    private int type;

    @Expose
    @SerializedName("openid")
    private String openid;

    @Expose
    @SerializedName("unionid")
    private String unionid;

    @Expose
    @SerializedName("avatar")
    private String avatar;

    @Expose
    @SerializedName("province")
    private String province;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("country")
    private String country;

    @Expose
    @SerializedName("gender")
    private int gender;

    @Expose
    @SerializedName("access_token")
    private String accessToken;

    @Expose
    @SerializedName("refresh_token")
    private String refreshToken;

    @Expose
    @SerializedName("nick_name")
    private String nickName;

    public LoginByWXRequest(String grantType, String clientId, String clientSecret, int type,
                            String openid, String unionid, String avatar, String province,
                            String city, String country, int gender, String accessToken,
                            String refreshToken, String nickName) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.type = type;
        this.openid = openid;
        this.unionid = unionid;
        this.avatar = avatar;
        this.province = province;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickName = nickName;
        setMethodName("login");
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}