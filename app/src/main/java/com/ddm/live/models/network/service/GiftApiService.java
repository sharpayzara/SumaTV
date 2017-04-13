package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.gift.GetGiftsListRequest;
import com.ddm.live.models.bean.gift.GetGiftsListResponse;
import com.ddm.live.models.network.api.GiftApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public class GiftApiService extends BaseService {
    GiftApi giftApi;
    RetrofitApiManager retrofitApiManager;
    public GiftApiService() {
        retrofitApiManager = new RetrofitApiManager();
        giftApi = retrofitApiManager.provideWithHeaderApi().create(GiftApi.class);
    }

    public Observable<GetGiftsListResponse> getGiftList(GetGiftsListRequest request){
        Observable<GetGiftsListResponse> response=giftApi.getGiftList();
        return response;
    }
}
