package com.ddm.live.inject.components.test;

import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.presenter.VideoPlayerPresenter;

import dagger.Component;

/**
 * Created by wsheng on 16/1/18.
 */
@Component(modules = {PresenterModule.class},dependencies = {NetworkingComponent.class,AppComponent.class})
public interface PresenterComponentTest {




    VideoPlayerPresenter getVideoPlayerPresenter();


}
