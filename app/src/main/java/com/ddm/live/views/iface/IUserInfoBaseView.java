package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;

import java.util.List;

/**
 * Created by cxx on 2016/10/23.
 */
public interface IUserInfoBaseView extends IBaseView{
    /**
     * 获取用户详细信息
     *
     * @param userInfoArrayList 用户详细信息
     */
    public void getFansContributionInfoResult(final List<FansContributionInfoBean> fansContributionInfoBeanList,String total);
    public void getUserInfoResult(final List<UserInfoBean> userInfoBeen);
    public void getSpecUserInfo(final SpecUserInfoBean specUserInfoBean);

}
