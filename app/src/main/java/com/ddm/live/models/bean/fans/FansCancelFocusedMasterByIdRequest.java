package com.ddm.live.models.bean.fans;

/**
 * Created by cxx on 2016/8/15.
 */
public class FansCancelFocusedMasterByIdRequest extends FansBaseRequest{
    private Integer id;
    public FansCancelFocusedMasterByIdRequest(Integer id) {

        setMethodName("cancelFocusedMasterById");
        this.id=id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
