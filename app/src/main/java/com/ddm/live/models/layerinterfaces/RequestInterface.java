package com.ddm.live.models.layerinterfaces;

import android.content.Intent;

import com.ddm.live.AppApplication;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.constants.Constants;
import com.ddm.live.models.bean.authorization.RefreshTokenRequest;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.basebean.request.BaseRequest;
import com.ddm.live.models.bean.common.authorizationbeans.PersistentBean;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.models.network.service.BaseService;
import com.ddm.live.utils.GetErrorResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by cxx on 2016/8/15.
 */
public class RequestInterface extends BaseService {

    Class apiService;
    Method method;
    RetrofitApiManager retrofitApiManager;
    AppUser appUser;
    Observable<ResponseBaseInterface> responseObserver;

    public RequestInterface() {
        retrofitApiManager = new RetrofitApiManager();
        appUser = AppApplication.getInstance().getAppUser();
    }

    public Observable<ResponseBaseInterface> sendRequest2(final BaseRequest request) {


        String interfaceName = request.getInterfaceServiceName();
        String methodName = request.getMethodName();
        Observable<ResponseBaseInterface> result;
        try {
            apiService = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            method = apiService.getMethod(methodName, request.getClass());
//            Log.e("cxx",methodName);
            responseObserver = (Observable<ResponseBaseInterface>) method.invoke(apiService.newInstance(), request);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        result = responseObserver.flatMap(new Func1<ResponseBaseInterface, Observable<ResponseBaseInterface>>() {
            @Override
            public Observable<ResponseBaseInterface> call(ResponseBaseInterface responseBaseInterface) {

                return Observable.just(responseBaseInterface);
            }
        }).doOnError(new Action1<Throwable>() {
                         @Override
                         public void call(Throwable e) {
                             ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                             if (null != errorResponseBean) {
                                 String errorMessage = errorResponseBean.getMessage();
                                 Integer errorStatusCode = errorResponseBean.getStatusCode();
                                 print("网络层:" + errorMessage + ":" + errorStatusCode);
                                 if (errorStatusCode == 401) {//判断是否是401错误，错误的话就刷洗token
                                     RefreshManager refreshManager = RefreshManager.getInstance();
                                     refreshManager.refreshToken()
                                             .subscribe(new Subscriber<ResponseBaseInterface>() {
                                                 @Override
                                                 public void onCompleted() {

                                                 }

                                                 @Override
                                                 public void onError(Throwable e) {
                                                     RefreshManager.isRefreshed=true;
                                                     ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                                                     if (null != errorResponseBean) {
                                                         Integer errorStatusCode = errorResponseBean.getStatusCode();
                                                         String errorMessage=errorResponseBean.getMessage();
                                                         if (errorMessage.equals("The refresh token is invalid.")) {
//                                                             Log.e("cxx","The refresh token is invalid.");
                                                             AppApplication.getInstance().getAppUser().clearUserInfo();
                                                             Intent intent = new Intent(AppApplication.getInstance(), BFLoginActivity.class);
                                                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                             AppApplication.getInstance().startActivity(intent);
                                                         }
                                                     }
                                                 }

                                                 @Override
                                                 public void onNext(ResponseBaseInterface responseBaseInterface) {
                                                     checkAndStoragePersistentInfo(responseBaseInterface);
                                                     retrofitApiManager.setHeader_value("Bearer " + appUser.getAccess_token());
//                                                     Log.e("cxx","refresh success");
                                                     sendRequest2(request);

                                                 }
                                             });


                                 }

                             }
                         }
                     }
        )
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ResponseBaseInterface>>() {
                    @Override
                    public Observable<? extends ResponseBaseInterface> call(Throwable throwable) {
                        return responseObserver;
                    }
                });

        return result;
    }

    //刷新token
    public Observable<ResponseBaseInterface> refreshToken() {

//        print("******************开始刷新token*********************");
        final RefreshTokenRequest refreshRequest = new RefreshTokenRequest("refresh_token",
                Constants.CLIENT_ID, Constants.CLIENT_SECRETE, appUser.getRefresh_token());

        return sendRequest2(refreshRequest).flatMap(new Func1<ResponseBaseInterface, Observable<ResponseBaseInterface>>() {
            @Override
            public Observable<ResponseBaseInterface> call(ResponseBaseInterface responseBaseInterface) {
                //先对返回的Response进行拦截，判断返回的是否是需要进行持久化的PersistentBean的子类,如果是则进行持久化
                checkAndStoragePersistentInfo(responseBaseInterface);
                return Observable.just(responseBaseInterface);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends ResponseBaseInterface>>() {
            @Override
            public Observable<? extends ResponseBaseInterface> call(Throwable throwable) {

                return sendRequest2(refreshRequest);
            }
        });
    }

    //存储持久化信息
    public void checkAndStoragePersistentInfo(ResponseBaseInterface response) {
        Observable.just(response).subscribe(new Subscriber<ResponseBaseInterface>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {//此处若有异常不用处理，交给下一级处理

            }

            @Override
            public void onNext(ResponseBaseInterface responseBaseInterface) {
                //判断返回的Response是否需要进行持久化存储
                boolean isFarther = (PersistentBean.class).isAssignableFrom(responseBaseInterface.getClass());
                if (isFarther) {
                    storagePersistentInfo(responseBaseInterface);
                }
            }
        });
    }

    //存储持久化信息
    public void storagePersistentInfo(ResponseBaseInterface responseBaseInterface) {
        PersistentBean persistentBean = (PersistentBean) responseBaseInterface;
        appUser.setAccess_token(persistentBean.getAccessToken());
        appUser.setRefresh_token(persistentBean.getRefreshToken());
        appUser.setExpires_in(String.valueOf(persistentBean.getExpiresIn()));
//        System.out.println("cxx*****************************************************");
//        Log.e("cxx刷新成功-access_token", appUser.getAccess_token());
//        Log.e("cxx刷新成功-refresh_token", appUser.getRefresh_token());
        RefreshManager.isRefreshed = true;
    }

}