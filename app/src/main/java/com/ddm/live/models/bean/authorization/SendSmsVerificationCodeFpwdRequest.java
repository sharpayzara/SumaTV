package com.ddm.live.models.bean.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class SendSmsVerificationCodeFpwdRequest extends AuthorizationBaseRequest {
    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    public SendSmsVerificationCodeFpwdRequest(String phone_number) {
        setMethodName("sendSmsVerificationCodeFpwd");
        this.phoneNumber = phone_number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone_number) {
        this.phoneNumber = phone_number;
    }
}
