package com.ddm.live.models.bean.fans;

/**
 * Created by cxx on 2016/8/15.
 */
public class FansGetFocusedMastersListRequest extends  FansBaseRequest {
    private Integer page;
    public FansGetFocusedMastersListRequest(Integer page) {
        setMethodName("getFocusedMastersList");
        this.page=page;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
