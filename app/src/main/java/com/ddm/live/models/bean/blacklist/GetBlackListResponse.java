package com.ddm.live.models.bean.blacklist;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.blacklistbeans.BlackListBean;
import com.ddm.live.models.bean.common.commonbeans.MetaBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/11.
 */
public class GetBlackListResponse implements ResponseBaseInterface {

    @SerializedName("data")
    @Expose
    private List<BlackListBean> data = new ArrayList<BlackListBean>();
    @SerializedName("meta")
    @Expose
    private MetaBean meta;

    public List<BlackListBean> getData() {
        return data;
    }

    public void setData(List<BlackListBean> data) {
        this.data = data;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }
}
