package com.ddm.live.models.bean.live;

/**
 * Created by JiGar on 2016/1/17.
 */
public class LiveItem {

    private int id;

    private int uid;

    private String cname;

    private String headimgurl;

    private String position;

    private int praiseNum;

    private String liveAd;

    private String recordAd;

    private String logoAd;

    private int status;

    private int stime;

    private int etime;

    private int watchNum;

    private int noeNum;

    private String name;

    private int islive;

    private int isopen;

    private String qid;

    private String title;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    public void setLiveAd(String liveAd) {
        this.liveAd = liveAd;
    }

    public String getLiveAd() {
        return this.liveAd;
    }

    public void setRecordAd(String recordAd) {
        this.recordAd = recordAd;
    }

    public String getRecordAd() {
        return this.recordAd;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStime(int stime) {
        this.stime = stime;
    }

    public int getStime() {
        return this.stime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIslive(int islive) {
        this.islive = islive;
    }

    public int getIslive() {
        return this.islive;
    }

    public void setIsopen(int isopen) {
        this.isopen = isopen;
    }

    public int getIsopen() {
        return this.isopen;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQid() {
        return this.qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getLogoAd() {
        return logoAd;
    }

    public void setLogoAd(String logoAd) {
        this.logoAd = logoAd;
    }

    public int getEtime() {
        return etime;
    }

    public void setEtime(int etime) {
        this.etime = etime;
    }

    public int getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(int watchNum) {
        this.watchNum = watchNum;
    }

    public int getNoeNum() {
        return noeNum;
    }

    public void setNoeNum(int noeNum) {
        this.noeNum = noeNum;
    }
}

