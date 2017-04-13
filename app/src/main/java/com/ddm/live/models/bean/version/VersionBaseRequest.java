package com.ddm.live.models.bean.version;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by Administrator on 2016/10/20.
 */
public class VersionBaseRequest extends BaseRequest {
    public VersionBaseRequest() {
        setInterfaceServiceName(Constants.VERSION_API_SERVICE_NAME);
    }
}
