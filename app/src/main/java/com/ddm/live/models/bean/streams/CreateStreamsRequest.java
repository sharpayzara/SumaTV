package com.ddm.live.models.bean.streams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class CreateStreamsRequest extends StreamsBaseRequest {
    @Expose
    @SerializedName("subject")
    private String subject;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("address")
    private String address;

    public CreateStreamsRequest(String subject, String title, String address) {
        setMethodName("creatStreams");
        this.subject = subject;
        this.title = title;
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
