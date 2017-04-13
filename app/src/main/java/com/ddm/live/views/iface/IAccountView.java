package com.ddm.live.views.iface;

import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.GetRechargeListResponseData;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.common.accountbeans.AccountTypesDataBean;

import java.util.List;

/**
 * Created by cxx on 2016/9/14.
 */
public interface IAccountView extends IBaseView {

    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList);

    public void isPaySuccess(int position,int type);
//    public void onSuccess(String lebi, String diamonds);
    public void onGetPrepayId(BuyVirtualCoinsResponse response);

    public void onQueryRechargeRecoderList(QueryChargeInfoListResponse response);

    public void onGetFansContributionToatal(String total);

    public void getRechargeListInfoResponse(List<GetRechargeListResponseData> chargeList);
}
