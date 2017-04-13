package com.ddm.live.models.network.api;


import com.ddm.live.models.bean.address.Address;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsheng on 16/1/7.
 */
public interface LocationApi {
    /**
     * 通过经纬度获取地址位置(中文)
     * //http://api.map.baidu.com/geocoder/v2/?ak=NRLlKYZy8QYaBonDuxkG7FYK&location=39.983424,116.322987&output=json&pois=0
     *
     * @param location
     * @return
     */
    @GET("http://api.map.baidu.com/geocoder/v2/?ak=NRLlKYZy8QYaBonDuxkG7FYK&output=json&pois=0")
    Observable<Address> getAddress(@Query("location") String location);

}
