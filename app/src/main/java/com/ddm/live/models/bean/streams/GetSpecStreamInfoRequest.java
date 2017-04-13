package com.ddm.live.models.bean.streams;

/**
 * Created by cxx on 2016/8/15.
 */
public class GetSpecStreamInfoRequest extends StreamsBaseRequest{
    private String qiniuId;
    public GetSpecStreamInfoRequest(String qiniuId) {
        setMethodName("getSpecMainInfo");
        this.qiniuId=qiniuId;
    }

    public String getQiniuId() {
        return qiniuId;
    }

    public void setQiniuId(String qiniuId) {
        this.qiniuId = qiniuId;
    }
}
