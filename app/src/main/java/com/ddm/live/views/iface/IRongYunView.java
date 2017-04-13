package com.ddm.live.views.iface;

import com.ddm.live.models.bean.msgtype.LiveContentMessage;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserListMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;

import rx.Observable;

/**
 * Created by wsheng on 16/1/25.
 */
public interface IRongYunView {

    //接收系统消息回调接口
    public void onDrawLiveContentMessage(Observable<LiveContentMessage> messageObservable);

    //接收礼物消息回调接口

    public void onDrawLiveGiftMessage(Observable<LiveGiftMessage> messageObservable);

    //接收状态消息回调接口
    public void onDrawLiveStatusMessage(Observable<LiveStatusMessage> messageObservable);

    //接收用户消息回调接口

    public void onDrawLiveUserMessage(Observable<LiveUserMessage> messageObservable);

    //接收用户消息回调接口

    public void onDrawLiveUserListMessage(Observable<LiveUserListMessage> messageObservable);
}
