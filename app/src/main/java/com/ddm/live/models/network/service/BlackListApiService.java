package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.blacklist.AddUserIntoBlackListByIDRequest;
import com.ddm.live.models.bean.blacklist.AddUserIntoBlackListByIDResponse;
import com.ddm.live.models.bean.blacklist.GetBlackListRequest;
import com.ddm.live.models.bean.blacklist.GetBlackListResponse;
import com.ddm.live.models.network.api.BlackApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/8/11.
 */
public class BlackListApiService extends BaseService {
    BlackApi blackApi;
    RetrofitApiManager retrofitApiManager;

    public BlackListApiService() {
        retrofitApiManager = new RetrofitApiManager();
        blackApi = retrofitApiManager.provideWithHeaderApi().create(BlackApi.class);
    }

    /**
     * 1.获取黑名单列表
     */
    public Observable<GetBlackListResponse> getBlackList(GetBlackListRequest request) {
        Observable<GetBlackListResponse> response = blackApi.getBlackList();
        return response;
    }

    /**
     * 2.通过ID将某用户添加到黑名单
     */
    public Observable<AddUserIntoBlackListByIDResponse> addUserIntoBlackListByID(AddUserIntoBlackListByIDRequest request) {
        Observable<AddUserIntoBlackListByIDResponse> response = blackApi.addUserIntoBlacklistByID(request);
        return response;
    }


}
