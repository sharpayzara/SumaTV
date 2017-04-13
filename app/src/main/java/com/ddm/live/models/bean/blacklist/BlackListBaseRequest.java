package com.ddm.live.models.bean.blacklist;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class BlackListBaseRequest extends BaseRequest{
    public BlackListBaseRequest() {
        setInterfaceServiceName(Constants.BLACKLIST_API_SERVICE_NAME);
    }
}
