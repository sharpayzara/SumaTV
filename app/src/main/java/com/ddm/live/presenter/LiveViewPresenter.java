package com.ddm.live.presenter;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoRequest;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/14.
 */

public class LiveViewPresenter {
    LiveViewListener listener;
    public void attachView(LiveViewListener listener){
        this.listener=listener;
    }
    public void getStreamStatus(final String qnId) {
        GetSpecStreamInfoRequest request = new GetSpecStreamInfoRequest(qnId);
        RequestInterface requestInterface = new RequestInterface();
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                            listener.onfailed();
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetSpecStreamInfoResponse specStreamInfoResponse = (GetSpecStreamInfoResponse) response;
                       listener.getStreamStatus(specStreamInfoResponse.getData().getStatus());
                    }
                });
    }
    public interface LiveViewListener{
        void getStreamStatus(Integer status);
        void onfailed();
    }
}
