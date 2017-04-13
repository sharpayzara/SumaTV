package com.ddm.live.models.bean.streams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiGar on 2016/10/24.
 */
public class GetMasterLiveListRequest extends StreamsBaseRequest {
    private int page;
    private Map<String,Integer> pageMap=new HashMap<>();
    public GetMasterLiveListRequest(int page) {
        setMethodName("getMasterLiveList");
        this.page=page;
        pageMap.put("page",page);
    }

    public Map<String, Integer> getPageMap() {
        return pageMap;
    }
}
