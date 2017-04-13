package com.ddm.live.models.bean.streams;

/**
 * Created by Administrator on 2017/2/24.
 */

public class ModifyStreamStateAsLiveRequest extends StreamsBaseRequest {
    private String qnId;
    public ModifyStreamStateAsLiveRequest(String qnId) {
        setMethodName("modifyStreamStateAsLive");
        this.qnId = qnId;
    }

    public void setQnId(String qnId) {
        this.qnId = qnId;
    }

    public String getQnId() {
        return qnId;
    }
}
