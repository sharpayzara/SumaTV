package com.ddm.live.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ddm.live.models.bean.msgtype.LiveContentMessage;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserListMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;
import com.ddm.live.views.iface.IRongYunView;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import rx.Observable;
import rx.Subscriber;

public class RongYunListenerContext {

    private static final String TAG = "DemoContext";

    private static RongYunListenerContext self;

    private SharedPreferences sharedPreferences;

    public Context mContext;

    public RongIMClient mRongIMClient;

    public String userId;

    private IRongYunView iView;


    public static RongYunListenerContext getInstance() {
        if (self == null) {
            self = new RongYunListenerContext();
        }

        return self;
    }

    public RongYunListenerContext() {
    }

    public RongYunListenerContext(Context context) {
        self = this;
    }

    public void init(Context context) {

        mContext = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setRongIMClient(RongIMClient rongIMClient) {
        mRongIMClient = rongIMClient;
    }

    public void registerReceiveMessageListener() {

        mRongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);
    }


    RongIMClient.OnReceiveMessageListener onReceiveMessageListener = new RongIMClient.OnReceiveMessageListener() {

        @Override
        public boolean onReceived(Message message, int left) {

            if (message.getContent() instanceof LiveUserMessage) {
                final LiveUserMessage liveUserMessage = (LiveUserMessage) message.getContent();
                Observable<LiveUserMessage> messageObservable = Observable.create(new Observable.OnSubscribe<LiveUserMessage>() {
                    @Override
                    public void call(Subscriber<? super LiveUserMessage> subscriber) {
                        subscriber.onNext(liveUserMessage);
                    }
                });
                if (iView != null) {
                    iView.onDrawLiveUserMessage(messageObservable);
                }

            } else if (message.getContent() instanceof LiveGiftMessage) {
                final LiveGiftMessage liveGiftMessage = (LiveGiftMessage) message.getContent();
                Observable<LiveGiftMessage> messageObservable = Observable.create(new Observable.OnSubscribe<LiveGiftMessage>() {
                    @Override
                    public void call(Subscriber<? super LiveGiftMessage> subscriber) {
                        subscriber.onNext(liveGiftMessage);
                    }
                });
                if (iView != null) {
                    iView.onDrawLiveGiftMessage(messageObservable);
                }

            } else if (message.getContent() instanceof LiveStatusMessage) {
                final LiveStatusMessage liveStatusMessage = (LiveStatusMessage) message.getContent();
                Observable<LiveStatusMessage> messageObservable = Observable.create(new Observable.OnSubscribe<LiveStatusMessage>() {
                    @Override
                    public void call(Subscriber<? super LiveStatusMessage> subscriber) {
                        subscriber.onNext(liveStatusMessage);
                    }
                });
                if (iView != null) {
                    iView.onDrawLiveStatusMessage(messageObservable);
                }
            } else if (message.getContent() instanceof LiveContentMessage) {
                final LiveContentMessage liveContentMessage = (LiveContentMessage) message.getContent();
                Observable<LiveContentMessage> messageObservable = Observable.create(new Observable.OnSubscribe<LiveContentMessage>() {
                    @Override
                    public void call(Subscriber<? super LiveContentMessage> subscriber) {
                        subscriber.onNext(liveContentMessage);
                    }
                });
                if (iView != null) {
                    iView.onDrawLiveContentMessage(messageObservable);
                }
            }else if(message.getContent() instanceof LiveUserListMessage){
                final LiveUserListMessage liveUserListMessage=(LiveUserListMessage) message.getContent();
                Observable<LiveUserListMessage> messageObservable=Observable.create(new Observable.OnSubscribe<LiveUserListMessage>() {
                    @Override
                    public void call(Subscriber<? super LiveUserListMessage> subscriber) {
                        subscriber.onNext(liveUserListMessage);
                    }
                });
                if(iView!=null){
                    iView.onDrawLiveUserListMessage(messageObservable);
                }
            }
            return false;
        }

    };


    public void attachIView(IRongYunView iView) {
        this.iView = iView;
    }


}