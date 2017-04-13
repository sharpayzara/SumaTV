package com.ddm.live.models.bean.streams;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by cxx on 2016/8/26.
 */
public class StreamsBaseRequest extends BaseRequest{
    public StreamsBaseRequest() {
        setInterfaceServiceName(Constants.STREAMS_API_SERVICE_NAME);
    }
}
