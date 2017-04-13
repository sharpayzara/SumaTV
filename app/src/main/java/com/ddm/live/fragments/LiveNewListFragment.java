package com.ddm.live.fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.adapters.NewListAdapter;
import com.ddm.live.adapters.SimpleAdapter;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.ddm.live.models.bean.live.PlayItem;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.views.iface.IListLiveView;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JiGar on 2016/10/13.
 */
public class LiveNewListFragment extends BaseFragment implements IListLiveView {

    private int moreNum = 100;
    int currentPage = 1;
    private boolean hasNext = true;
    private boolean isLoadMore = false;
    //    private Dialog liveListDialog;
    private ProgressBarDialogFragment progressBarDialogFragment;
    private ImageView headImageBitmap;

    private UltimateRecyclerView ultimateRecyclerView;

    private NewListAdapter newListAdapter;

    private LinearLayoutManager linearLayoutManager;

    private List<StreamsBean> listItems = new ArrayList<StreamsBean>();
    protected boolean isDrag = true, isEnableAutoLoadMore = true, status_progress = false;
    @Inject
    LiveListPresenter presenter;

    private StreamsBean liveItem;
    private AlertDialog dlg;
    private final CharSequence[] items = {"播放", "删除"};
    int count = 1;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return_back super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);

//        Log.d(TAG, "onCreateView: ");

//        Log.d(TAG, "onCreateView: " + presenter.toString());

//        liveListDialog = DialogUtils.createLoadingDialog(getContext(), "");
        progressBarDialogFragment = new ProgressBarDialogFragment();
        presenter.attachView(this);


        final View view = inflater.inflate(R.layout.fragment_live_new_list, container, false);
        headImageBitmap = (ImageView) view.findViewById(R.id.headImageBitmap);
        ultimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.live_new_list_view);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        //ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        newListAdapter = new NewListAdapter(listItems, this.getContext(), appUser, headImageBitmap);
//        ultimateRecyclerView.enableLoadmore();
        newListAdapter.setCustomLoadMoreView(LayoutInflater.from(this.getContext())
                .inflate(R.layout.custom_bottom_progressbar, null));
        ultimateRecyclerView.setAdapter(newListAdapter);

        presenter.loadList2(currentPage);


//点击播放
//       ultimateRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                Log.e(TAG, "onItemClick: " + position);
//
////                liveListDialog.show();
//                   progressBarDialogFragment.show(getActivity().getFragmentManager(),"");
//                liveItem = newListAdapter.getItem(position);
//
//                if (!appUser.isLogined()) {
//                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
//                    dlg.setTitle("请先登录");
//                    dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            FragmentManager fragmentManager = getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            UserCenterLoginFragment userCenterLoginFragment = new UserCenterLoginFragment();
//                            fragmentTransaction.replace(R.id.user_center_content, userCenterLoginFragment);
//                            fragmentTransaction.commit();
//                        }
//                    });
//                    dlg.show();
//
//                } else {
////                    if (appUser.getId().equals("30") || appUser.getId().equals("93") || appUser.getId().equals("26") || appUser.getId().equals("68")) {
//
//                        dlg = new AlertDialog.Builder(getContext()).setTitle("选择操作").setItems(items,
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        //这里item是根据选择的方式，   在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
//                                        if (which == 1) {
//                                            presenter.deleteStream2(String.valueOf(liveItem.getId()));
//                                        } else {
//    //                                        liveListDialog.show();
//        progressBarDialogFragment.show(getActivity().getFragmentManager(),"");
//                                            presenter.play2(String.valueOf(liveItem.getId()));
//                                        }
//                                    }
//                                }).create();
//                        dlg.show();
//
////                    } else {
////                        liveListDialog.show();
////        progressBarDialogFragment.show(getActivity().getFragmentManager(),"");
////                        presenter.play2(String.valueOf(liveItem.getId()));
////                    }
//
//                }
//            }
//        }));

        // StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(newListAdapter);
        // ultimateRecyclerView.addItemDecoration(headersDecor);


//        ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffff66ff"));
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count = newListAdapter.getAdapterItemCount();
                presenter.loadList2(1);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.e(TAG, "onActivityCreated: " );
    }


    @Override
    public void setInfo2List(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {
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
        newListAdapter.notifyDataChanged(listItems);

//        for (StreamsBean item : liveListItems) {
//            newListAdapter.insert(item, newListAdapter.getAdapterItemCount());
//        }
//
//        if (isFirst) {
//            ultimateRecyclerView.reenableLoadmore();
//            linearLayoutManager.scrollToPosition(0);
//        }
//
//        if (liveListItems.size() < 10) {
//            //没有更多了
//            ultimateRecyclerView.disableLoadmore();
//        }
//
//        Log.d(TAG, "setInfo2List: 这次共加载了条数为" + liveListItems.size());


    }

    @Override
    public void setInfo2MasterList(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean isFirst) {

    }

    public Bitmap compressBitmap(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 缩放图片的尺寸
        float scaleWidth = (float) 1 / 5;
        float scaleHeight = (float) 1 / 5;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
        return resizeBitmap;
    }

    public void startViewPlayActivity(SpecStreamBean playItem) {
        progressBarDialogFragment.dismiss();
        PlayItem playItem1 = new PlayItem(getActivity(), playItem);
        startActivity(playItem1.play());
    }

    @Override
    public void intentPlayerActivity(final SpecStreamBean playItem) {
        final String userImgurl = String.valueOf(playItem.getAvatar());

        if (!userImgurl.isEmpty()) {
            Picasso.with(getActivity()).load(userImgurl)
                    .placeholder(R.mipmap.bg_error)
                    .error(R.mipmap.bg_error)
                    .into(headImageBitmap, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            headImageBitmap.setImageBitmap(BlurBitmap.blur(getActivity(), bitmap, false));
                            Bitmap bitmap2 = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            LiveViewFragment.bitmap = compressBitmap(bitmap2);
                            startViewPlayActivity(playItem);
                        }

                        @Override
                        public void onError() {
                            startViewPlayActivity(playItem);
                        }
                    });
        } else {
            startViewPlayActivity(playItem);
        }

    }

    @Override
    public void setupFragmentComponent() {

        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenterComponent.inject(this);

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
        newListAdapter.notifyDataChanged(listItems);
    }

    @Override
    public void deleteStreamResult(String rs) {
        /*if (rs.equals("true")) {
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
        }*/

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshListEvent(RefreshNewListEvent refreshListEvent) {
        if (refreshListEvent.getMode() == 0) {
            presenter.loadList2(1);
        } else {
            presenter.loadList2(currentPage);
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
