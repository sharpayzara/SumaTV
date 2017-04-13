package com.ddm.live.models.bean.file;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/10.
 */
public class QiniuUploadTokenRequest extends FileBaseRequest {
    public QiniuUploadTokenRequest() {
        setMethodName("getQiniuUploadToken");
    }
}