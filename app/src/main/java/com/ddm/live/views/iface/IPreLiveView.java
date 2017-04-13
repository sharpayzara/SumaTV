package com.ddm.live.views.iface;

/**
 * Created by wsheng on 16/1/13.
 */
public interface IPreLiveView extends IBaseView{

    /**
     * 显示地址
     * @param address   现实物理地址
     */
    public void setLocation(String address);

    /**
     * 创建流结束
     * @param errorCode    状态代码
     * @param stream    七牛返回内容
     * @param id        流ID
     * @param qid       七牛ID
     */

    public void onFinishCreateStream(String streamJson,String streamId,String roomId,String qnId);



}
