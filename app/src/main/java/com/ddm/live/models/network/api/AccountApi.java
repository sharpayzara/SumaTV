package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.account.BuyVirtualCoinsRequest;
import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.DrawCashDiamondsRequest;
import com.ddm.live.models.bean.account.DrawCashDiamondsResponse;
import com.ddm.live.models.bean.account.GetAccountInfoResponse;
import com.ddm.live.models.bean.account.GetExchangeListResponse;
import com.ddm.live.models.bean.account.GetRechargeListResponse;
import com.ddm.live.models.bean.account.PresentVirtualCoinsRequest;
import com.ddm.live.models.bean.account.PresentVirtualCoinsResponse;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.account.TurnDiamondsToVirtualCoinsRequest;
import com.ddm.live.models.bean.account.TurnDiamondsToVirtualCoinsResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cxx on 2016/8/11.
 */
public interface AccountApi {
    /**
     * 1.Account_获取账户信息
     */
    @GET("account/info")
    Observable<GetAccountInfoResponse> getAccountInfo();

    /**
     * 2.Account_购买虚拟币
     */
    @POST("wechat_payment/create_order")
    Observable<BuyVirtualCoinsResponse> buyVirtualCoins(@Body BuyVirtualCoinsRequest body);

    /**
     * 3.Account_赠送虚拟币
     */
    @POST("account/send_gift")
    Observable<PresentVirtualCoinsResponse> presentVirtualCoins(@Body PresentVirtualCoinsRequest body);

    /**
     * 4.Account_钻石转虚拟币
     */
    @POST("account/convert")
    Observable<TurnDiamondsToVirtualCoinsResponse> turnDiamondsToVirtualCoins(@Body TurnDiamondsToVirtualCoinsRequest body);

    /**
     * 5.Account_钻石提现
     */
    @POST("account/tixian")
    Observable<DrawCashDiamondsResponse> drawCashDiamonds(@Body DrawCashDiamondsRequest body);

    /**
     * 6.Account_查询充值列表
     */
    @GET("account/record_list/{id}")
    Observable<QueryChargeInfoListResponse> queryRechargeInfoList(@Path("id") String id);
    /**
     * 7.Account_获取充值数额列表
     */
    @GET("recharge/recharge_config")
    Observable<GetRechargeListResponse> getRechargeList();

    /**
     * 8.Account_获取兑换列表
     */
    @GET("recharge/recharge_config")
    Observable<GetExchangeListResponse> getExchangeList();
}
