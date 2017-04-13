package com.ddm.live.models.bean.account;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.accountbeans.RechargeInfoBean;
import com.ddm.live.models.bean.common.commonbeans.MetaBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/25.
 */
public class QueryChargeInfoListResponse implements ResponseBaseInterface {

    @SerializedName("data")
    @Expose
    private List<RechargeInfoBean> data = new ArrayList<RechargeInfoBean>();
    @SerializedName("meta")
    @Expose
    private MetaBean meta;

    /**
     *
     * @return
     * The data
     */
    public List<RechargeInfoBean> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<RechargeInfoBean> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The meta
     */
    public MetaBean getMeta() {
        return meta;
    }

    /**
     *
     * @param meta
     * The meta
     */
    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

}