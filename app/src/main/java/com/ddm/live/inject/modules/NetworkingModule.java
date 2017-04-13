package com.ddm.live.inject.modules;

import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.models.network.api.LocationApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wsheng on 16/1/18.
 */
@Module
public class NetworkingModule {
    @Provides
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
//                下面是测试环境
//                .baseUrl("http://172.16.40.14:8088")
//                .baseUrl("http://i.9ddm.com:8580/")
//                下面是正式环境
                .baseUrl("http://i.9ddm.com:8180/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    LocationApi getApiService(Retrofit retrofit) {
        return retrofit.create(LocationApi.class);
    }


    @Provides
    LocationApiService provideLiveApiService(LocationApi locationApi) {
        return new LocationApiService(locationApi);
    }
}
