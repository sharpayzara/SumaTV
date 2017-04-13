package com.ddm.live.models.bean.account;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class AccountBaseRequest extends BaseRequest{
    public AccountBaseRequest() {
        setInterfaceServiceName(Constants.ACCOUNT_API_SERVICE_NAME);
    }
}
