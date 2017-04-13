package com.ddm.live.models.layerinterfaces;

import com.ddm.live.AppApplication;
import com.ddm.live.constants.Constants;
import com.ddm.live.models.bean.authorization.RefreshTokenRequest;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.user.AppUser;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/1/15.
 */

public class RefreshManager {
    private AppUser appUser;
    private static RefreshManager refreshManager;
    public static boolean isRefreshed = true;

    private RefreshManager() {
        appUser = AppApplication.getInstance().getAppUser();
    }

    public static synchronized RefreshManager getInstance() {
        if (refreshManager == null) {
            refreshManager = new RefreshManager();
        }
        return refreshManager;
    }

    //刷新token
    public synchronized Observable<ResponseBaseInterface> refreshToken() {
//        Log.e("cxx","start refresh");
        while (!RefreshManager.isRefreshed) {
        }
        if (RefreshManager.isRefreshed) {
            RefreshManager.isRefreshed = false;
        }
        Observable<ResponseBaseInterface> result;

        RefreshTokenRequest refreshRequest = new RefreshTokenRequest("refresh_token",
                Constants.CLIENT_ID, Constants.CLIENT_SECRETE, appUser.getRefresh_token());
        RequestInterface requestInterface = new RequestInterface();
        result = requestInterface.sendRequest2(refreshRequest).flatMap(new Func1<ResponseBaseInterface, Observable<ResponseBaseInterface>>() {
            @Override
            public Observable<ResponseBaseInterface> call(ResponseBaseInterface responseBaseInterface) {
//                checkAndStoragePersistentInfo(responseBaseInterface);
                return Observable.just(responseBaseInterface);
            }
        });
        return result;
    }
}
