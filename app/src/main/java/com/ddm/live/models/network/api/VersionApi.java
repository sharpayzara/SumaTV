package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.version.UpdateVersionResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cxx on 2016/10/20.
 */
public interface VersionApi {

    //查询最新版本信息
    @GET("update/check")
    Observable<UpdateVersionResponse> updateVersion();
}
