package com.ddm.live.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.HomeActivity;
import com.ddm.live.activities.VideoPlayerActivity;
import com.ddm.live.adapters.SimpleAdapter;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.views.iface.IListLiveView;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.RecyclerItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wsheng on 16/1/17.
 */
public class UserLiveListFragment extends BaseFragment implements IListLiveView {

    int moreNum = 100;
    private ImageView headImageBitmap;
    private UltimateRecyclerView ultimateRecyclerView;

    private SimpleAdapter simpleRecyclerViewAdapter;

    private LinearLayoutManager linearLayoutManager;

    private List<StreamsBean> listItems = new ArrayList<StreamsBean>();

    private HomeActivity homeActivity;

    @Inject
    LiveListPresenter presenter;

    private StreamsBean liveItem;

    @OnClick(R2.id.user_live_back_btn)
    public void backToUserCenter(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserCenterLoginoutFragment userCenterLoginoutFragment = new UserCenterLoginoutFragment();
        fragmentTransaction.replace(R.id.user_center_content, userCenterLoginoutFragment);
        fragmentTransaction.commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return_back super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView: ");

        Log.d(TAG, "onCreateView: " + presenter.toString());

        presenter.attachView(this);

        homeActivity = (HomeActivity) getActivity();

//        presenter.loadUserList(0, 10, appUser.getId());

        final View view = inflater.inflate(R.layout.fragment_user_live_list, container, false);
        ButterKnife.bind(this, view);
        ultimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.ultimate_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        headImageBitmap = (ImageView) view.findViewById(R.id.headImageBitmap);
        simpleRecyclerViewAdapter = new SimpleAdapter(listItems, this.getContext(), appUser,headImageBitmap);

        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);

        ultimateRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + position);

                liveItem = simpleRecyclerViewAdapter.getItem(position);

            }
        }));

        // StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(simpleRecyclerViewAdapter);
        // ultimateRecyclerView.addItemDecoration(headersDecor);
        ultimateRecyclerView.enableLoadmore();
        simpleRecyclerViewAdapter.setCustomLoadMoreView(LayoutInflater.from(this.getContext())
                .inflate(R.layout.custom_bottom_progressbar, null));
//        ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffff66ff"));
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleRecyclerViewAdapter.clear();

                Log.d(TAG, "onRefresh: " + simpleRecyclerViewAdapter.getListItems());

//                ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
//                presenter.loadUserList(simpleRecyclerViewAdapter.getAdapterItemCount(), 10, appUser.getId());


//                simpleRecyclerViewAdapter.notifyDataSetChanged();

                /*
                  simpleRecyclerViewAdapter.insert("  Refresh things", 0);
                        ultimateRecyclerView.setRefreshing(false);
                        //   ultimateRecyclerView.scrollBy(0, -50);
                        linearLayoutManager.scrollToPosition(0);
//                        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
//                        simpleRecyclerViewAdapter.notifyDataSetChanged();
                        Log.e(TAG, "run: refresh");
                 */
            }
        });
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {

                presenter.loadList(simpleRecyclerViewAdapter.getAdapterItemCount() - 1, 10);
//                        SampleDataboxset.insertMore(simpleRecyclerViewAdapter, 10);
                //  linearLayoutManager.scrollToPositionWithOffset(maxLastVisiblePosition, -1);
                //  linearLayoutManager.scrollToPosition(maxLastVisiblePosition);
//                        simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                        simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                        simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                          linearLayoutManager.scrollToPositionWithOffset(maxLastVisiblePosition, -1);
                linearLayoutManager.scrollToPosition(maxLastVisiblePosition);
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.e(TAG, "onActivityCreated: " );
    }


    @Override
    public void setInfo2List(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {
        for (StreamsBean item : liveListItems) {
            simpleRecyclerViewAdapter.insert(item, simpleRecyclerViewAdapter.getAdapterItemCount());
        }

        if (hasPrevious) {
            ultimateRecyclerView.reenableLoadmore();
            linearLayoutManager.scrollToPosition(0);
        }

        if (liveListItems.size() < 10) {
            //没有更多了
            ultimateRecyclerView.disableLoadmore();
        }

        Log.d(TAG, "setInfo2List: 这次共加载了条数为" + liveListItems.size());

    }

    @Override
    public void setInfo2MasterList(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean isFirst) {

    }

    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {

        String playUrl = (playItem.getStatus() == 0) ? playItem.getRtmpLiveUrls() :playItem.getHlsPlaybackUrls();
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("isLive", playItem.getStatus() == 0);
        intent.putExtra("videoPath", playUrl);
        intent.putExtra("cid", String.valueOf(playItem.getRoomId()));//roomId
        intent.putExtra("anchorid", playItem.getUserId());
        intent.putExtra("viedoid", String.valueOf(playItem.getStreamId()));
        intent.putExtra("uname", String.valueOf(playItem.getName()));
        intent.putExtra("userimgurl", String.valueOf(playItem.getAvatar()));
        startActivity(intent);

    }

    @Override
    public void setupFragmentComponent() {

//        AppComponent appComponent = ((HomeActivity) getActivity()).getAppComponent();

        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenterComponent.inject(this);

    }

    @Override
    public void onfailed(String message) {
               super.onfaild(message);
    }

    @Override
    public void deleteStreamResult(String rs) {
        if (rs.equals("true")) {
            /*new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("成功")
                    .setContentText("视频已删除")
                    .show();*/
        } else {
            /*new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("失败")
                    .setContentText("删除失败")
                    .show();*/
        }

    }
}
