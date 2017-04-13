package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.rechargeinfo.GetRechargeInfoListResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cxx on 2016/8/9.
 */
public interface RechargeInfoApi {

    //通过token获取当前用户信息
    @GET("recharge/recharge_list")
    Observable<GetRechargeInfoListResponse> getRechargeInfoList();
}
