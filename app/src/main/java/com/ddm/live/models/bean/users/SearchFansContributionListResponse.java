package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.userbeans.ContributionTotalMetaBean;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cxx on 2016/11/3.
 */
public class SearchFansContributionListResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("data")
    private List<FansContributionInfoBean> data;

    public List<FansContributionInfoBean> getData() {
        return data;
    }

    @Expose
    @SerializedName("meta")
    private ContributionTotalMetaBean meta;

    public ContributionTotalMetaBean getMeta() {
        return meta;
    }
}
