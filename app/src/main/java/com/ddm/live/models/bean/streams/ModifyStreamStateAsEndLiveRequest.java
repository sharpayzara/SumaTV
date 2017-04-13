package com.ddm.live.models.bean.streams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class ModifyStreamStateAsEndLiveRequest extends StreamsBaseRequest {
    @Expose
    @SerializedName("title")
    String title;
    String qiniuId;
    public ModifyStreamStateAsEndLiveRequest(String title,String qiniuId) {
        setMethodName("modifyStreamStateAsEndLive");
        this.title = title;
        this.qiniuId=qiniuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQiniuId() {
        return qiniuId;
    }

    public void setQiniuId(String qiniuId) {
        this.qiniuId = qiniuId;
    }
}
