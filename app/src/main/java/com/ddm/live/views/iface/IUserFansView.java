package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.fansbeans.FansBean;
import com.ddm.live.models.bean.common.fansbeans.MasterBean;

import java.util.List;

/**
 * Created by JiGar on 2016/10/11.
 */
public interface IUserFansView extends IBaseView {

    /**
     * 获取关注用户列表
     *
     * @param userInfoList 关注用户信息
     */
    public void getFocusUserInfo(List<MasterBean> userInfoList);

    /**
     * 获取粉丝用户列表
     *
     * @param userInfoList 粉丝用户信息
     */
    public void getFansUserInfo(List<FansBean> userInfoList);
}
