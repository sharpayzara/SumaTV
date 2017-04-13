package com.ddm.live.inject.modules;

import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.presenter.AccountPresenter;
import com.ddm.live.presenter.GiftPresenter;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.presenter.LiveViewPresenter;
import com.ddm.live.presenter.LoginPresenter;
import com.ddm.live.presenter.MystarPresenter;
import com.ddm.live.presenter.RegisterPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.presenter.StartLivePresenter;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.presenter.UserInfoPresenter;
import com.ddm.live.presenter.VideoPlayerPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wsheng on 16/1/18.
 */
@Module
public class PresenterModule {

    /**
     * 直播列表服务类
     *
     * @param locationApiService
     * @return
     */
    @Provides
    LiveListPresenter provideLiveListPresenter(LocationApiService locationApiService) {
        return new LiveListPresenter(locationApiService);
    }


    /**
     * 开始直播服务类
     *
     * @param locationApiService
     * @return
     */
    @Provides
    StartLivePresenter provideStartLivePresenter(LocationApiService locationApiService) {
        return new StartLivePresenter(locationApiService);
    }


    /**
     * 观看直播服务类
     *
     * @param locationApiService
     * @return
     */
    @Provides
    VideoPlayerPresenter provideVideoPlayerPresenter(LocationApiService locationApiService) {
        return new VideoPlayerPresenter(locationApiService);
    }


    @Provides
    RongYunPresenter provideRongYunPresenter(LocationApiService locationApiService) {
        return new RongYunPresenter(locationApiService);
    }

    @Provides
    LoginPresenter provideLoginPresenter(LocationApiService locationApiService) {
        return new LoginPresenter(locationApiService);
    }

    @Provides
    UserCenterPresenter provideUserCenterPresenter() {
        return new UserCenterPresenter();
    }

    @Provides
    RegisterPresenter provideRegisterPresenter() {
        return new RegisterPresenter();
    }

    @Provides
    AccountPresenter provideAccountPresenter() {
        return new AccountPresenter();
    }

    @Provides
    UserInfoPresenter provideUserInfoPresenter() {
        return new UserInfoPresenter();
    }

    @Provides
    GiftPresenter provideGiftPresenter() {
        return new GiftPresenter();
    }

    @Provides
    LiveViewPresenter provideLivePresenter() {
        return new LiveViewPresenter();
    }

    @Provides
    MystarPresenter provideMystarPresenter() {
        return new MystarPresenter();
    }
}
