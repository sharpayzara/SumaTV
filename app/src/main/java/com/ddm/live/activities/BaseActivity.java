package com.ddm.live.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.models.bean.user.AppUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by wsheng on 16/1/12.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = this.getClass().getName();
    protected AppUser appUserActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appUserActivity = ((AppApplication) this.getApplication()).getAppUser();
        setupActivityComponent(AppApplication.get(this).getAppComponent());

    }

    public abstract void setupActivityComponent(AppComponent appComponent);

    protected void toast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void onfaild(String message) {
        if (!message.equals("The refresh token is invalid.") && !message.equals("The resource owner or authorization server denied the request.")) {//刷新token不成功，重新登录
            toast(message);
        }
    }

    protected SweetAlertDialog showLoaddingDialog(String loaddingString) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(loaddingString);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    protected SweetAlertDialog showSuccessDialog(String successString) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(successString);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        pDialog.show();
        return pDialog;
    }

    protected SweetAlertDialog showErrorDialog(String errorString) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(errorString);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        pDialog.show();
        return pDialog;
    }

    protected SweetAlertDialog showWarnDialog(String warnString) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(warnString);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        pDialog.show();
        return pDialog;
    }


}
