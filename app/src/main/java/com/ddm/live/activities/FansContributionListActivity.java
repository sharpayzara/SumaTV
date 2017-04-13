package com.ddm.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.FansContributionListAdapter;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.UserInfoPresenter;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IUserInfoBaseView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FansContributionListActivity extends BaseActivity implements IUserInfoBaseView {
    private AppComponent appComponent;
    List<FansContributionInfoBean> fansContributionInfoBeanList;
    @BindView(R2.id.fans_contribution_list)
    ListView fansContributionListView;
    @BindView(R2.id.total_diamonds_number)
    TextView totalDiamondNumber;
    @OnClick(R2.id.user_contribution_title_back_img)
    public void backBtn() {
        finish();
    }

    @Inject
    UserInfoPresenter userInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.xiaozhu_top_status_bar);
        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.xiaozhu_bottom_status_bar);
        setContentView(R.layout.activity_fans_contribution_list);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        Integer uID = getIntent().getIntExtra("uID",0);
        userInfoPresenter.searchFansContributionList(uID);
        fansContributionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到个人主页
                Intent intent = new Intent(FansContributionListActivity.this, UserInfoHomeActivity.class);

                intent.putExtra("uID", fansContributionInfoBeanList.get(position).getId());
                intent.putExtra("userImg",fansContributionInfoBeanList.get(position).getAvatarSmall());
                intent.putExtra("username",fansContributionInfoBeanList.get(position).getName());
                intent.putExtra("gender",fansContributionInfoBeanList.get(position).getGender());
                intent.putExtra("level",fansContributionInfoBeanList.get(position).getLevel());
                intent.putExtra("userNumber",fansContributionInfoBeanList.get(position).getUserNumber());
                intent.putExtra("sign",fansContributionInfoBeanList.get(position).getSign());

                startActivity(intent);
            }
        });
    }

    @Override
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList,String total) {
        if(total!=null){
            String totalNum=((int)Float.parseFloat(total))+"";
            totalDiamondNumber.setText(totalNum);
        }else {
            totalDiamondNumber.setText("0");
        }
        for (int i = 0; i < 3 && i < fansContributionInfoBeanList.size(); i++) {
            fansContributionInfoBeanList.get(i).setType(0);
        }
        this.fansContributionInfoBeanList = fansContributionInfoBeanList;
        FansContributionListAdapter fansContributionListAdapter = new FansContributionListAdapter(fansContributionInfoBeanList, this);
        fansContributionListView.setAdapter(fansContributionListAdapter);
    }

    @Override
    public void getUserInfoResult(List<UserInfoBean> userInfoBeen) {

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

        userInfoPresenter = presenterComponent.getUserInfoPresenter();
        userInfoPresenter.attachView(this);
    }

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }
}
