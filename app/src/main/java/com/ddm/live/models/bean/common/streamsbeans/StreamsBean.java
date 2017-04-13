package com.ddm.live.models.bean.common.streamsbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/11.
 */
public class StreamsBean implements Serializable{
    private static final long serialVersionUID = 1L;

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
    @SerializedName("avatar_small")
    @Expose
    private String avatarSmall;
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
    private String deletedAt;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("stream_id")
    @Expose
    private String streamId;
    @SerializedName("stream_title")
    @Expose
    private String streamTitle;
    @SerializedName("disabled")
    @Expose
    private Integer disabled;
    @SerializedName("stream_json")
    @Expose
    private String streamJson;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cover_pic")
    @Expose
    private String coverPic;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("favour_nums")
    @Expose
    private Integer favourNums;
    @SerializedName("watched_nums")
    @Expose
    private Integer watchedNums;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("level")
    @Expose
    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
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

    public Integer getIsPassword() {
        return isPassword;
    }

    public void setIsPassword(Integer isPassword) {
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getWatchedNumber() {
        return watchedNumber;
    }

    public void setWatchedNumber(Integer watchedNumber) {
        this.watchedNumber = watchedNumber;
    }

    public Integer getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(Integer fansNumber) {
        this.fansNumber = fansNumber;
    }


    public void setFavoursNumber(Integer favoursNumber) {
        this.favoursNumber = favoursNumber;
    }

    public Integer getExperienceNumber() {
        return experienceNumber;
    }

    public void setExperienceNumber(Integer experienceNumber) {
        this.experienceNumber = experienceNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamTitle() {
        return streamTitle;
    }

    public void setStreamTitle(String streamTitle) {
        this.streamTitle = streamTitle;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getStreamJson() {
        return streamJson;
    }

    public void setStreamJson(String streamJson) {
        this.streamJson = streamJson;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getFavourNums() {
        return favourNums;
    }

    public void setFavourNums(Integer favourNums) {
        this.favourNums = favourNums;
    }

    public Integer getWatchedNums() {
        return watchedNums;
    }

    public void setWatchedNums(Integer watchedNums) {
        this.watchedNums = watchedNums;
    }

    public String getAvatarSmall() {
        return avatarSmall;
    }

    public void setAvatarSmall(String avatarSmall) {
        this.avatarSmall = avatarSmall;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
