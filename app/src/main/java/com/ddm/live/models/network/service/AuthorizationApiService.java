package com.ddm.live.models.network.service;


import com.ddm.live.models.bean.authorization.GetTokenByPwdRequest;
import com.ddm.live.models.bean.authorization.GetTokenByPwdResponse;
import com.ddm.live.models.bean.authorization.LoginByWXRequest;
import com.ddm.live.models.bean.authorization.LoginByWXResponse;
import com.ddm.live.models.bean.authorization.RefreshTokenRequest;
import com.ddm.live.models.bean.authorization.RefreshTokenResponse;
import com.ddm.live.models.bean.authorization.RegisterByPhoneNumberRequest;
import com.ddm.live.models.bean.authorization.RegisterByPhoneNumberResponse;
import com.ddm.live.models.bean.authorization.ResetPasswordRequest;
import com.ddm.live.models.bean.authorization.ResetPasswordResponse;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeFpwdRequest;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeFpwdResponse;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeRequest;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeResponse;
import com.ddm.live.models.network.api.AuthorizationApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by cxx on 16/8/8.
 * 直播相关的接口API
 */
public class AuthorizationApiService extends BaseService {

    private AuthorizationApi apiService;

    RetrofitApiManager retrofitApiManager;

    public AuthorizationApiService() {

        retrofitApiManager = new RetrofitApiManager();
        apiService = retrofitApiManager.provideNoHeaderApi().create(AuthorizationApi.class);

    }


    /**
     * 通过微信登录
     */
    public Observable<LoginByWXResponse> login(LoginByWXRequest request) {
        Observable<LoginByWXResponse> response = apiService.login(request);
        return response;
    }

    /**
     * 发送注册短信验证码
     */
    public Observable<SendSmsVerificationCodeResponse> sendSmsVerificationCode(SendSmsVerificationCodeRequest request) {
        Observable<SendSmsVerificationCodeResponse> response = apiService.sendSmsVerificationCode(request);
        return response;
    }

    /**
     * 通过手机注册
     */
    public Observable<RegisterByPhoneNumberResponse> registByPhoneNumber(RegisterByPhoneNumberRequest request) {
        Observable<RegisterByPhoneNumberResponse> response = apiService.registByPhoneNumber(request);
        return response;
    }


    /**
     * 发送忘记密码短信验证
     */
    public Observable<SendSmsVerificationCodeFpwdResponse> sendSmsVerificationCodeFpwd(SendSmsVerificationCodeFpwdRequest request) {
        Observable<SendSmsVerificationCodeFpwdResponse> response = apiService.sendSmsVerificationCodeFpwd(request);
        return response;
    }


    /**
     * 密码重置
     */
    public Observable<ResetPasswordResponse> resetPassword(ResetPasswordRequest request) {
        Observable<ResetPasswordResponse> response = apiService.resetPassword(request);
        return response;
    }

    /**
     * 密码认证获取token
     */
    public Observable<GetTokenByPwdResponse> getTokenByPassword(GetTokenByPwdRequest request) {
        Observable<GetTokenByPwdResponse> response = apiService.getTokenByPassword(request);
        return response;
    }

    /**
     * 刷新token
     */
    public Observable<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        Observable<RefreshTokenResponse> response = apiService.refreshToken(request);
        return response;
    }

}