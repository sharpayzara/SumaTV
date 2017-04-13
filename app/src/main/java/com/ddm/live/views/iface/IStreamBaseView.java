package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;

import java.util.List;

import io.rong.imlib.model.ChatRoomInfo;

/**
 * Created by JiGar on 2016/1/29.
 */
public interface IStreamBaseView extends IBaseView{

    /**
     * 获取聊天室内用户id
     *
     * @param userArray 用户ID数组
     */
    public void getStreamChatRoomUser(ChatRoomInfo userArray);

    /**
     * 获取用户详细信息
     *
     * @param userInfoArrayList 用户详细信息
     */
    public void getStreamUserInfoResult(List<SpecUserInfoBean> userInfoArrayList,final int code);

    /**
     * 退出聊天室
     */
    public void quitStreamRoomResult(String result);

    /**
     * 修改观看流的观看人数结果
     */
    public void modifyWatchedNumResult();

    /**
     * 修改流状态为结束
     */
    public void modifyStreamStateAsLive(boolean streamState);
}
