package com.ddm.live.models.bean.gift;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class GiftBaseRequest extends BaseRequest {
    public GiftBaseRequest() {
        setInterfaceServiceName(Constants.GIFT_API_SERVICE_NAME);
    }
}
