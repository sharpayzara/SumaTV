package com.ddm.live.models.bean.common.commonbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/11/3.
 */
public class UserInfoBaseBean {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("user_group_id")
    private int userGroupId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    @SerializedName("is_password")
    private int isPassword;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("gender")
    private int gender;

    @Expose
    @SerializedName("province")
    private String province;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("sign")
    private String sign;

    @Expose
    @SerializedName("birthday")
    private String birthday;

    @Expose
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("avatar_small")
    @Expose
    private String avatarSmall;
    @Expose
    @SerializedName("user_number")
    private int userNumber;

    @Expose
    @SerializedName("watched_number")
    private int watchedNumber;

    @Expose
    @SerializedName("fans_number")
    private int fansNumber;

    @Expose
    @SerializedName("favours_number")
    private int favoursNumber;

    @Expose
    @SerializedName("experience_number")
    private int experienceNumber;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("level")
    private Integer level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsPassword() {
        return isPassword;
    }

    public void setIsPassword(int isPassword) {
        this.isPassword = isPassword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarSmall() {
        return avatarSmall;
    }

    public void setAvatarSmall(String avatarSmall) {
        this.avatarSmall = avatarSmall;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getWatchedNumber() {
        return watchedNumber;
    }

    public void setWatchedNumber(int watchedNumber) {
        this.watchedNumber = watchedNumber;
    }

    public int getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(int fansNumber) {
        this.fansNumber = fansNumber;
    }

    public int getFavoursNumber() {
        return favoursNumber;
    }

    public void setFavoursNumber(int favoursNumber) {
        this.favoursNumber = favoursNumber;
    }

    public int getExperienceNumber() {
        return experienceNumber;
    }

    public void setExperienceNumber(int experienceNumber) {
        this.experienceNumber = experienceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
