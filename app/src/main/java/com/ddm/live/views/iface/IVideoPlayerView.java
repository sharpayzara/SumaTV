package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;

import java.util.List;

import io.rong.imlib.model.ChatRoomInfo;

/**
 * Created by wsheng on 16/1/24.
 * 直播界面
 */

public interface IVideoPlayerView extends IBaseView {

    /**
     * 关注接口结果
     */
    public void focusResult(String result, String type);

    /**
     * 举报接口
     */
    public void reportResult(String result);

    /**
     * 获取聊天室内用户id
     *
     * @param userArray 用户ID数组
     */
    public void getChatRoomUser(ChatRoomInfo userArray);

    /**
     * 获取用户详细信息
     *
     * @param userInfoArrayList 用户详细信息
     */
    public void getUserInfoResult(List<SpecUserInfoBean> userInfoArrayList,int code);

    /**
     * 退出聊天室
     */
    public void quitRoomResult(String result);

    /**
     * 查看关注状态
     */
    public void queryFocusStatusResult(Boolean result,int code);

    /**
     * 取消关注
     */
    public void cancelFocusMaster();

    /**
     * 关注主播
     */
    public void focusMaster();

//    /**
//     * 查询用户余额是否足够
//     * @param isPay
//     * @param accountList
//     */
//    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList);
//
//    public void isPaySuccess(boolean isSuc);
}
