package com.ddm.live.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ddm.live.AppApplication;
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
import com.ddm.live.views.iface.IUserInfoBaseView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cxx on 2016/11/19.
 */

public class FansContributonListFragment extends BaseFragment implements IUserInfoBaseView {
    List<FansContributionInfoBean> fansContributionInfoBeanList;
    @BindView(R2.id.fans_contribution_list)
    ListView fansContributionListView;
    @BindView(R2.id.total_diamonds_number)
    TextView totalDiamondNumber;

    @OnClick(R2.id.user_contribution_title_back_img)
    public void backBtn() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.frament_enter_right, R.anim.fragment_close);
        transaction.remove(this);
        transaction.commit();
    }

    @Inject
    UserInfoPresenter userInfoPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fans_contribution_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView() {
//        String uID = getActivity().getIntent().getStringExtra("uID");
        Integer uID = getArguments().getInt("uID");
        userInfoPresenter.searchFansContributionList(uID);
//        fansContributionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //跳转到个人主页
//                Intent intent = new Intent(getActivity(), UserInfoHomeActivity.class);
//                intent.putExtra("uID", String.valueOf(fansContributionInfoBeanList.get(position).getId()));
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList, String total) {
        if (total != null) {
            String totalNum = ((int) Float.parseFloat(total)) + "";
            totalDiamondNumber.setText(totalNum);
        } else {
            totalDiamondNumber.setText("0");
        }
        for (int i = 0; i < 3 && i < fansContributionInfoBeanList.size(); i++) {
            fansContributionInfoBeanList.get(i).setType(0);
        }
        this.fansContributionInfoBeanList = fansContributionInfoBeanList;
        FansContributionListAdapter fansContributionListAdapter = new FansContributionListAdapter(fansContributionInfoBeanList, getActivity());
        fansContributionListView.setAdapter(fansContributionListAdapter);
    }

    @Override
    public void getUserInfoResult(List<UserInfoBean> userInfoBeen) {

    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();
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
    public void onfailed(String message) {
        super.onfaild(message);
    }

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {

    }
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }
}
