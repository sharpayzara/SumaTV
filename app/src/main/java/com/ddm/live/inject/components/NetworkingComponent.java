package com.ddm.live.inject.components;

import com.ddm.live.inject.modules.NetworkingModule;
import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.models.network.api.LocationApi;

import dagger.Component;

/**
 * Created by wsheng on 16/1/18.
 */

@Component(modules = {NetworkingModule.class})
public interface NetworkingComponent {
    LocationApi getLiveApi();
    LocationApiService getLiveApiService();
}

