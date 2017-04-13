package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.file.QiniuUploadTokenRequest;
import com.ddm.live.models.bean.file.QiniuUploadTokenResponse;
import com.ddm.live.models.network.api.FileApi;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;

import rx.Observable;

/**
 * Created by JiGar on 2016/11/3.
 */
public class FileApiService extends BaseService {

    FileApi apiService;
    RetrofitApiManager retrofitApiManager;

    public FileApiService() {
        retrofitApiManager = new RetrofitApiManager();
        apiService = retrofitApiManager.provideWithHeaderApi().create(FileApi.class);
    }

    //通过七牛上传图片token
    public Observable<QiniuUploadTokenResponse> getQiniuUploadToken(QiniuUploadTokenRequest request) {
        Observable<QiniuUploadTokenResponse> response = apiService.getQiniuUploadToken();
        return response;
    }

}
