package com.ddm.live.models.bean.fans;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.MetaBean;
import com.ddm.live.models.bean.common.fansbeans.FansBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/11.
 */
public class FansGetFansListResponse implements ResponseBaseInterface {
    @SerializedName("data")
    @Expose
    private List<FansBean> data = new ArrayList<FansBean>();
    @SerializedName("meta")
    @Expose
    private MetaBean meta;

    public List<FansBean> getData() {
        return data;
    }

    public void setData(List<FansBean> data) {
        this.data = data;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }
}
