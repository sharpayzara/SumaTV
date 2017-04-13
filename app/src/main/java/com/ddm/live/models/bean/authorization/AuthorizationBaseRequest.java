package com.ddm.live.models.bean.authorization;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class AuthorizationBaseRequest extends BaseRequest{
    public AuthorizationBaseRequest() {
        setInterfaceServiceName(Constants.AUTHORIZATION_API_SERVICE_NAME);
    }
}
