package com.ddm.live.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.basebean.request.BaseRequest;
import com.ddm.live.models.bean.msgtype.LiveContentMessage;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserListMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.utils.RongYunListenerContext;
import com.ddm.live.views.iface.IStreamBaseView;
import com.ddm.live.views.iface.IVideoPlayerView;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by wsheng on 16/1/24.
 */
public class RongYunPresenter extends BasePresenter {

    //    public String TOKEN = "x94eKdw8VEH4ObFmZ/BZMa1vR4FohHH1FahVtbdpNRU/FetwaG1rLU+zYUfgbPEk0ahQ8P42qN4=";//12345
//    public String TOKEN = "G5esq3MkcxZwygkCF1EXFih1TQrwIBZ9Sj0/jWBXe2M0ztBuanpi+nPhRkM2ZHd+JnQ4aKXKIML0Vv7q3027Nw==";//12345
    private String mUserIdTest = "6";//
    public static RongIMClient mRongIMClient;
    private IVideoPlayerView iView;
    private IStreamBaseView iStreamBaseView;
    public static boolean isInLivingRoom = false;
    private LocationApiService locationApiService;

    private AppUser appUser;


    public RongYunPresenter(LocationApiService locationApiService) {
        this.locationApiService = locationApiService;

    }

    public void attachView(IVideoPlayerView iVideoPlayerView) {
        this.iView = iVideoPlayerView;
    }

    public void attachLiveView(IStreamBaseView iStreamBaseView) {
        this.iStreamBaseView = iStreamBaseView;
    }

    /**
     * 实例化聊天对象
     */
    public Observable<RongIMClient> initConnection(final AppUser appUser, final Context context) {

        Observable<RongIMClient> observableConnection = Observable.create(new Observable.OnSubscribe<RongIMClient>() {
            @Override
            public void call(final Subscriber<? super RongIMClient> subscriber) {
                mRongIMClient = RongIMClient.connect(appUser.getRongYunToken(), new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        appUser.setIsLogined(false);
                        Toast.makeText(context, "登录失败，请重新登录", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, BFLoginActivity.class));

                    }

                    @Override
                    public void onSuccess(String userId) {
                        subscriber.onNext(mRongIMClient);
                        RongYunListenerContext.getInstance().setRongIMClient(mRongIMClient);
                        RongYunListenerContext.getInstance().registerReceiveMessageListener();
                        appUser.setIsLogined(true);
//                            RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus= RongIMClient.getInstance().getCurrentConnectionStatus();
//                        Log.d(TAG, "onSuccess: 融云连接成功:" + appUser.getRongYunToken());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(context, "请检查当前网络状况", Toast.LENGTH_SHORT).show();
                    }
                });
//                } else {
//                    subscriber.onNext(mRongIMClient);
//                }

//                RongYunListenerContext.getInstance().attachIView();
//                RongYunListenerContext.getInstance().setRongIMClient(mRongIMClient);
//                RongYunListenerContext.getInstance().registerReceiveMessageListener();
            }

        });

        return observableConnection;
    }

    public Observable<LiveUserMessage> sendLiveUserMessage(final MessageContent msg, final String chartRoomId) {

        Observable<LiveUserMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<LiveUserMessage>() {
            @Override
            public void call(final Subscriber<? super LiveUserMessage> subscriber) {
                mRongIMClient.sendMessage(Conversation.ConversationType.CHATROOM, chartRoomId, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LiveUserMessage contentMessage = (LiveUserMessage) msg;
                        subscriber.onNext(contentMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }

    //发送状态消息
    public Observable<LiveStatusMessage> sendLiveStatusMessage(final MessageContent msg, final String chartRoomId) {
        Observable<LiveStatusMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<LiveStatusMessage>() {
            @Override
            public void call(final Subscriber<? super LiveStatusMessage> subscriber) {
                mRongIMClient.sendMessage(Conversation.ConversationType.CHATROOM, chartRoomId, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LiveStatusMessage contentMessage = (LiveStatusMessage) msg;
                        subscriber.onNext(contentMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }

    //发送内容消息
    public Observable<LiveContentMessage> sendLiveContentMessage(final MessageContent msg, final String chartRoomId) {
        Observable<LiveContentMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<LiveContentMessage>() {
            @Override
            public void call(final Subscriber<? super LiveContentMessage> subscriber) {
                mRongIMClient.sendMessage(Conversation.ConversationType.CHATROOM, chartRoomId, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LiveContentMessage contentMessage = (LiveContentMessage) msg;
                        subscriber.onNext(contentMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }

    //发送礼物消息
    public Observable<LiveGiftMessage> sendLiveGiftMessage(final MessageContent msg, final String chartRoomId) {

        Observable<LiveGiftMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<LiveGiftMessage>() {
            @Override
            public void call(final Subscriber<? super LiveGiftMessage> subscriber) {
                mRongIMClient.sendMessage(Conversation.ConversationType.CHATROOM, chartRoomId, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LiveGiftMessage contentMessage = (LiveGiftMessage) msg;
                        subscriber.onNext(contentMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }


    //发送观众列表
    public Observable<LiveUserListMessage> sendLiveUserListMessage(final Conversation.ConversationType conversationType,final MessageContent msg, final String id) {
        Observable<LiveUserListMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<LiveUserListMessage>() {
            @Override
            public void call(final Subscriber<? super LiveUserListMessage> subscriber) {
                mRongIMClient.sendMessage(conversationType, id, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LiveUserListMessage contentMessage = (LiveUserListMessage) msg;
                        subscriber.onNext(contentMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }

    public Observable<LiveStatusMessage> sendMessage4(final MessageContent msg, final String chartRoomId) {

        Observable<LiveStatusMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<LiveStatusMessage>() {
            @Override
            public void call(final Subscriber<? super LiveStatusMessage> subscriber) {
                mRongIMClient.sendMessage(Conversation.ConversationType.CHATROOM, chartRoomId, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        LiveStatusMessage contentMessage = (LiveStatusMessage) msg;
                        subscriber.onNext(contentMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }

    /**
     * 发送聊天室消息
     *
     * @param msg
     */
    public Observable<TextMessage> sendMessage(final MessageContent msg, final String chartRoomId) {

        Observable<TextMessage> sendMessageObservable = Observable.create(new Observable.OnSubscribe<TextMessage>() {
            @Override
            public void call(final Subscriber<? super TextMessage> subscriber) {
                mRongIMClient.sendMessage(Conversation.ConversationType.CHATROOM, chartRoomId, msg, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        TextMessage textMessage = (TextMessage) msg;
                        subscriber.onNext(textMessage);
//                        Log.d(TAG, "onSuccess: 发送聊天信息成功" + integer);
                    }
                });
            }
        });

        return sendMessageObservable;

    }

    /**
     * 进入聊天室
     *
     * @param chatroomId
     * @param defMessageCount
     */
    public Observable<String> joinChartRoom(final String chatroomId, final int defMessageCount) {
        Observable<String> joinChartRoomObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                mRongIMClient.joinChatRoom(chatroomId, defMessageCount, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
//                        Log.d(TAG, "onSuccess: 进入聊天室成功®:" + chatroomId);
                        isInLivingRoom = true;
                        subscriber.onNext(chatroomId);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        subscriber.onError(new Throwable(errorCode.getMessage()));
//                        Log.e(TAG, errorCode.getMessage());
//                        joinChartRoom(chatroomId, defMessageCount);
                    }
                });
            }
        });

        return joinChartRoomObservable;
    }

    /**
     * 退出聊天室
     *
     * @param chatroomId 聊天室 Id
     */
    public void quitChartRoom(final String chatroomId) {
        if (mRongIMClient != null) {
            mRongIMClient.quitChatRoom(chatroomId, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
//                    iView.quitRoomResult("true");
                    isInLivingRoom=false;
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
//                    Log.e(TAG, "onError: 退出房间失败" + errorCode);
                }
            });
        }
    }


    public void quitLiveChartRoom(String chatroomId) {
        if (mRongIMClient != null) {
            mRongIMClient.quitChatRoom(chatroomId, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
//                    iStreamBaseView.quitStreamRoomResult("true");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
//                    Log.e(TAG, "onError: 退出房间失败" + errorCode);
                }
            });
        }
    }

    /**
     * 获取聊天室用户信息
     *
     * @param chatRoomId
     * @param defMemberCount
     * @param order
     */
    public void getChatRoomMember(final String chatRoomId, final int defMemberCount, final ChatRoomInfo.ChatRoomMemberOrder order) {
        mRongIMClient.getChatRoomInfo(chatRoomId, defMemberCount, order, new RongIMClient.ResultCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(ChatRoomInfo chatRoomInfo) {
//                Log.e("onSuccess2","---获取房间用户信息---");
//                Log.d("onSuccess2","get chatroom member info:chatRoomId-"+chatRoomId);
                int totalMember = chatRoomInfo.getTotalMemberCount();
                for (int i = 0; i < totalMember; i++) {
//                  Log.d("onSuccess2","chatroom member info "+"userId:"+ chatRoomInfo.getMemberInfo().get(i).getUserId());
                }
                iView.getChatRoomUser(chatRoomInfo);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Log.e("---onError---", chatRoomId);
            }
        });
    }

    public void getLiveChatRoomMember(final String chatRoomId, final int defMemberCount, final ChatRoomInfo.ChatRoomMemberOrder order) {
        mRongIMClient.getChatRoomInfo(chatRoomId, defMemberCount, order, new RongIMClient.ResultCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(ChatRoomInfo chatRoomInfo) {
                iStreamBaseView.getStreamChatRoomUser(chatRoomInfo);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 关闭流切换流状态
     *
     * @param request
     */
    public void closeStreams(BaseRequest request) {
        RequestInterface requestInterface = new RequestInterface();
        requestInterface.sendRequest2(request)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        iStreamBaseView.quitLiveResult(false);
//                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
//                        if (null != errorResponseBean) {
//                            String errorMessage = errorResponseBean.getMessage();
//                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("关闭直播失败" + errorMessage);
//                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("关闭直播流成功");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                    }
                });
    }
}
