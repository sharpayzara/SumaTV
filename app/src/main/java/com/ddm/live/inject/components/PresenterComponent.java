package com.ddm.live.inject.components;

import com.ddm.live.activities.BlueUserVideoManageActivity;
import com.ddm.live.activities.FansContributionListActivity;
import com.ddm.live.activities.SearchActivity;
import com.ddm.live.fragments.FansContributonListFragment;
import com.ddm.live.fragments.LiveFocusListFragment;
import com.ddm.live.fragments.LiveListFragment;
import com.ddm.live.fragments.LiveNewListFragment;
import com.ddm.live.fragments.LiveSelectListFragment;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.PersionalInfoPanelFragment;
import com.ddm.live.fragments.StartLiveFragment;
import com.ddm.live.fragments.UserCenterLoginoutFragment;
import com.ddm.live.fragments.UserLiveListFragment;
import com.ddm.live.fragments.WatcherLayerFragment;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.presenter.AccountPresenter;
import com.ddm.live.presenter.GiftPresenter;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.presenter.LoginPresenter;
import com.ddm.live.presenter.MystarPresenter;
import com.ddm.live.presenter.RegisterPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.presenter.StartLivePresenter;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.presenter.UserInfoPresenter;
import com.ddm.live.presenter.VideoPlayerPresenter;

import dagger.Component;

/**
 * Created by wsheng on 16/1/18.
 */
@Component(modules = {PresenterModule.class}, dependencies = {NetworkingComponent.class, AppComponent.class})
public interface PresenterComponent {
    void inject(LiveListFragment fragment);

    void inject(UserLiveListFragment fragment);

    void inject(StartLiveFragment fragment);

    void inject(UserCenterLoginoutFragment fragment);

    void inject(BlueUserVideoManageActivity activity);

    void inject(WatcherLayerFragment fragment);

    void inject(LiveSelectListFragment fragment);

    void inject(LiveFocusListFragment fragment);

    void inject(LiveNewListFragment fragment);

    void inject(FansContributonListFragment fragment);

    void inject(PersionalInfoPanelFragment fragment);

    void inject(FansContributionListActivity fansContributionListActivity);

    void inject(SearchActivity searchActivity);

    void inject(LiveViewFragment liveViewFragment);

    LiveListPresenter getLiveListPresenter();

    StartLivePresenter getStartLivePresenter();

    RongYunPresenter getRongYunPresenter();

    LoginPresenter getLoginPresenter();

    VideoPlayerPresenter getVideoPlayerPresenter();

    RegisterPresenter getRegisterPresenter();

    AccountPresenter getAccountPresenter();

    UserCenterPresenter getUserCenterPresenter();

    UserInfoPresenter getUserInfoPresenter();

    GiftPresenter getGiftPresenter();

    MystarPresenter getMystarPresenter();
}
