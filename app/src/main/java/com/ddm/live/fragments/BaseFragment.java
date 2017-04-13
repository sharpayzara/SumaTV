package com.ddm.live.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.models.bean.user.AppUser;

//import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by wsheng on 16/1/18.
 */
public abstract class BaseFragment extends Fragment {

    public String TAG = this.getClass().getName();
    protected AppUser appUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appUser = ((AppApplication) getActivity().getApplication()).getAppUser();

        setupFragmentComponent();
    }

    public abstract void setupFragmentComponent();

    public void toast(String message) {
        if (getActivity() == null) {
            return;
        }else if(getActivity()!=null){
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onfaild(String message) {
        if (!message.equals("The refresh token is invalid.") && !message.equals("The resource owner or authorization server denied the request.")) {//刷新token不成功，重新登录
           toast(message);
        }
    }
}
