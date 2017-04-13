package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.account.BuyVirtualCoinsRequest;
import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.DrawCashDiamondsRequest;
import com.ddm.live.models.bean.account.DrawCashDiamondsResponse;
import com.ddm.live.models.bean.account.GetAccountInfoRequest;
import com.ddm.live.models.bean.account.GetAccountInfoResponse;
import com.ddm.live.models.bean.account.GetExchangeListRequest;
import com.ddm.live.models.bean.account.GetExchangeListResponse;
import com.ddm.live.models.bean.account.GetRechargeListRequest;
import com.ddm.live.models.bean.account.GetRechargeListResponse;
import com.ddm.live.models.bean.account.PresentVirtualCoinsRequest;
import com.ddm.live.models.bean.account.PresentVirtualCoinsResponse;
import com.ddm.live.models.bean.account.QueryChargeInfoListRequest;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.account.TurnDiamondsToVirtualCoinsRequest;
import com.ddm.live.models.bean.account.TurnDiamondsToVirtualCoinsResponse;
import com.ddm.live.models.network.api.AccountApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/8/11.
 */
public class AccountApiService extends BaseService {
    AccountApi accountApi;
    RetrofitApiManager retrofitApiManager;

    public AccountApiService() {
        retrofitApiManager = new RetrofitApiManager();
        accountApi = retrofitApiManager.provideWithHeaderApi().create(AccountApi.class);
    }

    /**
     * 1.Account_获取账户信息
     */
    public Observable<GetAccountInfoResponse> getAccountInfo(GetAccountInfoRequest request) {
        Observable<GetAccountInfoResponse> response = accountApi.getAccountInfo();
        return response;
    }

    /**
     * 2.Account_购买虚拟币
     */
    public Observable<BuyVirtualCoinsResponse> buyVirtualCoins(BuyVirtualCoinsRequest request) {
        Observable<BuyVirtualCoinsResponse> response = accountApi.buyVirtualCoins(request);
        return response;
    }

    /**
     * 3.Account_赠送虚拟币
     */
    public Observable<PresentVirtualCoinsResponse> presentVirtualCoins(PresentVirtualCoinsRequest request) {
        Observable<PresentVirtualCoinsResponse> response = accountApi.presentVirtualCoins(request);
        return response;
    }

    /**
     * 4.Account_钻石转虚拟币
     */
    public Observable<TurnDiamondsToVirtualCoinsResponse> turnDiamondsToVirtualCoins(TurnDiamondsToVirtualCoinsRequest request) {
        Observable<TurnDiamondsToVirtualCoinsResponse> response = accountApi.turnDiamondsToVirtualCoins(request);
        return response;

    }

    /**
     * 5.Account_钻石提现
     */
    public Observable<DrawCashDiamondsResponse> drawCashDiamonds(DrawCashDiamondsRequest request) {
        Observable<DrawCashDiamondsResponse> response = accountApi.drawCashDiamonds(request);
        return response;
    }

    /**
     * 6.Account_查询充值列表
     */

    public Observable<QueryChargeInfoListResponse> queryRechargeInfoList(QueryChargeInfoListRequest request) {
        String id = request.getId();
        Observable<QueryChargeInfoListResponse> response = accountApi.queryRechargeInfoList(id);
        return response;
    }

    /**
     * 7.Account_查询充值数值列表
     */

    public Observable<GetRechargeListResponse> getRechargeList(GetRechargeListRequest request) {
        Observable<GetRechargeListResponse> response = accountApi.getRechargeList();
        return response;
    }
    /**
     * 8.Account_查询兑换列表
     */

    public Observable<GetExchangeListResponse> getRechargeList(GetExchangeListRequest request) {
        Observable<GetExchangeListResponse> response = accountApi.getExchangeList();
        return response;
    }
}
