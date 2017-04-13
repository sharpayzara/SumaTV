package com.ddm.live.presenter;

import android.util.Log;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.fans.FansCancelFocusedMasterByIdRequest;
import com.ddm.live.models.bean.fans.FansFocusOnMasterByIDRequest;
import com.ddm.live.models.bean.users.GetSpecUserInfoRequest;
import com.ddm.live.models.bean.users.GetSpecUserInfoResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IStreamBaseView;
import com.ddm.live.views.iface.IVideoPlayerView;

import io.rong.imlib.RongIMClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsheng on 16/1/24.
 */
public class VideoPlayerPresenter extends BasePresenter {
    //配置信息
    public static final String TOKEN = "iE8FnsJDRPZa8zN6m3RQFV4PgRGJ4lryRR/tEnSS1d369tkxMOvibuU0SGJSZ+Q4Cp4acj43FRUPuryfFtVhkw==";//12345
    public static RongIMClient mRongIMClient;
    private String mUserIdTest = "48918";//

    //成员变量
    private LocationApiService locationApiService;
    private IVideoPlayerView iView;
    private IStreamBaseView iStreamBaseView;

    public VideoPlayerPresenter(LocationApiService locationApiService) {
        this.locationApiService = locationApiService;
    }

    public void attachView(IVideoPlayerView videoPlayerView) {
        this.iView = videoPlayerView;
    }

    public void attachLiveView(IStreamBaseView streamBaseView) {
        this.iStreamBaseView = streamBaseView;
    }

    public void initClient() {
        try {

            mRongIMClient = RongIMClient.connect(TOKEN, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.e(TAG, "--connect--onTokenIncorrect-------");
                }

                @Override
                public void onSuccess(String userId) {
                    Log.d(TAG, "--connect--onSuccess----userId---" + userId);
                    /*mUserId = userId;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VideoPlayerActivity.this, "连接成功！", Toast.LENGTH_LONG).show();
                            try {
                                RongIMClient.registerMessageType(AgreedFriendRequestMessage.class);

                            } catch (AnnotationNotFoundException e) {
                                e.printStackTrace();
                            }
                            connectButton.setText("连接服务器成功!");

                        }
                    });*/
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e(TAG, "--connect--errorCode-------" + errorCode);
                    /*runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(VideoPlayerActivity.this, "连接失败！", Toast.LENGTH_LONG).show();
                        }
                    });*/
                }
            });

//            DemoContext.getInstance().setRongIMClient(mRongIMClient);
//            DemoContext.getInstance().registerReceiveMessageListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param aid 被关注主播id
     */
    public void userCancelFocusMaster(Integer aid) {
        RequestInterface requestInterface = new RequestInterface();
        final FansCancelFocusedMasterByIdRequest request = new FansCancelFocusedMasterByIdRequest(aid);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("取消关注失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
//                            print("取消关注成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
//                            print("取消关注无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        iView.cancelFocusMaster();
                    }
                });
    }

    public void userFocusMaster(Integer[] ids) {
        RequestInterface requestInterface = new RequestInterface();
        FansFocusOnMasterByIDRequest request = new FansFocusOnMasterByIDRequest(ids);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("关注主播失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        iView.focusMaster();
                    }
                });
    }

    /**
     * 观看直播时查询关注状态
     *
     * @param ids
     */
    public void queryFocusedStatus(Integer[] ids,final int code) {
        RequestInterface requestInterface = new RequestInterface();
        final GetSpecUserInfoRequest request = new GetSpecUserInfoRequest(ids);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                          print("查询用户信息以获取关注状态" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
//                            print("查询用户信息以获取关注状态:" + response.getClass().getName() + ":" + gson.toJson(response));
                            GetSpecUserInfoResponse specUserInfoResponse = (GetSpecUserInfoResponse) response;

                            boolean result = specUserInfoResponse.getData().get(0).getFans().getData().getIsFollowed();
                            iView.queryFocusStatusResult(result,code);
                        } catch (NullPointerException e) {
//                            print("关注主播无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                    }
                });
    }


    //获取指定用户信息
    public void getSpecLiveUserInfoById(Integer[] userIds, final int code) {
        RequestInterface requestInterface = new RequestInterface();
        final GetSpecUserInfoRequest request = new GetSpecUserInfoRequest(userIds);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
                            print("业务层:" + errorMessage + ":" + errorStatusCode);
                            iStreamBaseView.onfailed(errorMessage);
                        } else {
                            iStreamBaseView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetSpecUserInfoResponse userInfoResponse = (GetSpecUserInfoResponse) response;
                        iStreamBaseView.getStreamUserInfoResult(userInfoResponse.getData(), code);
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                    }
                });
    }

    //获取指定用户信息
    public void getSpecUserInfoByIdPlay(Integer[] userIds, final int code) {
        RequestInterface requestInterface = new RequestInterface();
        GetSpecUserInfoRequest request = new GetSpecUserInfoRequest(userIds);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("业务层:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetSpecUserInfoResponse userInfoResponse = (GetSpecUserInfoResponse) response;
                        iView.getUserInfoResult(userInfoResponse.getData(), code);
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                    }
                });
    }

}
