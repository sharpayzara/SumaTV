package com.ddm.live.presenter;

import com.ddm.live.models.bean.account.BuyVirtualCoinsRequest;
import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.GetRechargeListRequest;
import com.ddm.live.models.bean.account.GetRechargeListResponse;
import com.ddm.live.models.bean.account.PresentVirtualCoinsRequest;
import com.ddm.live.models.bean.account.QueryChargeInfoListRequest;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.accountbeans.AccountTypesDataBean;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenRequest;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenResponse;
import com.ddm.live.models.bean.users.GetFansContributionTotalResponse;
import com.ddm.live.models.bean.users.GetFansContributiontTotalRequest;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IAccountView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxx on 2016/8/15.
 * 测试用
 */
public class AccountPresenter extends BasePresenter {

    private IAccountView iAccountView;

    public void attachView(IAccountView iAccountView) {
        this.iAccountView = iAccountView;
    }

    /**
     * 1、获取账户信息
     */

    public void getAccountInfo() {
        GetCurrentUserInfoByTokenRequest request = new GetCurrentUserInfoByTokenRequest();
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
                            iAccountView.onfailed("下单失败");

                        } else {
                            iAccountView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("获取账户信息成功:" + response.getClass().getName() + ":" + gson.toJson(response));

                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        GetCurrentUserInfoByTokenResponse accountInfoResponse = (GetCurrentUserInfoByTokenResponse) response;

                        boolean flag = accountInfoResponse.getData().getAccountTypesBean().getData().isEmpty();
                        if (flag) {
                            print("为空");
                            iAccountView.checkUserAccount(false, null);
                        } else {
                            print("不为空");
                            print("获取当前用户账户信息:" + gson.toJson(accountInfoResponse.getData().getAccountTypesBean().getData()));
                            List<AccountTypesDataBean> accountList = accountInfoResponse.getData().getAccountTypesBean().getData();
                            iAccountView.checkUserAccount(true, accountList);

                        }
//                        iView.obtainCodeResult();
                    }
                });
    }

    /**
     * 2、购买虚拟币
     */
    public void buyVirtualCoins(Integer money) {
        BuyVirtualCoinsRequest request = new BuyVirtualCoinsRequest(money);
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
                            print("购买虚拟币失败:" + errorMessage + ":" + errorStatusCode);
                            iAccountView.onfailed("购买虚拟币失败");
                        } else {
                            iAccountView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
//                        try {
//                            print("购买虚拟币成功:" + response.getClass().getName() + ":" + gson.toJson(response));
//                        } catch (NullPointerException e) {
//                            print("无返回信息，或检查接口url是否更改");
//                        } catch (Exception e) {
//                            print(e.toString());
//                        }
                        BuyVirtualCoinsResponse buyVirtualCoinsResponse = (BuyVirtualCoinsResponse) response;
//                           iAccountView.onSuccess();
                        iAccountView.onGetPrepayId(buyVirtualCoinsResponse);
                    }
                });
    }

    /**
     * 3、赠送虚拟币
     */
    public void presentVirtualCoins(Integer money, Integer id, final int position, final int type) {
        PresentVirtualCoinsRequest request = new PresentVirtualCoinsRequest(money, id);
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
                            print("赠送虚拟币失败:" + errorMessage + ":" + errorStatusCode);
                            iAccountView.onfailed("赠送虚拟币失败");

                        } else {
                            iAccountView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("赠送虚拟币成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        iAccountView.isPaySuccess(position, type);

                    }
                });
    }

    /**
     * 4、查询充值列表
     */
    public void queryRechargeInfoList(String id) {
        QueryChargeInfoListRequest request = new QueryChargeInfoListRequest(id);
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
                            iAccountView.onfailed(errorMessage);

                        } else {
                            iAccountView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("查询充值列表成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        QueryChargeInfoListResponse chargeInfoListResponse = (QueryChargeInfoListResponse) response;
                        iAccountView.onQueryRechargeRecoderList(chargeInfoListResponse);
                    }
                });
    }

    /**
     * 查询用户的粉丝总数
     *
     * @param id
     */
    public void getFansContributionTotal(Integer id) {
        GetFansContributiontTotalRequest request = new GetFansContributiontTotalRequest(id);
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
                            iAccountView.onfailed(errorMessage);
                        } else {
                            iAccountView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetFansContributionTotalResponse fansContributionListResponse = (GetFansContributionTotalResponse) response;
                        iAccountView.onGetFansContributionToatal(fansContributionListResponse.getData().getTotal());
                    }
                });
    }

    /**
     * 7、查询充值数额列表
     */
    public void getRechargeListInfo() {
        GetRechargeListRequest request = new GetRechargeListRequest();
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
                            print("查询充值数值列表失败:" + errorMessage + ":" + errorStatusCode);
                            iAccountView.onfailed(errorMessage);
                        } else {
                            iAccountView.onfailed("网络异常，请稍后再试");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("查询充值列表成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        GetRechargeListResponse getRechargeListResponse = (GetRechargeListResponse) response;
                        iAccountView.getRechargeListInfoResponse(getRechargeListResponse.getData());
                    }
                });
    }
}
