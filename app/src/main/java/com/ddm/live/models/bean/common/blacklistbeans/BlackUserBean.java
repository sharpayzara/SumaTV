package com.ddm.live.models.bean.common.blacklistbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class BlackUserBean  {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_group_id")
    @Expose
    private Integer userGroupId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("is_password")
    @Expose
    private Integer isPassword;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("sign")
    @Expose
    private String sign;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("user_number")
    @Expose
    private Integer userNumber;
    @SerializedName("watched_number")
    @Expose
    private Integer watchedNumber;
    @SerializedName("fans_number")
    @Expose
    private Integer fansNumber;
    @SerializedName("favours_number")
    @Expose
    private Integer favoursNumber;
    @SerializedName("experience_number")
    @Expose
    private Integer experienceNumber;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

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
     * The userGroupId
     */
    public Integer getUserGroupId() {
        return userGroupId;
    }

    /**
     *
     * @param userGroupId
     * The user_group_id
     */
    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     * The phone_number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     * The isPassword
     */
    public Integer getIsPassword() {
        return isPassword;
    }

    /**
     *
     * @param isPassword
     * The is_password
     */
    public void setIsPassword(Integer isPassword) {
        this.isPassword = isPassword;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The gender
     */
    public int getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The province
     */
    public String getProvince() {
        return province;
    }

    /**
     *
     * @param province
     * The province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The sign
     */
    public String getSign() {
        return sign;
    }

    /**
     *
     * @param sign
     * The sign
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     *
     * @return
     * The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     *
     * @param birthday
     * The birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     *
     * @return
     * The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     * The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     * The userNumber
     */
    public Integer getUserNumber() {
        return userNumber;
    }

    /**
     *
     * @param userNumber
     * The user_number
     */
    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    /**
     *
     * @return
     * The watchedNumber
     */
    public Integer getWatchedNumber() {
        return watchedNumber;
    }

    /**
     *
     * @param watchedNumber
     * The watched_number
     */
    public void setWatchedNumber(Integer watchedNumber) {
        this.watchedNumber = watchedNumber;
    }

    /**
     *
     * @return
     * The fansNumber
     */
    public Integer getFansNumber() {
        return fansNumber;
    }

    /**
     *
     * @param fansNumber
     * The fans_number
     */
    public void setFansNumber(Integer fansNumber) {
        this.fansNumber = fansNumber;
    }

    /**
     *
     * @return
     * The favoursNumber
     */
    public Integer getFavoursNumber() {
        return favoursNumber;
    }

    /**
     *
     * @param favoursNumber
     * The favours_number
     */
    public void setFavoursNumber(Integer favoursNumber) {
        this.favoursNumber = favoursNumber;
    }

    /**
     *
     * @return
     * The experienceNumber
     */
    public Integer getExperienceNumber() {
        return experienceNumber;
    }

    /**
     *
     * @param experienceNumber
     * The experience_number
     */
    public void setExperienceNumber(Integer experienceNumber) {
        this.experienceNumber = experienceNumber;
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
     * The deletedAt
     */
    public Object getDeletedAt() {
        return deletedAt;
    }

    /**
     *
     * @param deletedAt
     * The deleted_at
     */
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

}
