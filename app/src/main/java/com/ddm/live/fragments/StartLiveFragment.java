package com.ddm.live.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.basebean.AppConfig;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.StartLivePresenter;
import com.ddm.live.views.iface.IPreLiveView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by wsheng on 16/1/17.
 */
public class StartLiveFragment extends BaseFragment implements IPreLiveView {

    private LocationManager locationManager;
    private String locationProvider;
//    private SweetAlertDialog loaddingDialog;

    @Inject
    StartLivePresenter presenter;

    private AppUser appUser;

    @BindView(R2.id.pre_live_location)
    TextView tvLocation;

    @BindView(R2.id.pre_live_subject)
    TextView etSubject;

    @OnClick(R2.id.pre_live_btn)
    public void clickToLive() {
        //判断用户是否登录
        if (appUser.isLogined()) {

//            loaddingDialog = showLoaddingDialog("准备直播,请稍候...");

            String title = String.valueOf(System.currentTimeMillis());
            String address = tvLocation.getText().toString();
            String subject = etSubject.getText().toString();
            Integer uid = appUser.getId();
            String isopen = "1";
            if (subject.isEmpty()) {
                subject = AppConfig.DEFULT_LIVE_SUBJECT;
            }
            address = (address.equals("") ? "未知地理位置" : address);
            presenter.createStream2(subject, title, address);
        } else {
            /*new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("登录后就可以发起直播啦!")
//                .setContentText("Won't be able to recover this file!")
                    .setConfirmText("我要登录")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            HomeActivity homeActivity = (HomeActivity) getActivity();
                            CommonTabLayout mTabLayout = homeActivity.getmTabLayout();
                            mTabLayout.setCurrentTab(2);
                        }
                    })
                    .show();*/
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_live, container, false);
        ButterKnife.bind(this, view);

        presenter.attachIView(this);

        getLocation();

        return view;
    }

    @Override
    public void setLocation(String address) {
        tvLocation.setText(address);
    }


    @Override
    public void onFinishCreateStream(String streamJson, String streamId, String roomId, String qnId) {
//        Intent intent = new Intent(getActivity(), MasterStartLiveActivity.class);
//        intent.putExtra("stream_json_str", streamJson);
//        intent.putExtra("stream_id", streamId);
//        intent.putExtra("qiniu_id", qnId);
//        intent.putExtra("cid", roomId);
//        startActivity(intent);
//
//        //创建成功上一个流时,清除主题
//        etSubject.setText("");
    }

    public void getLocation() {

        //获取地理位置管理器
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
//            Toast.makeText(getActivity(), "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        //获取Location
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "不允许", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度

            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
            presenter.getBaiduLocation(locationString);

        }

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //如果位置发生变化,重新显示
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle arg2) {
                Log.d(TAG, "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e(TAG, "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onProviderDisabled: ");
            }
        };

        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(this.getContext()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenterComponent.inject(this);

        appUser = ((AppApplication) appComponent.getApplication()).getAppUser();
    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }
}
