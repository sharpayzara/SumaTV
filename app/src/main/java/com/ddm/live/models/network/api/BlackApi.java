package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.blacklist.AddUserIntoBlackListByIDRequest;
import com.ddm.live.models.bean.blacklist.AddUserIntoBlackListByIDResponse;
import com.ddm.live.models.bean.blacklist.GetBlackListResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cxx on 2016/8/11.
 */
public interface BlackApi {
    /**
     * 1.获取黑名单列表
     */
    @GET("black_list")
    Observable<GetBlackListResponse> getBlackList();

    /**
     * 2.通过ID将某用户添加到黑名单
     */
    @POST("black_list")
    Observable<AddUserIntoBlackListByIDResponse> addUserIntoBlacklistByID(@Body AddUserIntoBlackListByIDRequest body);
}
