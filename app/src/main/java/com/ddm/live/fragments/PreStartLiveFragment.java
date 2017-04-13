package com.ddm.live.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.constants.Constants;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.StartLivePresenter;
import com.ddm.live.views.iface.IPreLiveView;
import com.ddm.live.views.iface.IPreliveLayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */

public class PreStartLiveFragment extends BaseFragment implements IPreLiveView {
    int radioId = R.id.radio_btn2;
    private IPreliveLayerView iPreliveLayerView;
    private Location location;
    private LocationManager locationManager;
    private String locationProvider;
    private StartLivePresenter startLivePresenter;
    AppUser appUser;
    @BindView(R2.id.radio_btn1)
    ToggleButton tb1;
    @BindView(R2.id.radio_btn2)
    ToggleButton tb2;
    @BindView(R2.id.radio_btn3)
    ToggleButton tb3;
    @BindView(R2.id.radio_btn4)
    ToggleButton tb4;
    @BindView(R2.id.radio_btn5)
    ToggleButton tb5;
    @BindView(R2.id.share_tip)
    ImageView shareTip;
    @OnClick(R2.id.radio_btn1)
    public void onTb1Click() {
        shareTip.setVisibility(View.INVISIBLE);
        if (tb1.isChecked()) {
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(false);
            radioId = R.id.radio_btn1;
        } else {
            radioId = -1;
        }
    }

    @OnClick(R2.id.radio_btn2)
    public void onTb2Click() {
        shareTip.setVisibility(View.INVISIBLE);
        if (tb2.isChecked()) {
            tb1.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(false);
            radioId = R.id.radio_btn2;
        } else {
            radioId = -1;
        }
    }

    @OnClick(R2.id.radio_btn3)
    public void onTb3Click() {
        shareTip.setVisibility(View.INVISIBLE);
        if (tb3.isChecked()) {
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(false);
            radioId = R.id.radio_btn3;
        } else {
            radioId = -1;
        }
    }

    @OnClick(R2.id.radio_btn4)
    public void onTb4Click() {
        shareTip.setVisibility(View.INVISIBLE);
        if (tb4.isChecked()) {
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb5.setChecked(false);
            radioId = R.id.radio_btn4;
        } else {
            radioId = -1;
        }
    }
    @OnClick(R2.id.radio_btn5)
    public void onTb5Click() {
        shareTip.setVisibility(View.INVISIBLE);
        if (tb5.isChecked()) {
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(false);
            radioId = R.id.radio_btn5;
        } else {
            radioId = -1;
        }
    }
    @BindView(R2.id.bf_pre_live_title_edit)
    EditText liveTitle;

    @BindView(R2.id.bf_pre_location_text)
    TextView txLocation;
    @BindView(R.id.location_lebal)
    ImageView locationLebal;
    @OnClick(R.id.location)
    public void locate(){
          if(locationLebal.isEnabled()){
              locationLebal.setEnabled(false);
              txLocation.setEnabled(false);
              txLocation.setText("外太空");

          }else {
              locationLebal.setEnabled(true);
              txLocation.setEnabled(true);
              onGetLocation();
          }
    }
//    @OnClick(R2.id.bf_pre_location_text)
    public void onGetLocation() {
        boolean isGranted=true;
        //检测是否开启定位权限
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        for (String str : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(getActivity(), permissions, Constants.REQUEST_CODE_LOCATION);
                isGranted=false;
                return;
            }
        }
        if(isGranted){
            getLocation();
        }
    }

    @OnClick(R2.id.bf_pre_close_btn)
    public void preLiveClose() {
        getActivity().finish();
        getActivity().overridePendingTransition(0,R.anim.activity_close_form_bottom);
    }

    @OnClick(R2.id.bf_pre_camare_btn)
    public void preSwitchCamera() {
        iPreliveLayerView.preStartLiveSwitchCamera();
    }

    public void attachView(IPreliveLayerView iPreliveLayerView) {
        this.iPreliveLayerView = iPreliveLayerView;
    }


    //开始直播按钮
    @OnClick(R2.id.bf_pre_live_btn)
    public void preStartLive() {
               startLive();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pre_start_live, container, false);
        ButterKnife.bind(this, view);
        appUser = ((AppApplication) getActivity().getApplication()).getAppUser();
        startLivePresenter.attachIView(this);
        initView();
        return view;
    }

    public void initView() {
        getLocation();
    }


    public void startLive() {
        String time = String.valueOf(System.currentTimeMillis());
        String address = txLocation.getText().toString();
        String subject = liveTitle.getText().toString();
        iPreliveLayerView.startLive(subject, time, address,radioId);
    }

    public void getLocation() {
        //获取地理位置管理器
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //获取所有可用的位置提供器
        List<String> providers = locationManager.getAllProviders();
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
//            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
            startLivePresenter.getBaiduLocation(locationString);

        }

    }

    @Override
    public void setLocation(String address) {
        if (address.isEmpty()) {
            txLocation.setText("外太空");
        } else {
            txLocation.setText(address);
        }

    }

    @Override
    public void onFinishCreateStream(String streamJson, String streamId, String roomId, String qnId) {

    }

    @Override
    public void onfailed(String message) {
                 super.onfaild(message);
    }

    public void reSetLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
            startLivePresenter.getBaiduLocation(locationString);

        }
    }
    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();
        startLivePresenter = presenterComponent.getStartLivePresenter();
    }
}
