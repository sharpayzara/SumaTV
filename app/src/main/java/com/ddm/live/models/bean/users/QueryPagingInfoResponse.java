package com.ddm.live.models.bean.users;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.MetaBean;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;

import java.util.List;

/**
 * Created by cxx on 2016/8/10.
 */
public class QueryPagingInfoResponse implements ResponseBaseInterface {
    private List<UserInfoBean> data;
    private MetaBean meta;
    public void setData(List<UserInfoBean> data) {
        this.data = data;
    }
    public List<UserInfoBean> getData() {
        return data;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }
    public MetaBean getMeta() {
        return meta;
    }

}