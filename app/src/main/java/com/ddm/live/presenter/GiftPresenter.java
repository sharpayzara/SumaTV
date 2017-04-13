package com.ddm.live.presenter;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.gift.GetGiftsListRequest;
import com.ddm.live.models.bean.gift.GetGiftsListResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IGiftListView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxx on 2016/11/17.
 */

public class GiftPresenter extends BasePresenter{
IGiftListView iGiftListView;
    public void attachIGiftListView(IGiftListView iGiftListView){
        this.iGiftListView=iGiftListView;
    }
    /**
     * 获取礼物列表
     */
    public void getGiftList() {
        RequestInterface requestInterface = new RequestInterface();
        GetGiftsListRequest request= new GetGiftsListRequest();
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
                            iGiftListView.onfailed(errorMessage);
                        } else {
                           iGiftListView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetGiftsListResponse getGiftsListResponse = (GetGiftsListResponse) response;
                       iGiftListView.getGiftList(getGiftsListResponse.getData());
                    }
                });
    }
}
