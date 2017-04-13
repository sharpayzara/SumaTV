package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.userbeans.ContributionTotalMetaBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/18.
 */

public class GetFansContributionTotalResponse implements ResponseBaseInterface{
    @SerializedName("data")
    @Expose
    ContributionTotalMetaBean data;

    public ContributionTotalMetaBean getData() {
        return data;
    }
}
