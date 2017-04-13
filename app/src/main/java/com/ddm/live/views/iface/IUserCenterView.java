package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;

/**
 * Created by JiGar on 2016/1/13.
 */
public interface IUserCenterView extends IBaseView {

    /**
     * 修改用户信息结果返回
     */
    public void changeResult();

    /**
     * 获取用户详细信息
     * @param userInfo  用户信息
     */
    public void getUserInfo(SpecUserInfoBean userInfo);

    /**
     * 添加关注结果
     * @param result    结果
     */
    public void addFocusResult(boolean result, String msg);

    /**
     * 取消关注结果
     * @param result    结果
     */
    public void cancelFocusResult(boolean result, String msg);

    /**
     * 获取七牛上传图片token
     * @param token 服务器token
     * @param host  返回host
     */
    public void getUploadToken(String token, String host);
}
