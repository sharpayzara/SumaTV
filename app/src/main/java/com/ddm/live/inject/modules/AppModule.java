package com.ddm.live.inject.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wsheng on 16/1/12.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application getApplication(){
        return application;
    }
}
