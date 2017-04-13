package com.ddm.live.models.bean.common.commonbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxx on 2016/8/9.
 */
public class CreatedAtBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("timezone_type")
    private int timezoneType;

    @Expose
    @SerializedName("timezone")
    private String timezone;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTimezoneType(int timezoneType) {
        this.timezoneType = timezoneType;
    }

    public int getTimezoneType() {
        return timezoneType;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone() {
        return timezone;
    }

    @Override
    public String toString() {
        return "CreatedAtBean{" +
                "date='" + date + '\'' +
                ", timezoneType=" + timezoneType +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}