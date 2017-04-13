package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.adapters.MyStarRecyclerViewAdapter;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.GetRechargeListResponseData;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.account.StarDiamondExchangeBean;
import com.ddm.live.models.bean.common.accountbeans.AccountTypesDataBean;
import com.ddm.live.presenter.AccountPresenter;
import com.ddm.live.presenter.MystarPresenter;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.views.iface.IMystarView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ytzys on 2017/2/21.
 * <p>
 * 我的星光页面
 */
public class MyStarActivity extends UserCenterBaseActivity implements IMystarView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick(R.id.header_user_center_right_txt)
    public void exchangeRecord() {
        Intent intent = new Intent(MyStarActivity.this, ExchangeRecordActivity.class);
        startActivity(intent);
    }

    AppComponent appComponent;
    MystarPresenter mystarPresenter;
    AccountPresenter presenter;
    MyStarRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystar);
        ButterKnife.bind(this);

        title.setText("我的星光");
        rightTitle.setText("兑换记录");

        presenter.attachView(this);
        mystarPresenter.attachView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyStarRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        initData();
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule()).build();
        presenter = presenterComponent.getAccountPresenter();
        mystarPresenter = presenterComponent.getMystarPresenter();
    }

    private void initData() {

        if (NetworkUtils.isNetworkAvailable(this)) {
            mystarPresenter.getExchangeListInfo(); // 获取兑换列表
            presenter.getAccountInfo();
        } else {
            Toast.makeText(MyStarActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList) {

    }

    @Override
    public void isPaySuccess(int position, int type) {

    }

    @Override
    public void onGetPrepayId(BuyVirtualCoinsResponse response) {

    }

    @Override
    public void onQueryRechargeRecoderList(QueryChargeInfoListResponse response) {

    }

    @Override
    public void onGetFansContributionToatal(String total) {

    }

    @Override
    public void getRechargeListInfoResponse(List<GetRechargeListResponseData> chargeList) {

    }

    @Override
    public void getExchargeListInfoResponse(List<StarDiamondExchangeBean> chargeList) {
        List<StarDiamondExchangeBean> list = new ArrayList<StarDiamondExchangeBean>();

        StarDiamondExchangeBean bean = new StarDiamondExchangeBean();
        bean.setDiamonds("80");
        bean.setStars("8");
        list.add(bean);

        StarDiamondExchangeBean bean1 = new StarDiamondExchangeBean();
        bean1.setDiamonds("80");
        bean1.setStars("8");
        list.add(bean1);

        StarDiamondExchangeBean bean2 = new StarDiamondExchangeBean();
        bean2.setDiamonds("80");
        bean2.setStars("8");
        list.add(bean2);

        StarDiamondExchangeBean bean3 = new StarDiamondExchangeBean();
        bean3.setDiamonds("80");
        bean3.setStars("8");
        list.add(bean3);

        adapter.setList(list);
    }

    @Override
    public void onfailed(String message) {
    }
}
