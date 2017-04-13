package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenResponse;
import com.ddm.live.models.bean.users.GetFansContributionTotalResponse;
import com.ddm.live.models.bean.users.GetRandomUsersResponse;
import com.ddm.live.models.bean.users.GetSpecUserInfoRequest;
import com.ddm.live.models.bean.users.GetSpecUserInfoResponse;
import com.ddm.live.models.bean.users.ModifySpecUserGenderRequest;
import com.ddm.live.models.bean.users.ModifySpecUserHeaderRequest;
import com.ddm.live.models.bean.users.ModifySpecUserInfoRequest;
import com.ddm.live.models.bean.users.ModifySpecUserInfoResponse;
import com.ddm.live.models.bean.users.ModifySpecUserNameRequest;
import com.ddm.live.models.bean.users.ModifySpecUserSignRequest;
import com.ddm.live.models.bean.users.QueryPagingInfoResponse;
import com.ddm.live.models.bean.users.SearchFansContributionListResponse;
import com.ddm.live.models.bean.users.SearchUsersByKeywordRequest;
import com.ddm.live.models.bean.users.SearchUsersByKeywordResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public interface UsersApi {

    //(1)Users_分页信息查询
    @GET("users?include=posts")
    Observable<QueryPagingInfoResponse> queryPagingInfo();

    //(2)获取指定用户信息
    @POST("users/show_users")
    Observable<GetSpecUserInfoResponse> getSpecUserInfo(@Body GetSpecUserInfoRequest body);

    //(3)修改指定用户信息
    @PUT("users/update")
    Observable<ModifySpecUserInfoResponse> modifySpecUserInfo(@Body ModifySpecUserInfoRequest body);

    //(3)修改指定用户头像
    @PUT("users/update")
    Observable<ModifySpecUserInfoResponse> modifySpecUserHeader(@Body ModifySpecUserHeaderRequest body);

    //(3)修改指定用户昵称
    @PUT("users/update")
    Observable<ModifySpecUserInfoResponse> modifySpecUserName(@Body ModifySpecUserNameRequest body);

    //(3)修改指定用户昵称
    @PUT("users/update")
    Observable<ModifySpecUserInfoResponse> modifySpecUserGender(@Body ModifySpecUserGenderRequest body);

    //(3)修改指定用户签名
    @PUT("users/update")
    Observable<ModifySpecUserInfoResponse> modifySpecUserSign(@Body ModifySpecUserSignRequest body);

    //(4)根据关键字搜索用户
    @POST("users/search")
    Observable<SearchUsersByKeywordResponse> searchUsersByKeyword(@Body SearchUsersByKeywordRequest body);

    //通过token获取当前用户信息
    @GET("userinfo?include=account_types")
    Observable<GetCurrentUserInfoByTokenResponse> getCurrentUserInfoByToken();

    //通过token获取当前用户信息
    @GET("users/random_users")
    Observable<GetRandomUsersResponse> getRandomUsers();

    //查询当用用户粉丝钻石贡献榜
    @GET("bd/all/{id}")
    Observable<SearchFansContributionListResponse> searchFansContributionList(@Path("id") Integer id);


    //查询当用用户粉丝钻石贡献榜
    @GET("bd/total/{id}")
    Observable<GetFansContributionTotalResponse> getFansContributionTotal(@Path("id") Integer id);
}
