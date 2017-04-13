package com.ddm.live.models.bean.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ddm.live.utils.SharePreferenceHelper;

import io.rong.imlib.RongIMClient;

/**
 * Created by wsheng on 16/1/29.
 */
public class AppUser {

    private String TAG = "AppUser";

    SharePreferenceHelper preferenceHelper;

    //自定义的信息
    private boolean isLogined;
    private String sessionid;
    private String deviceID;
    private Integer id;
    private String rongYunToken;


    //第一步获取的信息
    private String expires_in;
    private String openid;
    private String refresh_token;
    private String access_token;
    private String unionid;

    //第二步获取的信息
    private String nickname;
    private int gender;//性别
    private String province;
    private String language;
    private String headimgurl;
    private String smallHeadimgurl;
    private String country;
    private String key;
    private String city;
    private RongIMClient rongIMClient;
    private String phoneNum;
    private Integer level;
    private int fansNum;
    private int focusNum;
    private int leNum;

    private String sign;

    private SharedPreferences preferences;

    public AppUser(Context context) {
        preferenceHelper = new SharePreferenceHelper(context);
        //如果有用户名则说明已经登录过了

        preferences = preferenceHelper.getPreferences();

        boolean isLogined = preferences.getBoolean("isLogined", false);

        if (isLogined) {
            this.isLogined = true;
            this.city = preferences.getString("city", "");
            this.country = preferences.getString("country", "");
            this.language = preferences.getString("language", "");
            this.unionid = preferences.getString("unionid", "");
            this.openid = preferences.getString("openid", "");
            this.key = preferences.getString("key", "");
            this.nickname = preferences.getString("nickname", "");
            this.sign = preferences.getString("sign", "");
            this.id = preferences.getInt("id", 0);
            this.gender = preferences.getInt("gender", 0);
            this.headimgurl = preferences.getString("headimgurl", "http://pili-static.9ddm.com/snapshots/z1.suma.Android_1454144160278/imageName.jpg");
            this.smallHeadimgurl = preferences.getString("smallHeadimgUrl", "");
            this.rongYunToken = preferences.getString("rongYunToken", "");
            this.access_token = preferences.getString("access_token", "");
            this.refresh_token = preferences.getString("refresh_token", "");
            this.expires_in = preferences.getString("expires_in", "");
            this.phoneNum = preferences.getString("phoneNum", "");
            this.level = preferences.getInt("level", 1);
            this.fansNum = preferences.getInt("fansNum", 0);
            this.focusNum = preferences.getInt("focusNum", 0);
            this.leNum = preferences.getInt("leNum", 0);


            Log.d(TAG, "AppUser: 该用户已经登录" + this.toString());
        } else {
            Log.d(TAG, "AppUser: 该用户还没有登录" + this.toString());
        }

    }

    public void loginUserInfo() {
        this.isLogined = true;
        SharedPreferences preferences = preferenceHelper.getPreferences();
        preferences.edit().clear().commit();
    }

    public void clearUserInfo() {
        this.isLogined = false;
        this.access_token = null;
        this.openid = null;
        this.refresh_token = null;
        this.access_token = null;
        this.unionid = null;
        this.smallHeadimgurl = null;
        this.gender = 0;
        this.province = null;
        this.language = null;
        this.headimgurl = null;
        this.country = null;
        this.key = null;
        this.rongYunToken = null;
        this.id = 0;
        this.nickname = null;
        this.sign = null;
        this.headimgurl = null;
        this.phoneNum = null;
        this.level = 1;
        this.fansNum = 0;
        this.focusNum = 0;
        this.leNum = 0;

        SharedPreferences preferences = preferenceHelper.getPreferences();
        preferences.edit().clear().commit();
        if (this.getRongIMClient() != null) {
            this.getRongIMClient().disconnect();
            this.getRongIMClient().logout();
//    RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus= this.getRongIMClient().getCurrentConnectionStatus();
        }
    }


    public String getRongYunToken() {
        return rongYunToken;
    }

    public void setRongYunToken(String rongYunToken) {
        this.rongYunToken = rongYunToken;
        preferenceHelper.set("rongYunToken", rongYunToken);

    }

    public Integer getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setId(Integer id) {
        this.id = id;
        preferenceHelper.set("id", id);
    }

    public void setCity(String city) {
        this.city = city;
        preferenceHelper.set("city", city);
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
        preferenceHelper.set("isLogined", isLogined);
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
        preferenceHelper.set("sessionid", sessionid);
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
        preferenceHelper.set("sessionid", sessionid);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
//        this.setIsLogined(true);
        preferenceHelper.set("nickname", nickname);
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
        preferenceHelper.set("sign", sign);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
        preferenceHelper.set("gender", gender);
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
        preferenceHelper.set("expires_in", expires_in);
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
        preferenceHelper.set("openid", openid);
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
        preferenceHelper.set("refresh_token", refresh_token);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
        preferenceHelper.set("access_token", access_token);
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
        preferenceHelper.set("unionid", unionid);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
        preferenceHelper.set("province", province);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        preferenceHelper.set("language", language);
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
        preferenceHelper.set("headimgurl", headimgurl);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        preferenceHelper.set("country", country);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        preferenceHelper.set("key", key);
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        preferenceHelper.set("phoneNum", phoneNum);
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
        preferenceHelper.set("level", level);
    }

    public RongIMClient getRongIMClient() {
        return rongIMClient;
    }

    public void setRongIMClient(RongIMClient rongIMClient) {
        this.rongIMClient = rongIMClient;
    }

    public String getSmallHeadimgurl() {
        return smallHeadimgurl;
    }

    public void setSmallHeadimgurl(String smallHeadimgurl) {
        this.smallHeadimgurl = smallHeadimgurl;
        preferenceHelper.set("smallHeadimgUrl", smallHeadimgurl);
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
        preferenceHelper.set("fansNum", fansNum);
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
        preferenceHelper.set("focusNum", focusNum);
    }

    public int getLeNum() {
        return leNum;
    }

    public void setLeNum(int leNum) {
        this.leNum = leNum;
        preferenceHelper.set("leNum", leNum);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "preferenceHelper=" + preferenceHelper +
                ", isLogined=" + isLogined +
                ", sessionid='" + sessionid + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", id='" + id + '\'' +
                ", rongYunToken='" + rongYunToken + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", openid='" + openid + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", access_token='" + access_token + '\'' +
                ", unionid='" + unionid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sign='" + sign + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", language='" + language + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", country='" + country + '\'' +
                ", key='" + key + '\'' +
                ", city='" + city + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", level='" + level + '\'' +
                ", fansNum='" + fansNum + '\'' +
                ", focusNum='" + focusNum + '\'' +
                ", leNum='" + leNum + '\'' +
                '}';
    }
}
