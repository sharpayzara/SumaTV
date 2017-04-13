package com.ddm.live.models.bean.streams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxx on 2016/8/15.
 */
public class GetStreamsListRequest extends StreamsBaseRequest{
    Map<String,Integer> queryMap=new HashMap<>();
    private int page;

    public GetStreamsListRequest(int page) {
        this.page=page;
        queryMap.put("page",page);
        setMethodName("getStreamsList");
    }

    public Map<String, Integer> getQueryMap() {
        return queryMap;
    }
}
