package com.ddm.live.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddm.live.R;
import com.ddm.live.activities.MasterStartLiveActivity;
import com.ddm.live.animation.EmptyFragment;

/**
 * Created by cxx on 2016/10/10.
 */
public class LiveMainFragment extends Fragment {

    private ViewPager viewPager;
    private LiveLayerFragment liveLayerFragment;
    private Bundle bundle;
    public LiveMainFragment() {
    }

    public LiveLayerFragment getLiveLayerFragment() {
        return liveLayerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = getArguments();
//        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_UP)
//                {
//                   watcherLayerFragment.onKeyDown(keyCode,event);
//                }
//                return true;
//            }
//        });

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new EmptyFragment();/*返回空界面的fragment*/
                } else if (position == 1) {
                    liveLayerFragment = new LiveLayerFragment();
                    liveLayerFragment.setArguments(bundle);
                    liveLayerFragment.attachView(((MasterStartLiveActivity)getActivity()).getSwCodeCameraStreamingFragment());
                    return liveLayerFragment;/*返回交互界面的fragment*/
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(1);/*设置默认显示交互界面*/
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);/*同时将界面改为resize已达到软键盘弹出时LiveFragment不会跟随移动*/
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("cxx", "ccccccccc");
//            }
//        });
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
//            @Override
//            public void onBackPressed() {
//                super.onBackPressed();
//                getActivity().finish();
//            }
//        };
//        return dialog;
//    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
//                liveLayerFragment.hideKeyboard();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}