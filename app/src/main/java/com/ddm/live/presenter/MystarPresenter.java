package com.ddm.live.presenter;

import com.ddm.live.activities.MyStarActivity;
import com.ddm.live.models.bean.account.GetExchangeListRequest;
import com.ddm.live.models.bean.account.GetExchangeListResponse;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IMystarView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ytzys on 2017/2/22.
 */
public class MystarPresenter extends BasePresenter {
    IMystarView imystarView;

    public void attachView(IMystarView imystarView) {
        this.imystarView = imystarView;

    }

    public void getExchangeListInfo() {
        imystarView.getExchargeListInfoResponse(null);
        return;
//        final GetExchangeListRequest request = new GetExchangeListRequest();
//        RequestInterface requestInterface = new RequestInterface();
//        requestInterface.sendRequest2(request)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<ResponseBaseInterface>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
//                        if (null != errorResponseBean) {
//                            String errorMessage = errorResponseBean.getMessage();
//                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("查询兑换列表失败:" + errorMessage + ":" + errorStatusCode);
//                            imystarView.onfailed(errorMessage);
//                        } else {
//                            imystarView.onfailed("网络异常，请稍后再试");
//                        }
//                    }
//
//                    @Override
//                    public void onNext(ResponseBaseInterface response) {
//                        try {
//                            print("查询兑换列表成功:" + response.getClass().getName() + ":" + gson.toJson(response));
//                        } catch (NullPointerException e) {
//                            print("无返回信息，或检查接口url是否更改");
//                        } catch (Exception e) {
//                            print(e.toString());
//                        }
//                        imystarView.getExchargeListInfoResponse(((GetExchangeListResponse) response).getData());
//                    }
//                });
    }


}
