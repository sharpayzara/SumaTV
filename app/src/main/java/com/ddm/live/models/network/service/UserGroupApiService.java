package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.usergroup.GetUserListByGroupRequest;
import com.ddm.live.models.bean.usergroup.GetUserListByGroupResponse;
import com.ddm.live.models.network.api.UserGroupApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 2016/8/10.
 */
public class UserGroupApiService extends BaseService {
    UserGroupApi userGroupApi;
    RetrofitApiManager retrofitApiManager;
    public UserGroupApiService() {
        retrofitApiManager = new RetrofitApiManager();
        userGroupApi = retrofitApiManager.provideWithHeaderApi().create(UserGroupApi.class);
    }

    /**
     * 根据用户组查询用户列表
     */
    public Observable<GetUserListByGroupResponse> getUserListByUserGroup(GetUserListByGroupRequest request) {
        Observable<GetUserListByGroupResponse> response = userGroupApi.getUserListByUserGroup();
        return response;
    }
}
