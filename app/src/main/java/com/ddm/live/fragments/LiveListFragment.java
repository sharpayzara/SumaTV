package com.ddm.live.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.SearchActivity;
import com.ddm.live.views.utils.ViewFindUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsheng on 16/1/17.
 */
public class LiveListFragment extends Fragment {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"关注", "精选", "最新"};
    private SlidingTabLayout mTabLayout;
    private View view;
    private int curPosition = 1;

    /**
     * SDK打包时:打开home_return_text监听代码
     */
//    @OnClick(R2.id.home_return_text)
//    public void onHomeReturn() {
//        getActivity().finish();
//        getActivity().overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
//    }
    @OnClick(R2.id.home_search)
    public void onSearchClick() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_live_list, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mFragments.add(new LiveFocusListFragment());
        mFragments.add(new LiveSelectListFragment());
        mFragments.add(new LiveNewListFragment());

        mTabLayout = ViewFindUtils.find(view, R.id.live_list_sliding);
        ViewPager vp = ViewFindUtils.find(view, R.id.live_list_vp);
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        vp.setAdapter(new MyPagerAdapter(fm));
        mTabLayout.setViewPager(vp, mTitles, getActivity(), mFragments);
        mTabLayout.setCurrentTab(1);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
//                if(ButtonUtils.isFastSelectedClick(3000)){
//                    return;
//                }
                switch (position) {
                    case 0:
                        EventBus.getDefault().post(new RefreshMasterListEvent(1));

                        break;
                    case 1:
                        EventBus.getDefault().post(new RefreshSelectedListEvent(1));

                        break;
                    case 2:
                        EventBus.getDefault().post(new RefreshNewListEvent(1));

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {
                curPosition = position;
                switch (position) {
                    case 0:
                        EventBus.getDefault().post(new RefreshMasterListEvent(1));
                        break;
                    case 1:
                        EventBus.getDefault().post(new RefreshSelectedListEvent(1));
                        break;
                    case 2:
                        EventBus.getDefault().post(new RefreshNewListEvent(1));
                        break;
                }

            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

//    @Override
//    public void onResume() {
//        switch (curPosition){
//            case 0:
//                EventBus.getDefault().post(new RefreshMasterListEvent(1));
//                break;
//            case 1:
//                EventBus.getDefault().post(new RefreshSelectedListEvent(1));
//                break;
//            case 2:
//                EventBus.getDefault().post(new RefreshNewListEvent(1));
//                break;
//        }
//        super.onResume();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(RefreshListEvent refreshListEvent) {
        switch (curPosition) {
            case 0:
                if (refreshListEvent.getMode() == 0) {
                    EventBus.getDefault().post(new RefreshMasterListEvent(0));
                } else {
                    EventBus.getDefault().post(new RefreshMasterListEvent(1));
                }
                break;
            case 1:
                if (refreshListEvent.getMode() == 0) {
                    EventBus.getDefault().post(new RefreshSelectedListEvent(0));
                } else {
                    EventBus.getDefault().post(new RefreshSelectedListEvent(1));
                }

                break;
            case 2:
                if (refreshListEvent.getMode() == 0) {
                    EventBus.getDefault().post(new RefreshNewListEvent(0));
                } else {
                    EventBus.getDefault().post(new RefreshNewListEvent(1));
                }

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
