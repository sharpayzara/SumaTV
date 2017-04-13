package com.ddm.live.models.bean.streams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class ModifyStreamWatchedNumberRequest extends StreamsBaseRequest {
    @Expose
    @SerializedName("stream_id")
    private String streamID;

    @Expose
    @SerializedName("watched_number")
    private String watchedNumber;

    public ModifyStreamWatchedNumberRequest(String streamID, String watchedNumber) {
        setMethodName("modifyStreamWatchedNumber");
        this.streamID = streamID;
        this.watchedNumber = watchedNumber;
    }

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public String getWatchedNumber() {
        return watchedNumber;
    }

    public void setWatchedNumber(String watchedNumber) {
        this.watchedNumber = watchedNumber;
    }
}
