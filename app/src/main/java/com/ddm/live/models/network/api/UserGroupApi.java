package com.ddm.live.models.network.api;


import com.ddm.live.models.bean.usergroup.GetUserListByGroupResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public interface UserGroupApi {

    //UserGroup_跟剧组查询用户列表
    @GET("user_groups")
    Observable<GetUserListByGroupResponse> getUserListByUserGroup();
}
