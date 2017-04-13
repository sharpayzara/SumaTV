package com.ddm.live.models.bean.streams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxx on 2016/8/11.
 */
public class GetStreamsByUserIDRequest extends StreamsBaseRequest{
    @Expose
    @SerializedName("user_id")
    private Integer userId;
    private int page;
    Map<String,Integer> pageMap=new HashMap<>();
    public GetStreamsByUserIDRequest(Integer userId,int page) {
        setMethodName("getStreamsByUserId");
        this.userId = userId;
        this.page=page;
        pageMap.put("page",page);
    }

    public Map<String, Integer> getPageMap() {
        return pageMap;
    }
}
