package com.ddm.live.models.bean.account;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;

import java.util.List;

/**
 * Created by ytzys on 2017/2/22.
 */
public class GetExchangeListResponse implements ResponseBaseInterface {
    private List<StarDiamondExchangeBean> data;

    public List<StarDiamondExchangeBean> getData() {
        return data;
    }

    public void setData(List<StarDiamondExchangeBean> data) {
        this.data = data;
    }
}
