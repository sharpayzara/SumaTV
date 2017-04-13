package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.version.UpdateVersionRequest;
import com.ddm.live.models.bean.version.UpdateVersionResponse;
import com.ddm.live.models.network.api.VersionApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/10/20.
 */
public class VersionApiService {
    VersionApi apiService;
    RetrofitApiManager retrofitApiManager;
    public VersionApiService() {
        retrofitApiManager = new RetrofitApiManager();
        apiService=retrofitApiManager.provideNoHeaderApi().create(VersionApi.class);
    }
    /**
     * 查询最新版本信息
     */
    public Observable<UpdateVersionResponse> updateVersion(UpdateVersionRequest request) {
        Observable<UpdateVersionResponse> response = apiService.updateVersion();
        return response;
    }

}
