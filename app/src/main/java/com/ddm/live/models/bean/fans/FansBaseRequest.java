package com.ddm.live.models.bean.fans;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class FansBaseRequest extends BaseRequest{

    public FansBaseRequest() {
        setInterfaceServiceName(Constants.FANS_API_SERVICE_NAME);
    }
}
