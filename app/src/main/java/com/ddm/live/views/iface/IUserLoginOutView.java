package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;

/**
 * Created by JiGar on 2016/1/13.
 */
public interface IUserLoginOutView {

    /**
     * 获取关注或粉丝用户信息

     */
    public void getFocusOrFansInfo(SpecUserInfoBean userInfo);

}
