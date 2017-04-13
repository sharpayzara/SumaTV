package com.ddm.live.models.bean.streams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JiGar on 2016/9/29.
 */
public class DeleteStreamRequest extends StreamsBaseRequest {
    @Expose
    @SerializedName("id")
    private String id;

    public DeleteStreamRequest(String id) {
        setMethodName("deleteStream");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
