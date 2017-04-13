package com.ddm.live.presenter;

import android.util.Log;

import com.ddm.live.models.bean.address.Address;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.streams.CreateStreamsRequest;
import com.ddm.live.models.bean.streams.CreateStreamsResponse;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsLiveRequest;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsLiveResponse;
import com.ddm.live.models.bean.streams.ModifyStreamWatchedNumberRequest;
import com.ddm.live.models.bean.streams.ModifyStreamWatchedNumberResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IPreLiveView;
import com.ddm.live.views.iface.IStreamBaseView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsheng on 16/1/19.
 */
public class StartLivePresenter extends BasePresenter {
    private LocationApiService locationApiService;
    private IPreLiveView iView;
    private IStreamBaseView iStreamBaseView;

    public void attachIView(IPreLiveView iView) {
        this.iView = iView;
    }

    public void attachIStreamBaseView(IStreamBaseView iStreamBaseView) {
        this.iStreamBaseView = iStreamBaseView;
    }

    public StartLivePresenter(LocationApiService locationApiService) {
        this.locationApiService = locationApiService;
    }

    public void getBaiduLocation(String location) {
        locationApiService.getAddress(location)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Address>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                        iView.setLocation("未知地理位置");
                    }

                    @Override
                    public void onNext(Address address) {
                        iView.setLocation(address.getResult().getAddressComponent().getCity());
                    }
                });
    }

    public void createStream2(String subject, String title, String address) {
        RequestInterface requestInterface = new RequestInterface();
        CreateStreamsRequest request = new CreateStreamsRequest(subject, title, address);
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
//                            print("业务层直播流创建失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {

//                        System.out.println("直播流创建成功");
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        CreateStreamsResponse streamsResponse = (CreateStreamsResponse) response;
                        SpecStreamBean streamsBean = streamsResponse.getData();
                        iView.onFinishCreateStream(streamsBean.getStreamJson(), streamsBean.getStreamId(),
                                streamsBean.getRoomId(), String.valueOf(streamsBean.getId()));
                    }
                });
    }


    /**
     * 修改观看流的观看人数
     *
     * @param streamID 流ID
     * @param num      观看人数
     */
    public void modifyWatchedNum(String streamID, String num) {
        RequestInterface requestInterface = new RequestInterface();
        ModifyStreamWatchedNumberRequest request = new ModifyStreamWatchedNumberRequest(streamID, num);
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
//                            print("修改观看流的观看人数:" + errorMessage + ":" + errorStatusCode);
//                            iStreamBaseView.onfailed(errorMessage);
                        } else {
                            iStreamBaseView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        ModifyStreamWatchedNumberResponse streamsResponse = (ModifyStreamWatchedNumberResponse) response;
                        iStreamBaseView.modifyWatchedNumResult();
                    }
                });
    }

    /**
     * 修改流状态为直播状态
     *
     * @param qnId
     */
    public void modifyStreamStateAsLive(String qnId) {
        RequestInterface requestInterface = new RequestInterface();
        ModifyStreamStateAsLiveRequest request=new ModifyStreamStateAsLiveRequest(qnId);
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
                        } else {
                            iStreamBaseView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {

                        ModifyStreamStateAsLiveResponse streamsResponse = (ModifyStreamStateAsLiveResponse) response;
                        iStreamBaseView.modifyStreamStateAsLive(true);

                    }
                });
    }
}
