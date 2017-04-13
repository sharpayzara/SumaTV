package com.ddm.live.presenter;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.version.UpdateVersionRequest;
import com.ddm.live.models.bean.version.UpdateVersionResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IGetUpdateVersionView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxx on 2016/10/20.
 */
public class GetNewVersionInfoPresenter extends BasePresenter {
    IGetUpdateVersionView iGetUpdateVersionView;

    public void attachView(IGetUpdateVersionView iGetUpdateVersionView) {
        this.iGetUpdateVersionView = iGetUpdateVersionView;
    }

    public void getNewVerwsionInfo() {
        UpdateVersionRequest request = new UpdateVersionRequest();
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
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
                            print("获取账户信息失败:" + errorMessage + ":" + errorStatusCode);
                                iGetUpdateVersionView.onfailed(errorMessage);
                        } else {
                            iGetUpdateVersionView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        UpdateVersionResponse updateVersionResponse = (UpdateVersionResponse) response;
                        iGetUpdateVersionView.getNewVersionInfo(updateVersionResponse.getVersionbean());
                    }
                });
    }
}
