package com.ddm.live.models.bean.users;

/**
 * Created by Administrator on 2016/11/18.
 */

public class GetFansContributiontTotalRequest extends UsersBaseRequest{
    private Integer id;

    public GetFansContributiontTotalRequest(Integer id) {
        setMethodName("getFansContributionTotal");
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
