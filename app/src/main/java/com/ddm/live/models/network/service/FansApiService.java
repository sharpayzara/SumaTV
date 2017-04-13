package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.fans.FansCancelFocusedMasterByIdRequest;
import com.ddm.live.models.bean.fans.FansCancelFocusedMasterByIdResponse;
import com.ddm.live.models.bean.fans.FansFocusOnMasterByIDRequest;
import com.ddm.live.models.bean.fans.FansFocusOnMasterByIDResponse;
import com.ddm.live.models.bean.fans.FansGetFansListRequest;
import com.ddm.live.models.bean.fans.FansGetFansListResponse;
import com.ddm.live.models.bean.fans.FansGetFocusedMastersListRequest;
import com.ddm.live.models.bean.fans.FansGetFocusedMastersListResponse;
import com.ddm.live.models.network.api.FansApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public class FansApiService extends BaseService {
    FansApi fansApi;
    RetrofitApiManager retrofitApiManager;

    public FansApiService() {
        retrofitApiManager = new RetrofitApiManager();
        fansApi = retrofitApiManager.provideWithHeaderApi().create(FansApi.class);
    }

    /**
     * 1.Fans_通过ID关注某主播
     */
    public Observable<FansFocusOnMasterByIDResponse> focusOnMasterByID(FansFocusOnMasterByIDRequest request) {
        Observable<FansFocusOnMasterByIDResponse> response = fansApi.focusOnMasterByID(request);
        return response;
    }

    /**
     * 2.Fans_获取关注的主播列表
     */
    public Observable<FansGetFocusedMastersListResponse> getFocusedMastersList(FansGetFocusedMastersListRequest request) {
        Integer page = request.getPage();
        Observable<FansGetFocusedMastersListResponse> response = fansApi.getFocusedMastersList(page);
        return response;
    }

    /**
     * 3.Fans_获取粉丝列表
     */
    public Observable<FansGetFansListResponse> getFansList(FansGetFansListRequest request) {
        Integer page = request.getPage();
        Observable<FansGetFansListResponse> response = fansApi.getFansList(page);
        return response;
    }


    /**
     * 4.Fans_通过ID取消关注某用户
     */
    public Observable<FansCancelFocusedMasterByIdResponse> cancelFocusedMasterById(FansCancelFocusedMasterByIdRequest request) {
        Integer id=request.getId();
        Observable<FansCancelFocusedMasterByIdResponse> response = fansApi.cancelFocusedMasterById(id);
        return response;
    }
}