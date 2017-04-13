package com.ddm.live.views.iface;

import com.ddm.live.models.bean.account.StarDiamondExchangeBean;

import java.util.List;

/**
 * Created by ytzys on 2017/2/22.
 */
public interface IMystarView extends IAccountView {
    public void getExchargeListInfoResponse(List<StarDiamondExchangeBean> chargeList);
}
