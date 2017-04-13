package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.RechargeNumberListAdapter;
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
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IAccountView;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity implements IAccountView {
    private AccountPresenter presenter;
    private AppComponent appComponent;
//    private ProgressBarDialogFragment progressBarDialogFragment;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private IWXAPI api;

    private RechargeNumberListAdapter rechargeNumberListAdapter;
    private UltimateRecyclerView ultimateRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<GetRechargeListResponseData> chargeNumList = new ArrayList<GetRechargeListResponseData>();

    @OnClick(R2.id.recharge_num_list_title_back_img)
    public void goBack() {
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
    }

    @OnClick(R2.id.recharge_num_list_title_text)
    public void goBuyRecord() {
        startActivity(new Intent(RechargeActivity.this, RechargeListActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_user_fans_list_title_background);
        setContentView(R.layout.activity_recharge);
        presenter.attachView(this);
        ButterKnife.bind(this);

//        api = WXAPIFactory.createWXAPI(this,"wx81ec8ff55666bb9b",false);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp("wx81ec8ff55666bb9b");

//        progressBarDialogFragment = new ProgressBarDialogFragment();
//        if(!progressBarDialogFragment.isAdded()){
//            progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
//        }
        initData();

        presenter.getRechargeListInfo();
        ultimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.recharge_num_list_view);

        linearLayoutManager = new LinearLayoutManager(this);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        rechargeNumberListAdapter = new RechargeNumberListAdapter(chargeNumList, this, presenter);

        ultimateRecyclerView.setAdapter(rechargeNumberListAdapter);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                rechargeNumberListAdapter.clear();
                initData();
            }
        });

        ultimateRecyclerView.disableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                int count = rechargeNumberListAdapter.getAdapterItemCount() - 1;
//                presenter.loadList(simpleRecyclerViewAdapter.getAdapterItemCount() - 1, 10);
//                linearLayoutManager.scrollToPosition(maxLastVisiblePosition);
            }
        });

        ultimateRecyclerView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//                Log.e(TAG, "onScrollChanged: ");
            }

            @Override
            public void onDownMotionEvent() {
//                Log.e(TAG, "onDownMotionEvent: ");

            }

            @Override
            public void onUpOrCancelMotionEvent(ObservableScrollState observableScrollState) {
//                Log.e(TAG, "onUpOrCancelMotionEvent: ");
            }
        });
    }

    private void initData() {

        if (NetworkUtils.isNetworkAvailable(this)) {
            presenter.getRechargeListInfo();
            presenter.getAccountInfo();
        } else {
            Toast.makeText(RechargeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
        }
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
    protected void toast(String message) {
        super.toast(message);
    }

    @Override
    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList) {
        String lebi = "0";
        String diamonds = "0";
        if (isPay) {
            for (AccountTypesDataBean data : accountList) {
                if (data.getType() == 1) {
                    diamonds = String.valueOf((int) (Float.parseFloat(data.getAccountEarth().getData().getBalance())));
                } else if (data.getType() == 0) {
                    lebi = String.valueOf((int) (Float.parseFloat(data.getAccountEarth().getData().getBalance())));
                }
            }
        }
        TextView diamondsText = (TextView) findViewById(R.id.recharge_diamonds_num);
     //   TextView lebiText = (TextView) findViewById(R.id.recharge_lebi_num);
        diamondsText.setText(diamonds);
      //  lebiText.setText(lebi);

    }

    @Override
    public void isPaySuccess(int postion, int type) {

    }

    @Override
    public void onGetPrepayId(BuyVirtualCoinsResponse response) {
        try {
            PayReq req = new PayReq();
            req.appId = response.getAppid();
            req.partnerId = response.getPartnerid();
            req.prepayId = response.getPrepayId();
            req.packageValue = "Sign=WXPay";
            req.nonceStr = response.getNonceStr();
            req.timeStamp = response.getTimestamp();
            req.sign = response.getSign();
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }

    @Override
    public void onQueryRechargeRecoderList(QueryChargeInfoListResponse response) {

    }

    @Override
    public void onGetFansContributionToatal(String total) {

    }

    @Override
    public void getRechargeListInfoResponse(List<GetRechargeListResponseData> chargeList) {
//        chargeNumList.addAll(chargeList);
        rechargeNumberListAdapter.notifyDataChanged(chargeList);
//        for (GetRechargeListResponseData item : chargeList) {
//            rechargeNumberListAdapter.insert(item, rechargeNumberListAdapter.getAdapterItemCount());
//        }
//        ultimateRecyclerView.reenableLoadmore();
//        linearLayoutManager.scrollToPosition(0);

//        if (chargeList.size() < 10) {
//            //没有更多了
//            ultimateRecyclerView.disableLoadmore();
//        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        rechargeNumberListAdapter.clear();
        rechargeNumberListAdapter.getListItems();
        int count = rechargeNumberListAdapter.getAdapterItemCount();
        if (NetworkUtils.isNetworkAvailable(this)) {
            presenter.getRechargeListInfo();
        } else {
            Toast.makeText(RechargeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
        }
        System.gc();
        initData();
    }

    /*@Override
    protected void onDestroy() {
        if (lebiList != null) {
            lebiList.clear();
            lebiList = null;
            payMoney.clear();
            payMoney = null;
        }
        rechargeListAdapter = null;
        System.gc();
        super.onDestroy();
    }*/
}
