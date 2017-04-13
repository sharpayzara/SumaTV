package com.ddm.live.models.network.api;


import com.ddm.live.models.bean.fans.FansCancelFocusedMasterByIdResponse;
import com.ddm.live.models.bean.fans.FansFocusOnMasterByIDRequest;
import com.ddm.live.models.bean.fans.FansFocusOnMasterByIDResponse;
import com.ddm.live.models.bean.fans.FansGetFansListResponse;
import com.ddm.live.models.bean.fans.FansGetFocusedMastersListResponse;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public interface FansApi {
    //(1)Fans_通过ID关注某主播
    @POST("fans/")
    Observable<FansFocusOnMasterByIDResponse> focusOnMasterByID(@Body FansFocusOnMasterByIDRequest body);

    //(2)Fans_获取关注的主播列表
    @GET("masters_page/{page}")
    Observable<FansGetFocusedMastersListResponse> getFocusedMastersList(@Path("page") Integer page);

    //(3)Fans_获取粉丝列表
    @GET("fans_page/{page}")
    Observable<FansGetFansListResponse> getFansList(@Path("page") Integer page);

    //(4)Fans_通过ID取消关注某用户
    @DELETE("fans/{id}")
    Observable<FansCancelFocusedMasterByIdResponse> cancelFocusedMasterById(@Path("id") Integer id);
}
