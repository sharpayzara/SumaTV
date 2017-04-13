package com.ddm.live.models.bean.rechargeinfo;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class RechargeInfoBaseRequest extends BaseRequest{
    public RechargeInfoBaseRequest() {
        setInterfaceServiceName(Constants.RECHARGEINFO_API_SERVICE_NAME);
    }
}
