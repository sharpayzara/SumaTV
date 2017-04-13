package com.ddm.live.models.bean.common.commonbeans;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/8.
 */
public class ErrorResponseBean implements ResponseBaseInterface {
    @Expose
    @SerializedName("message")
    String message;

    @Expose
    @SerializedName("status_code")
    Integer statusCode;

    @Expose
    @SerializedName("errors")
    ErrorBean errors;

    public ErrorResponseBean(String message, Integer status_code) {
        this.message = message;
        this.statusCode = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer status_code) {
        this.statusCode = status_code;
    }

    public ErrorBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorBean errors) {
        this.errors = errors;
    }
}
