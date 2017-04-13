package com.ddm.live.models.bean.basebean.request;

/**
 * Created by JiGar on 2016/1/12.
 */
public class RequestHeader {
    private String code;
    private String time;

    public RequestHeader(String code, String time) {
        this.code = code;
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                "code:\"" + code + '\"' +
                ", time:\"" + time + '\"' +
                '}';
    }
}
