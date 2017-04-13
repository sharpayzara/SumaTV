package com.ddm.live.models.layerinterfaces;

import com.ddm.live.AppApplication;
import com.ddm.live.constants.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by cxx on 2016/8/12.
 */
public class RetrofitApiManager {
    //    public static final String SERVER_URL = "http://172.16.40.19:8000/api/";
//    public static final String SERVER_URL = "http://t.9ddm.com/api/";
//    public static final String SERVER_URL = "http://liveapi.9ddm.com/api/";
    //    public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/";
    public String header_key = "Authorization";
    private String header_value = "Bearer f0GHfgTSa97N95kqVG5VgAtlJ7cFkEETNHeg2Efh";
    public String refresh_token = "6fuarcsQGiQiyIATzkOipSmJyA24qhtq55aUdJU7";
    private long DEFAULT_TIMEOUT = 30;
    private String version_header_key = "Accept";
//    private String version_header_value = "application/vnd.live.v1+json";
    private String version_header_value = "application/vnd.live.v1.2+json";
    public String getHeader_value() {
        return header_value;
    }

    public void setHeader_value(String header_value) {
        this.header_value = header_value;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public RetrofitApiManager() {
        String access_token = AppApplication.getSharePreferenceHelper().getValue("access_token").toString();
        setHeader_value("Bearer " + access_token);
//        setHeader_value("Bearer Q6vvHXjjvW2QUDvWlHNB1GJmH3gBKdD4YjuCGD");
//        System.out.println("access_token:" + access_token);
    }

    public String getHeader_key() {
        return header_key;
    }

    public void setHeader_key(String header_key) {
        this.header_key = header_key;
    }

    /**
     * 提供有头的api接口
     *
     * @return
     */
    public Retrofit provideWithHeaderApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(genericClient())
                .build();
        return retrofit;
    }

    /**
     * 提供无头的api接口
     *
     * @return
     */

    public Retrofit provideNoHeaderApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(genericWithNoAuthorizationClient())
                .build();
        return retrofit;
    }


    //为http请求添加统一的头部
    public OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(header_key, header_value)
                                .addHeader(version_header_key, version_header_value)
                                .build();
                        return chain.proceed(request);
                    }

                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }

    //为http请求添加统一的头部
    public OkHttpClient genericWithNoAuthorizationClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(version_header_key, version_header_value)
                                .build();
                        return chain.proceed(request);
                    }

                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }
}
