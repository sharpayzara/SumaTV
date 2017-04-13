package com.ddm.live.models.bean.fans;

/**
 * Created by cxx on 2016/8/15.
 */
public class FansGetFansListRequest extends  FansBaseRequest {
    private Integer page;
    public FansGetFansListRequest(Integer page) {
        setMethodName("getFansList");
        this.page=page;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
