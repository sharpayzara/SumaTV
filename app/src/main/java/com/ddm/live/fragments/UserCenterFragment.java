package com.ddm.live.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddm.live.R;


/**
 * Created by JiGar on 2016/1/18.
 */
public class UserCenterFragment extends BaseFragment {

    private String TAG = "UserCenterFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_center, container, false);

        loadFragmentContent();

        return view;
    }

    public void loadFragmentContent() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (appUser.isLogined()) {
//            LoginoutFragment loginoutWXFragment = new LoginoutFragment();
            UserCenterLoginoutFragment userCenterLoginoutFragment = new UserCenterLoginoutFragment();
            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginoutFragment);
        } else {
//            LoginFragment loginFragment = new LoginFragment();
            UserCenterLoginFragment userCenterLoginFragment = new UserCenterLoginFragment();
            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginFragment);
        }

        fragmentTransaction.commit();

    }

    @Override
    public void setupFragmentComponent() {

    }
}
