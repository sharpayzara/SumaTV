package com.ddm.live.models.bean.file;

import com.ddm.live.models.Constants;
import com.ddm.live.models.bean.basebean.request.BaseRequest;

/**
 * Created by JiGar on 2016/11/3.
 */
public class FileBaseRequest extends BaseRequest {
    public FileBaseRequest() {
        setInterfaceServiceName(Constants.FILE_MANAGE_API_SERVICE_NAME);
    }
}
