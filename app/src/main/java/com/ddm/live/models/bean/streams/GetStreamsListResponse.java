package com.ddm.live.models.bean.streams;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.MetaBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2016/8/11.
 */
public class GetStreamsListResponse implements ResponseBaseInterface {

    @Expose
    private List<StreamsBean> data = new ArrayList<StreamsBean>();
    @SerializedName("meta")
    @Expose
    private MetaBean meta;

    public List<StreamsBean> getData() {
        return data;
    }

    public void setData(List<StreamsBean> data) {
        this.data = data;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }


}
