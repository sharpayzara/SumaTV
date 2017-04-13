package com.ddm.live.presenter;

import com.ddm.live.AppApplication;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.constants.Constants;
import com.ddm.live.models.bean.authorization.GetTokenByPwdRequest;
import com.ddm.live.models.bean.authorization.GetTokenByPwdResponse;
import com.ddm.live.models.bean.authorization.LoginByWXRequest;
import com.ddm.live.models.bean.authorization.LoginByWXResponse;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenRequest;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.ILoginView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsheng on 16/1/28.
 */
public class LoginPresenter extends BasePresenter {
    private LocationApiService locationApiService;
    private AppApplication appApplication;
    private ILoginView iView;
    final AppUser appUser;

    public LoginPresenter(LocationApiService locationApiService) {
        this.locationApiService = locationApiService;
        appApplication = AppApplication.getInstance();
        appUser = appApplication.getAppUser();
    }

    public void attachView(ILoginView iLoginView) {
        this.iView = iLoginView;
    }


    public void loginByWX() {
        //向微信发起请求,判断是否是新用户

        BFLoginActivity bfLoginActivity = (BFLoginActivity) iView;

        appApplication = AppApplication.getInstance();
        final AppUser appUser = appApplication.getAppUser();

        LoginByWXRequest request = new LoginByWXRequest("third_login", Constants.CLIENT_ID,
                Constants.CLIENT_SECRETE, 0,
                appUser.getOpenid(), appUser.getUnionid(), appUser.getHeadimgurl(),
                appUser.getProvince(), appUser.getCity(), appUser.getCountry(), appUser.getGender(),
                appUser.getAccess_token(), appUser.getRefresh_token(), appUser.getNickname());
        RequestInterface requestInterface = new RequestInterface();
        requestInterface.sendRequest2(request).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                            print("业务层微信登录失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {

                        try {
                            print("业务层微信登录成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        LoginByWXResponse loginByWXResponse=(LoginByWXResponse)response;
                        appUser.setExpires_in(String.valueOf(loginByWXResponse.getExpiresIn()));
                        appUser.setRefresh_token(loginByWXResponse.getRefreshToken());
                        appUser.setAccess_token(loginByWXResponse.getAccessToken());
                        //通过token获取当前用户信息，昵称等，获取Rongyuntoken
                        RequestInterface requestInterface = new RequestInterface();
                        GetCurrentUserInfoByTokenRequest request = new GetCurrentUserInfoByTokenRequest();
                        requestInterface.sendRequest2(request).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
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
                                            print("业务层获取融云token失败:" + errorMessage + ":" + errorStatusCode);
                                            iView.onfailed(errorMessage);
                                        } else {
                                            iView.onfailed("当前网络状况不佳");
                                        }
                                    }

                                    @Override
                                    public void onNext(ResponseBaseInterface response) {
                                        GetCurrentUserInfoByTokenResponse rongYunResponse = (GetCurrentUserInfoByTokenResponse) response;
                                        initAppUser(rongYunResponse);

                                        iView.registerResult();
                                        try {
                                            print("业务层获取融云token成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                                        } catch (NullPointerException e) {
                                            print("无返回信息，或检查接口url是否更改");
                                        } catch (Exception e) {
                                            print(e.toString());
                                        }
                                    }
                                });
                    }
                });

    }

    //密码登录
    public void login(String phoneNum, String pwd) {
        GetTokenByPwdRequest request = new GetTokenByPwdRequest("password", Constants.CLIENT_ID, Constants.CLIENT_SECRETE,
                phoneNum, pwd);
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
                            iView.onfailed("未注册或密码错误");
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {

                        try {
                            print("登录成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        GetTokenByPwdResponse pwdResponse = (GetTokenByPwdResponse) response;
                        appUser.setExpires_in(String.valueOf(pwdResponse.getExpiresIn()));
                        appUser.setRefresh_token(pwdResponse.getRefreshToken());
                        appUser.setAccess_token(pwdResponse.getAccessToken());

                        //通过token获取当前用户信息，昵称等，获取Rongyuntoken
                        RequestInterface requestInterface = new RequestInterface();
                        GetCurrentUserInfoByTokenRequest request = new GetCurrentUserInfoByTokenRequest();
                        requestInterface.sendRequest2(request).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
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
                                            print("业务层获取融云token失败:" + errorMessage + ":" + errorStatusCode);
                                            iView.onfailed(errorMessage);
                                        } else {
                                            iView.onfailed("当前网络状况不佳");
                                        }
                                    }

                                    @Override
                                    public void onNext(ResponseBaseInterface response) {
                                        GetCurrentUserInfoByTokenResponse rongYunResponse = (GetCurrentUserInfoByTokenResponse) response;
                                        initAppUser(rongYunResponse);
                                        iView.registerResult();
                                        try {
                                            print("业务层获取融云token成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                                        } catch (NullPointerException e) {
                                            print("无返回信息，或检查接口url是否更改");
                                        } catch (Exception e) {
                                            print(e.toString());
                                        }
                                    }
                                });
                    }
                });
    }

    public void initAppUser(GetCurrentUserInfoByTokenResponse rongYunResponse) {
        appUser.setId(rongYunResponse.getData().getId());
        appUser.setRongYunToken(rongYunResponse.getMeta().getTongyunToken().getToken());
        appUser.setNickname(rongYunResponse.getData().getName());
        appUser.setHeadimgurl(rongYunResponse.getData().getAvatar());
        appUser.setSign(rongYunResponse.getData().getSign());
        appUser.setSmallHeadimgurl(rongYunResponse.getData().getAvatarSmall());
        appUser.setGender(rongYunResponse.getData().getGender());
        appUser.setCity(rongYunResponse.getData().getCity());
        appUser.setProvince(rongYunResponse.getData().getProvince());
        appUser.setPhoneNum(rongYunResponse.getData().getPhoneNumber());
        appUser.setLevel(rongYunResponse.getData().getLevel());
        appUser.setFansNum(rongYunResponse.getData().getFansNumber());
        appUser.setFocusNum(rongYunResponse.getData().getWatchedNumber());
        appUser.setLeNum(rongYunResponse.getData().getUserNumber());
    }
}
