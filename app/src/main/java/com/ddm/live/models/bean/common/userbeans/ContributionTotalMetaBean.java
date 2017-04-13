package com.ddm.live.models.bean.common.userbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/18.
 */

public class ContributionTotalMetaBean {
    @Expose
    @SerializedName("total")
    private String total;

    public String getTotal() {
        return total;
    }
}
