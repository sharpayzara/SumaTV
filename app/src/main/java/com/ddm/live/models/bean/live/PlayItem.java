package com.ddm.live.models.bean.live;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ddm.live.activities.VideoPlayerActivity;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;

/**
 * Created by wsheng on 16/1/19.
 */
public class PlayItem {


    private Integer cid;
    private String cname;
    private Integer id;
    private Integer uid;
    private String position;
    private Integer praiseNum;
    private String liveAd;
    private String recordAd;
    private String logoAd;
    private Integer status;
    private Integer stime;
    private Integer watchNum;
    private Integer nowNum;
    private String name;
    private Integer islive;
    private Integer isopen;
    private String qid;
    private String title;
    private String headimgurl;
    private SpecStreamBean specStreamBean;
    private Context context;

    public PlayItem(Context context, SpecStreamBean specStreamBean) {
        this.context = context;
        this.specStreamBean = specStreamBean;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getLiveAd() {
        return liveAd;
    }

    public void setLiveAd(String liveAd) {
        this.liveAd = liveAd;
    }

    public String getRecordAd() {
        return recordAd;
    }

    public void setRecordAd(String recordAd) {
        this.recordAd = recordAd;
    }

    public String getLogoAd() {
        return logoAd;
    }

    public void setLogoAd(String logoAd) {
        this.logoAd = logoAd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStime() {
        return stime;
    }

    public void setStime(Integer stime) {
        this.stime = stime;
    }

    public Integer getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(Integer watchNum) {
        this.watchNum = watchNum;
    }

    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIslive() {
        return islive;
    }

    public void setIslive(Integer islive) {
        this.islive = islive;
    }

    public Integer getIsopen() {
        return isopen;
    }

    public void setIsopen(Integer isopen) {
        this.isopen = isopen;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    //播放
    public Intent play() {
        String playUrl = (specStreamBean.getStatus() == 0) ? specStreamBean.getRtmpLiveUrls() : specStreamBean.getHlsPlaybackUrls();
        String roomID = String.valueOf(specStreamBean.getRoomId());
        Integer anchorId = specStreamBean.getUserId();
        String viedoid = String.valueOf(specStreamBean.getStreamId());
        String uname = String.valueOf(specStreamBean.getName());
        String userImgurl = String.valueOf(specStreamBean.getAvatar());
        Integer id = specStreamBean.getId();
        String userNumber = String.valueOf(specStreamBean.getUserNumber());
        String avatarSmall = specStreamBean.getAvatarSmall();
        Integer tagLevel = specStreamBean.getLevel();
        Integer streamStatus = specStreamBean.getStatus();
        String sign=specStreamBean.getSign();
        String topic=specStreamBean.getSubject();
        int gender=specStreamBean.getGender();
        int totalWatched = specStreamBean.getWatchedNums();
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLive", specStreamBean.getStatus() == 0);
        bundle.putString("mVideoPath", playUrl);
        bundle.putString("roomID", roomID);//roomId
        bundle.putInt("anchorID", anchorId);
        bundle.putString("videoID", viedoid);
        bundle.putString("uname", uname);
        bundle.putString("headimgUrl", userImgurl);
        bundle.putString("avatarsmall", avatarSmall);
        bundle.putInt("id", id);
        bundle.putString("userNumber", userNumber);
        bundle.putInt("tagLevel", tagLevel);
        bundle.putInt("streamStatus", streamStatus);
        bundle.putInt("totalWatched", totalWatched);
        bundle.putInt("gender",gender);
        bundle.putString("sign",sign);
        bundle.putString("topic",topic);
        intent.putExtras(bundle);
        return intent;
    }
}
