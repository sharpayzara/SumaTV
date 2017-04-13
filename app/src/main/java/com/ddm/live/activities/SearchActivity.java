package com.ddm.live.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.SearchUserInfoListAdapter;
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
import com.ddm.live.ui.widget.LoadingView;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IUserInfoBaseView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements IUserInfoBaseView {
    @BindView(R.id.search_edit_text)
    EditText searchInput;
    @BindView(R2.id.search_result_list)
    ListView lvSearchList;
    @BindView(R2.id.search_big)
    ImageView searchBig;
    @BindView(R.id.loadView)
    LoadingView loadingView;

    @OnClick(R2.id.search_cancle_btn)
    public void cancel() {
        hideKeyboard();
        finish();
        overridePendingTransition(R.anim.activity_enter_left, R.anim.activity_close_right);
    }

    @OnClick(R2.id.search_delete_btn)
    public void delete() {
        searchInput.setText("");
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }

    @Inject
    UserInfoPresenter userInfoPresenter;
    private SearchUserInfoListAdapter searchUserInfoListAdapter;
    private List<UserInfoBean> searchUserInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.xiaozhu_top_status_bar);
        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.xiaozhu_bottom_status_bar);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchInput.addTextChangedListener(textWatcher);
        searchUserInfoListAdapter = new SearchUserInfoListAdapter(SearchActivity.this, searchUserInfoList);
        lvSearchList.setAdapter(searchUserInfoListAdapter);
        lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("uID", searchUserInfoListAdapter.getItem(position).getId());
                intent.putExtra("userImg", searchUserInfoListAdapter.getItem(position).getAvatarSmall());
                intent.putExtra("username", searchUserInfoListAdapter.getItem(position).getName());
                intent.putExtra("gender", searchUserInfoListAdapter.getItem(position).getGender());
                intent.putExtra("level", searchUserInfoListAdapter.getItem(position).getLevel());
                intent.putExtra("userNumber", searchUserInfoListAdapter.getItem(position).getUserNumber());
                intent.putExtra("sign", searchUserInfoListAdapter.getItem(position).getSign());
                intent.setClass(SearchActivity.this, UserInfoHomeActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String ss = searchInput.getEditableText().toString();
            if (ss.length() > 0) {
                loadingView.setVisibility(View.VISIBLE);
                userInfoPresenter.searchUsersByKeywords(ss);
            } else {
                searchBig.setVisibility(View.VISIBLE);
                searchUserInfoListAdapter.clearData();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
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
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList, String total) {

    }

    @Override
    public void getUserInfoResult(List<UserInfoBean> userInfoBeen) {
        setUserInfo2List(userInfoBeen);
    }

    private void setUserInfo2List(List<UserInfoBean> infoList) {
        loadingView.setVisibility(View.GONE);
        if (infoList.size() > 0) {
            searchBig.setVisibility(View.GONE);
        }
        searchUserInfoList = infoList;
        searchUserInfoListAdapter.notifyAdapter(infoList);


    }

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
        loadingView.setVisibility(View.GONE);
    }
}
