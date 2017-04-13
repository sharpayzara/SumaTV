package com.ddm.live.models.network.api;


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
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeFpwdResponse;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeFpwdRequest;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeRequest;
import com.ddm.live.models.bean.authorization.SendSmsVerificationCodeResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wsheng on 16/1/7.
 */
/*
*转换Http API为java接口
* 每一个函数都必须提供请求方式和相对URL的http注解,注解中指定的资源是相对URL
* Retrofit提供了五种http注解:GET,POST,PUT,DELETE,HEAD
 */
public interface AuthorizationApi {

    //微信登录
    @POST("oauth/access_token?XDEBUG_SESSION_START=PHPSTORM")
    Observable<LoginByWXResponse> login(@Body LoginByWXRequest body);

    //发送注册短信验证码
    @POST("account/send_register_yzm?XDEBUG_SESSION_START=PHPSTORM")
    Observable<SendSmsVerificationCodeResponse> sendSmsVerificationCode(@Body SendSmsVerificationCodeRequest body);

    //通过手机号注册
    @POST("account/phone_register")
    Observable<RegisterByPhoneNumberResponse> registByPhoneNumber(@Body RegisterByPhoneNumberRequest body);

    //发送忘记密码验证短信
    @POST("account/send_forget_yzm")
    Observable<SendSmsVerificationCodeFpwdResponse> sendSmsVerificationCodeFpwd(@Body SendSmsVerificationCodeFpwdRequest body);

    //密码重置
    @POST("account/forget_password")
    Observable<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest body);

    //密码认证获取token
    @POST("oauth/access_token")
    Observable<GetTokenByPwdResponse> getTokenByPassword(@Body GetTokenByPwdRequest body);

    //刷新token
    @POST("oauth/access_token")
    Observable<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest body);

}
