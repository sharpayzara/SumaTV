package com.ddm.live.models.bean.common.streamsbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class SpecStreamBean extends StreamsBean {


    @SerializedName("rtmp_publish_url")
    @Expose
    private String rtmpPublishUrl;
    @SerializedName("rtmp_live_urls")
    @Expose
    private String rtmpLiveUrls;
    @SerializedName("hls_live_urls")
    @Expose
    private String hlsLiveUrls;
    @SerializedName("http_flv_live_urls")
    @Expose
    private String httpFlvLiveUrls;
    @SerializedName("hls_playback_urls")
    @Expose
    private String hlsPlaybackUrls;

    public String getRtmpPublishUrl() {
        return rtmpPublishUrl;
    }

    public void setRtmpPublishUrl(String rtmpPublishUrl) {
        this.rtmpPublishUrl = rtmpPublishUrl;
    }

    public String getRtmpLiveUrls() {
        return rtmpLiveUrls;
    }

    public void setRtmpLiveUrls(String rtmpLiveUrls) {
        this.rtmpLiveUrls = rtmpLiveUrls;
    }

    public String getHlsLiveUrls() {
        return hlsLiveUrls;
    }

    public void setHlsLiveUrls(String hlsLiveUrls) {
        this.hlsLiveUrls = hlsLiveUrls;
    }

    public String getHttpFlvLiveUrls() {
        return httpFlvLiveUrls;
    }

    public void setHttpFlvLiveUrls(String httpFlvLiveUrls) {
        this.httpFlvLiveUrls = httpFlvLiveUrls;
    }

    public String getHlsPlaybackUrls() {
        return hlsPlaybackUrls;
    }

    public void setHlsPlaybackUrls(String hlsPlaybackUrls) {
        this.hlsPlaybackUrls = hlsPlaybackUrls;
    }
}
