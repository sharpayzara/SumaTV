package com.ddm.live.models.bean.common.versionbeans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/10/20.
 */
public class Versionbean {
    @Expose
    @SerializedName("app_name")
    private String appName;//应用名

    @Expose
    @SerializedName("version_id")
    private Integer versionCode;//版本升级号

    @Expose
    @SerializedName("current_version")
    private String versionName;//版本名

    @Expose
    @SerializedName("version_desc")
    private String versionDesc;//版本描述

    @Expose
    @SerializedName("download_path")
    private String versionPath;//apk下载路径或存储路径
    @Expose
    @SerializedName("must_download")
    private String mustDownload;

    public Versionbean() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionPath() {
        return versionPath;
    }

    public void setVersionPath(String versionPath) {
        this.versionPath = versionPath;
    }

    public String getMustDownload() {
        return mustDownload;
    }

    public void setMustDownload(String mustDownload) {
        this.mustDownload = mustDownload;
    }

    public Versionbean(String appName, Integer versionCode, String versionName, String versionDesc, String versionPath, String mustDownload) {
        this.appName = appName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.versionDesc = versionDesc;
        this.versionPath = versionPath;
        this.mustDownload = mustDownload;
    }
}
