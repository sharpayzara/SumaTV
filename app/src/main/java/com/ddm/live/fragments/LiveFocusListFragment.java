package com.ddm.live.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
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
import com.ddm.live.utils.DialogUtils;
import com.ddm.live.views.iface.IListLiveView;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JiGar on 2016/10/13.
 */
public class LiveFocusListFragment extends BaseFragment implements IListLiveView {

    private Dialog liveListDialog;
    private UltimateRecyclerView ultimateRecyclerView;
    private SimpleAdapter simpleRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<StreamsBean> listItems = new ArrayList<StreamsBean>();
    private StreamsBean liveItem;
    private ImageView headImageBitmap;
    private AlertDialog dlg;
    private final CharSequence[] items = {"播放", "删除"};
    int currentPage = 1;
    private boolean hasNext = true;
    private boolean isLoadMore = false;
    @Inject
    LiveListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        liveListDialog = DialogUtils.createLoadingDialog(getContext(), "");

        presenter.attachView(this);
        /**
         * SDK打包时：注释掉加载关注视频列表代码
         */
        //加载关注视频列表
        presenter.loadMasterList(0);

        final View view = inflater.inflate(R.layout.fragment_live_focus_list, container, false);
        ultimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.live_focus_list_view);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        headImageBitmap = (ImageView) view.findViewById(R.id.headImageBitmap);

        simpleRecyclerViewAdapter = new SimpleAdapter(listItems, this.getContext(), appUser, headImageBitmap);

        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);

        ultimateRecyclerView.disableLoadmore();

        /*ultimateRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                liveItem = simpleRecyclerViewAdapter.getItem(position);

                if (!appUser.isLogined()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                    dlg.setTitle("请先登录");
                    dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            UserCenterLoginFragment userCenterLoginFragment = new UserCenterLoginFragment();
                            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginFragment);
                            fragmentTransaction.commit();
                        }
                    });
                    dlg.show();

                } else {
                    if (appUser.getId().equals("30") || appUser.getId().equals("93") || appUser.getId().equals("26") || appUser.getId().equals("68")) {

                        dlg = new AlertDialog.Builder(getContext()).setTitle("选择操作").setItems(items,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //这里item是根据选择的方式，   在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
                                        if (which == 1) {
                                            presenter.deleteStream2(String.valueOf(liveItem.getId()));
                                        } else {
                                            liveListDialog.show();
                                            presenter.play2(String.valueOf(liveItem.getId()));
                                        }
                                    }
                                }).create();
                        dlg.show();

                    } else {
                        liveListDialog.show();
                        presenter.play2(String.valueOf(liveItem.getId()));
                    }

                }

            }
        }));*/

        // StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(simpleRecyclerViewAdapter);
        // ultimateRecyclerView.addItemDecoration(headersDecor);

//        ultimateRecyclerView.enableLoadmore();

        /*simpleRecyclerViewAdapter.setCustomLoadMoreView(LayoutInflater.from(this.getContext())
                .inflate(R.layout.custom_bottom_progressbar, null));*/


//        ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffff66ff"));
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadMasterList(1);
            }
        });
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                if (hasNext) {
                    isLoadMore = true;
                    presenter.loadList2(++currentPage);
                }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void toast(String message) {
        super.toast(message);
    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenterComponent.inject(this);
    }

    @Override
    public void setInfo2List(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {

    }

    @Override
    public void setInfo2MasterList(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {
        this.hasNext = hasNext;
        this.currentPage = currentPage;
        if (hasNext) {
            ultimateRecyclerView.enableLoadmore();
        } else {
            ultimateRecyclerView.disableLoadmore();
        }
        if (isLoadMore) {//加载更多
            listItems.addAll(liveListItems);
            isLoadMore = false;
        } else if (!hasPrevious) {//页面初现与再现刷新时处于第一页
            listItems = liveListItems;
        }
        simpleRecyclerViewAdapter.notifyDataChanged(listItems);
    }

    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {
        liveListDialog.cancel();
        String playUrl = (playItem.getStatus() == 0) ? playItem.getRtmpLiveUrls() : playItem.getHlsPlaybackUrls();

        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("isLive", playItem.getStatus() == 0);
        intent.putExtra("videoPath", playUrl);
        intent.putExtra("cid", String.valueOf(playItem.getRoomId()));//roomId
        intent.putExtra("anchorid", playItem.getUserId());
        intent.putExtra("viedoid", String.valueOf(playItem.getStreamId()));
        intent.putExtra("uname", String.valueOf(playItem.getName()));
        intent.putExtra("userimgurl", String.valueOf(playItem.getAvatar()));
        intent.putExtra("id", String.valueOf(playItem.getId()));
        intent.putExtra("userNumber", String.valueOf(playItem.getUserNumber()));
        startActivity(intent);
    }

    @Override
    public void deleteStreamResult(String rs) {
        if (rs.equals("true")) {
            liveListDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("删除成功");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.i(TAG, "删除视频成功");
                        }
                    });
            builder.show();
        } else {
            liveListDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("删除失败");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.i(TAG, "删除视频失败");
                        }
                    });
            builder.show();
        }
    }

    @Override
    public void onfailed(String message) {
        liveListDialog.cancel();
        simpleRecyclerViewAdapter.notifyDataChanged(listItems);
        super.onfaild(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMasterListEvent(RefreshMasterListEvent refreshMasterListEvent) {
        if (refreshMasterListEvent.getMode() == 0) {
            presenter.loadMasterList(1);
        } else {
            presenter.loadMasterList(currentPage);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
//            return;
//        }
//        presenter.loadList2(currentPage);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
