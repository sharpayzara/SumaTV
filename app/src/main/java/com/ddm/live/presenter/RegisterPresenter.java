package com.ddm.live.presenter;

import com.ddm.live.AppApplication;
import com.ddm.live.models.bean.authorization.RegisterByPhoneNumberRequest;
import com.ddm.live.models.bean.authorization.RegisterByPhoneNumberResponse;
import com.ddm.live.models.bean.authorization.ResetPasswordRequest;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeFpwdRequest;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeRequest;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenRequest;
import com.ddm.live.models.bean.users.GetCurrentUserInfoByTokenResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IRegisterResetPwmView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxx on 2016/9/6.
 */
public class RegisterPresenter extends BasePresenter {
    IRegisterResetPwmView iView;
    private AppApplication appApplication;
    private AppUser appUser;

    public RegisterPresenter() {
        appApplication = AppApplication.getInstance();
        appUser = appApplication.getAppUser();
    }

    public void attachView(IRegisterResetPwmView iView) {
        this.iView = iView;
    }

    //获取手机注册验证码
    public void getRegisterCode(String phoneNum) {
        SendSmsVerificationCodeRequest request = new SendSmsVerificationCodeRequest(phoneNum);
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
//                            print("获取验证码失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("获取验证码成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        iView.obtainCodeResult();
                    }
                });
    }

    //手机注册
    public void registerByPhoneNumber(String phoneNum, String pwm, String code) {

        RegisterByPhoneNumberRequest request = new RegisterByPhoneNumberRequest(phoneNum, pwm, code);
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
//                            print("手机注册失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("手机注册成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        RegisterByPhoneNumberResponse phoneNumberResponse = (RegisterByPhoneNumberResponse) response;
                        appUser.setExpires_in(String.valueOf(phoneNumberResponse.getExpiresIn()));
                        appUser.setRefresh_token(phoneNumberResponse.getRefreshToken());
                        appUser.setAccess_token(phoneNumberResponse.getAccessToken());

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
                                        iView.onResult();
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

    //获取手机注册验证码
    public void getResetPwmCode(String phoneNum) {
        SendSmsVerificationCodeFpwdRequest request = new SendSmsVerificationCodeFpwdRequest(phoneNum);
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
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("获取验证码成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        iView.obtainCodeResult();
                    }
                });
    }

    //密码重置
    public void retriecePassword(String phoneNum, String newPassword, String code) {
        ResetPasswordRequest request = new ResetPasswordRequest(phoneNum, newPassword, code);
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
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("密码修改成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        iView.onResult();
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
