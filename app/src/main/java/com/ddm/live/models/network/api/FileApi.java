package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.file.QiniuUploadTokenResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by JiGar on 2016/11/3.
 */
public interface FileApi {
    //(1)File_获取七牛上传文件token
    @GET("file/user_image_token")
    Observable<QiniuUploadTokenResponse> getQiniuUploadToken();
}
