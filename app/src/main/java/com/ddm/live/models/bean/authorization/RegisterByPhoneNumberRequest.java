package com.ddm.live.models.bean.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/7.
 */
public class RegisterByPhoneNumberRequest extends AuthorizationBaseRequest {

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("code")
    private String code;

    public RegisterByPhoneNumberRequest(String phone_number, String password, String code) {
        setMethodName("registByPhoneNumber");
        this.phoneNumber = phone_number;
        this.password = password;
        this.code = code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
