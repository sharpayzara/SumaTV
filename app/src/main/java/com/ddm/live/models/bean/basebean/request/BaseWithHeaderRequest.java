package com.ddm.live.models.bean.basebean.request;

/**
 * Created by cxx on 2016/8/12.
 */
public class BaseWithHeaderRequest extends BaseWithNoHeaderRequest {
    private String authorization;

    public BaseWithHeaderRequest() {
    }

    public BaseWithHeaderRequest(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
