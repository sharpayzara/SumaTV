package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.versionbeans.Versionbean;

/**
 * Created by cxx on 2016/10/20.
 */
public interface IGetUpdateVersionView extends IBaseView{
    public void getNewVersionInfo(Versionbean versionbean);
    public void updateNewVersion();
}
