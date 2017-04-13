package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenRequest;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenResponse;
import com.ddm.live.models.bean.users.GetFansContributionTotalResponse;
import com.ddm.live.models.bean.users.GetFansContributiontTotalRequest;
import com.ddm.live.models.bean.users.GetRandomUsersRequest;
import com.ddm.live.models.bean.users.GetRandomUsersResponse;
import com.ddm.live.models.bean.users.GetSpecUserInfoRequest;
import com.ddm.live.models.bean.users.GetSpecUserInfoResponse;
import com.ddm.live.models.bean.users.ModifySpecUserGenderRequest;
import com.ddm.live.models.bean.users.ModifySpecUserHeaderRequest;
import com.ddm.live.models.bean.users.ModifySpecUserInfoRequest;
import com.ddm.live.models.bean.users.ModifySpecUserInfoResponse;
import com.ddm.live.models.bean.users.ModifySpecUserNameRequest;
import com.ddm.live.models.bean.users.ModifySpecUserSignRequest;
import com.ddm.live.models.bean.users.QueryPagingInfoRequest;
import com.ddm.live.models.bean.users.QueryPagingInfoResponse;
import com.ddm.live.models.bean.users.SearchFansContributionListRequest;
import com.ddm.live.models.bean.users.SearchFansContributionListResponse;
import com.ddm.live.models.bean.users.SearchUsersByKeywordRequest;
import com.ddm.live.models.bean.users.SearchUsersByKeywordResponse;
import com.ddm.live.models.network.api.UsersApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public class UsersApiService extends BaseService {
    UsersApi apiService;
    RetrofitApiManager retrofitApiManager;

    public UsersApiService() {
        retrofitApiManager = new RetrofitApiManager();
        apiService = retrofitApiManager.provideWithHeaderApi().create(UsersApi.class);
    }

    /**
     * Users_分页信息查询
     */
    public Observable<QueryPagingInfoResponse> queryPagingInfo(QueryPagingInfoRequest request) {
        Observable<QueryPagingInfoResponse> response = apiService.queryPagingInfo();
        return response;
    }


    /**
     * 获取指定用户信息
     */
    public Observable<GetSpecUserInfoResponse> getSpecUserInfo(GetSpecUserInfoRequest request) {
        Observable<GetSpecUserInfoResponse> response = apiService.getSpecUserInfo(request);
        return response;
    }

    /**
     * 修改指定用户信息
     */
    public Observable<ModifySpecUserInfoResponse> modifySpecUserInfo(ModifySpecUserInfoRequest request) {
        Observable<ModifySpecUserInfoResponse> response = apiService.modifySpecUserInfo(request);
        return response;
    }

    /**
     * 修改指定用户头像
     */
    public Observable<ModifySpecUserInfoResponse> modifySpecUserHeader(ModifySpecUserHeaderRequest request) {
        Observable<ModifySpecUserInfoResponse> response = apiService.modifySpecUserHeader(request);
        return response;
    }

    /**
     * 修改指定用户昵称
     */
    public Observable<ModifySpecUserInfoResponse> modifySpecUserName(ModifySpecUserNameRequest request) {
        Observable<ModifySpecUserInfoResponse> response = apiService.modifySpecUserName(request);
        return response;
    }

    /**
     * 修改指定用户昵称
     */
    public Observable<ModifySpecUserInfoResponse> modifySpecUserGender(ModifySpecUserGenderRequest request) {
        Observable<ModifySpecUserInfoResponse> response = apiService.modifySpecUserGender(request);
        return response;
    }

    /**
     * 修改指定用户个性签名
     */
    public Observable<ModifySpecUserInfoResponse> modifySpecUserSign(ModifySpecUserSignRequest request) {
        Observable<ModifySpecUserInfoResponse> response = apiService.modifySpecUserSign(request);
        return response;
    }

    /**
     * 根据关键字搜索用户
     */
    public Observable<SearchUsersByKeywordResponse> searchUsersByKeyword(SearchUsersByKeywordRequest request) {
        Observable<SearchUsersByKeywordResponse> response = apiService.searchUsersByKeyword(request);
        return response;
    }

    //通过token获取当前用户信息
    public Observable<GetCurrentUserInfoByTokenResponse> getCurrentUserInfoByToken(GetCurrentUserInfoByTokenRequest request) {
        Observable<GetCurrentUserInfoByTokenResponse> response = apiService.getCurrentUserInfoByToken();
        return response;
    }

    //随机获取一批用户
    public Observable<GetRandomUsersResponse> getRandomUsers(GetRandomUsersRequest request) {
        Observable<GetRandomUsersResponse> response = apiService.getRandomUsers();
        return response;
    }
    //查询当用用户粉丝钻石贡献榜
    public Observable<SearchFansContributionListResponse> searchFansContributionList(SearchFansContributionListRequest request){
        Observable<SearchFansContributionListResponse> response=apiService.searchFansContributionList(request.getId());
        return response;
    }
    //获取粉丝贡献榜总数
    public Observable<GetFansContributionTotalResponse> getFansContributionTotal(GetFansContributiontTotalRequest request){
        Observable<GetFansContributionTotalResponse> response=apiService.getFansContributionTotal(request.getId());
        return response;
    }
}
