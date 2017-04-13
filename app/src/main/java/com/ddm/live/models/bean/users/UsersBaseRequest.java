package com.ddm.live.models.bean.users;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class UsersBaseRequest extends BaseRequest {
    public UsersBaseRequest() {
        setInterfaceServiceName(Constants.USERS_API_SERVICE_NAME);
    }
}
