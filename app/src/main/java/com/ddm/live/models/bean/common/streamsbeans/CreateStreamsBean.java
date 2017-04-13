package com.ddm.live.models.bean.common.streamsbeans;

/**
 * Created by Administrator on 2016/8/11.
 */

import com.ddm.live.models.bean.common.commonbeans.CreatedAtBean;
import com.ddm.live.models.bean.common.commonbeans.UpdatedAtBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * StreamAndUserAndGroupBean
 * CreateStreamsBean.java
 */
public class CreateStreamsBean {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("user_id")
    private int userId;

    @Expose
    @SerializedName("stream_id")
    private String streamId;

    @Expose
    @SerializedName("stream_title")
    private String streamTitle;

    @Expose
    @SerializedName("disabled")
    private String disabled;
    @Expose
    @SerializedName("stream_json")
    private String streamJson;

    @Expose
    @SerializedName("subject")
    private String subject;

    @Expose
    @SerializedName("province")
    private String province;
    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("cover_pic")
    private String coverPic;

    @Expose
    @SerializedName("topic")
    private String topic;

    @Expose
    @SerializedName("favour_nums")
    private String favourNums;

    @Expose
    @SerializedName("watched_nums")
    private String watchedNums;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("room_id")
    private String roomId;

    @Expose
    @SerializedName("created_at")
    private CreatedAtBean createdAt;

    @Expose
    @SerializedName("updated_at")
    private UpdatedAtBean updatedAt;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamTitle(String streamTitle) {
        this.streamTitle = streamTitle;
    }

    public String getStreamTitle() {
        return streamTitle;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setStreamJson(String streamJson) {
        this.streamJson = streamJson;
    }

    public String getStreamJson() {
        return streamJson;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setFavourNums(String favourNums) {
        this.favourNums = favourNums;
    }

    public String getFavourNums() {
        return favourNums;
    }

    public void setWatchedNums(String watchedNums) {
        this.watchedNums = watchedNums;
    }

    public String getWatchedNums() {
        return watchedNums;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setCreatedAt(CreatedAtBean createdAt) {
        this.createdAt = createdAt;
    }

    public CreatedAtBean getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(UpdatedAtBean updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UpdatedAtBean getUpdatedAt() {
        return updatedAt;
    }

}