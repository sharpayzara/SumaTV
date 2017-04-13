package com.ddm.live.views.iface;

/**
 * Created by Administrator on 2016/11/29.
 */

public interface IPreliveLayerView extends IBaseView{
    //翻转摄像头
    public void preStartLiveSwitchCamera();
    //开始直播
    public void startLive(String subject,String title,String address,int radioId);

}
