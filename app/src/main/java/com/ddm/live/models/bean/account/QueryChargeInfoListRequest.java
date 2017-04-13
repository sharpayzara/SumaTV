package com.ddm.live.models.bean.account;

/**
 * Created by cxx on 2016/8/25.
 */
public class QueryChargeInfoListRequest extends AccountBaseRequest{
    private String id;

    public QueryChargeInfoListRequest(String id) {
        setMethodName("queryRechargeInfoList");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
