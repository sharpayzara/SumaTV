package com.ddm.live.inject.components;

import android.app.Application;

import com.ddm.live.inject.modules.AppModule;

import dagger.Component;

/**
 * Created by wsheng on 16/1/18.
 */
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();
}
