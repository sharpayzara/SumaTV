package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.address.Address;
import com.ddm.live.models.network.api.LocationApi;

import rx.Observable;

/**
 * Created by wsheng on 16/1/18.
 * 直播相关的接口API
 */
public class LocationApiService extends BaseService {

    private String TAG = "ws";

    private LocationApi apiService;


    public LocationApiService(LocationApi apiService) {
        this.apiService = apiService;
    }


    /**
     * 通过jingwei获取地址位置
     *
     * @param locatioin
     * @return
     */
    public Observable<Address> getAddress(String locatioin) {
        return apiService.getAddress(locatioin);
    }

}