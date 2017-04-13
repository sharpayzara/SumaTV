package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.rechargeinfo.GetRechargeInfoListRequest;
import com.ddm.live.models.bean.rechargeinfo.GetRechargeInfoListResponse;
import com.ddm.live.models.network.api.RechargeInfoApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by Administrator on 2016/8/9.
 */
public class RechargeInfoApiService extends BaseService {
    RetrofitApiManager retrofitApiManager;
    RechargeInfoApi apiService;

    public RechargeInfoApiService() {
        retrofitApiManager = new RetrofitApiManager();
        apiService = retrofitApiManager.provideWithHeaderApi().create(RechargeInfoApi.class);
    }

    /**
     * 获取充值列表
     */
    public Observable<GetRechargeInfoListResponse> getRechargeInfoList(GetRechargeInfoListRequest request) {
        Observable<GetRechargeInfoListResponse> response = apiService.getRechargeInfoList();
        return response;
    }
}
