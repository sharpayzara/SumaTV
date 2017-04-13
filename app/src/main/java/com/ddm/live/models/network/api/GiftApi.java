package com.ddm.live.models.network.api;


import com.ddm.live.models.bean.gift.GetGiftsListResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public interface GiftApi {

    //根据token获取礼物列表
    @GET("gift/gift_list")
    Observable<GetGiftsListResponse> getGiftList();
}
