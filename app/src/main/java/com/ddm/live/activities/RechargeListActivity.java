package com.ddm.live.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.RechargeRecorderListAdapter;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.GetRechargeListResponseData;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.common.accountbeans.AccountTypesDataBean;
import com.ddm.live.presenter.AccountPresenter;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IAccountView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeListActivity extends BaseActivity implements IAccountView {
    private AccountPresenter presenter;
    private AppComponent appComponent;
    //    android.widget.ListView rechargeRecorderList;
//    private ListView rechargeRecorderList;
//    private ProgressBarDialogFragment progressBarDialogFragment;

    @BindView(R2.id.recharge_record_list)
    ListView rechargeRecorderList;

    @BindView(R2.id.no_data_empty_view)
    LinearLayout llEmptyLayout;

    @BindView(R2.id.empty_view_text)
    TextView txEmptyWord;


    public AppComponent getAppComponent() {
        return appComponent;
    }

//    public ProgressBarDialogFragment getProgressBarDialogFragment() {
//        return progressBarDialogFragment;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.xiaozhu_top_status_bar);
        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.xiaozhu_bottom_status_bar);
        setContentView(R.layout.activity_recharge_list);
        ButterKnife.bind(this);
//        rechargeRecorderList = (ListView) findViewById(R.id.recharge_record_list);
//        progressBarDialogFragment = new ProgressBarDialogFragment();
//        if (!progressBarDialogFragment.isAdded()) {
//            progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
//        }

        presenter.attachView(this);
        presenter.queryRechargeInfoList("1");
        initView();

    }

    public void initView() {
        ImageView returnBt = (ImageView) findViewById(R.id.recharge_record_return_btn);
        returnBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
            }
        });
        txEmptyWord.setText("无购买记录");
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        presenter = presenterComponent.getAccountPresenter();
    }

    @Override
    public void onQueryRechargeRecoderList(QueryChargeInfoListResponse response) {

        rechargeRecorderList.setAdapter(new RechargeRecorderListAdapter(response, this));
        if (response.getData().size() == 0) {
            llEmptyLayout.setVisibility(View.VISIBLE);
            rechargeRecorderList.setVisibility(View.INVISIBLE);
        } else {
            llEmptyLayout.setVisibility(View.INVISIBLE);
            rechargeRecorderList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList) {

    }

    @Override
    public void isPaySuccess(int postion, int type) {

    }

    @Override
    public void onGetPrepayId(BuyVirtualCoinsResponse response) {

    }

    @Override
    public void onGetFansContributionToatal(String total) {

    }

    @Override
    public void getRechargeListInfoResponse(List<GetRechargeListResponseData> chargeList) {

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }
}
