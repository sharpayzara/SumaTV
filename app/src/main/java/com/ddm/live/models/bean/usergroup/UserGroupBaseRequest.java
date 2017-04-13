package com.ddm.live.models.bean.usergroup;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class UserGroupBaseRequest extends BaseRequest{
    public UserGroupBaseRequest() {
        setInterfaceServiceName(Constants.USERGROUP_API_SERVICE_NAME);
    }
}
