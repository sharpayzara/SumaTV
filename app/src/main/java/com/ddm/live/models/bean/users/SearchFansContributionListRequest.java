package com.ddm.live.models.bean.users;

/**
 * Created by cxx on 2016/11/3.
 */
public class SearchFansContributionListRequest extends UsersBaseRequest {
    private Integer id;

    public SearchFansContributionListRequest(Integer id) {
        setMethodName("searchFansContributionList");
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
