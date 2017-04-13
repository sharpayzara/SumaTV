package com.ddm.live.models.bean.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class ResetPasswordRequest extends AuthorizationBaseRequest {

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    @SerializedName("new_password")
    private String newPassword;

    @Expose
    @SerializedName("code")
    private String code;

    public ResetPasswordRequest(String phone_number, String new_password, String code) {
        setMethodName("resetPassword");
        this.phoneNumber = phone_number;
        this.newPassword = new_password;
        this.code = code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String new_password) {
        this.newPassword = new_password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
