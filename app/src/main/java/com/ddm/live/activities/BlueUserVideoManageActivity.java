package com.ddm.live.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.SimpleUserHomeAdapter;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.ProgressBarDialogFragment;
import com.ddm.live.fragments.ShareBordFrament;
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
import com.ddm.live.ui.ActionSheetDialog;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.utils.ButtonUtils;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.views.iface.IListLiveView;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.rong.imlib.RongIMClient;

public class BlueUserVideoManageActivity extends BaseActivity implements IListLiveView, SimpleUserHomeAdapter.MyLiveListListener {

    private LiveListPresenter presenter;
    private UltimateRecyclerView ultimateRecyclerView;
    private boolean isLoadMore = false;
    private SimpleUserHomeAdapter simpleRecyclerViewAdapter;

    private LinearLayoutManager linearLayoutManager;
    private List<StreamsBean> listItems = new ArrayList<StreamsBean>();
    private StreamsBean liveItem;
    protected ShareBordFrament shareBordFrament;
    private AppComponent appComponent;
    private boolean hasNext = true;
    private int currentPage;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    //    private Dialog liveListDialog;
    private ProgressBarDialogFragment progressBarDialogFragment;
private StreamsBean deleteItem;
    @OnClick(R2.id.blue_video_manage_back_img)
    public void manageBackToUserHome() {
        finish();
    }

    @BindView(R2.id.headImageBitmap)
    ImageView headImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UILApplication.getInstance().addActivity(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.le_user_fans_list_title_background);
        setContentView(R.layout.content_blue_user_video_manage);
        ButterKnife.bind(this);
//        liveListDialog = DialogUtils.createLoadingDialog(this, "");
        progressBarDialogFragment = new ProgressBarDialogFragment();
        presenter.attachIView(this);
//        presenter.loadUserList(0, 5, appUserActivity.getId());

        presenter.loadUserList2(0, appUserActivity.getId());


        ultimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.ultimate_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        simpleRecyclerViewAdapter = new SimpleUserHomeAdapter(listItems, this, this);

        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadUserList2(1, appUserActivity.getId());

            }
        });

        simpleRecyclerViewAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.custom_bottom_progressbar, null));

//        ultimateRecyclerView.enableLoadmore();


        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                if (hasNext) {
                    isLoadMore = true;
                    presenter.loadUserList2(++currentPage, appUserActivity.getId());
                }
//                int count = simpleRecyclerViewAdapter.getAdapterItemCount() - 1;
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


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        presenter = presenterComponent.getLiveListPresenter();
    }

    @Override
    public void setInfo2List(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {
//        progressBarDialogFragment.dismiss();
        this.hasNext = hasNext;
        this.currentPage = currentPage;
        if (hasNext) {
            ultimateRecyclerView.enableLoadmore();
        } else {
            ultimateRecyclerView.disableLoadmore();
        }
        if (isLoadMore) {
            listItems.addAll(liveListItems);
            isLoadMore = false;
        } else {
            listItems = liveListItems;
        }
        simpleRecyclerViewAdapter.notifyDataChanged(listItems);

//        for (StreamsBean item : liveListItems) {
//            simpleRecyclerViewAdapter.insert(item, simpleRecyclerViewAdapter.getAdapterItemCount());
//        }
//
//        if (hasPrevious) {
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

    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {
        final SpecStreamBean playItem2 = playItem;
        final String userImgurl = String.valueOf(playItem.getAvatar());

        if (!userImgurl.isEmpty()) {
            Picasso.with(this).load(userImgurl)
                    .placeholder(R.mipmap.bg_error)
                    .error(R.mipmap.bg_error)
                    .into(headImageBitmap, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            headImageBitmap.setImageBitmap(BlurBitmap.blur(BlueUserVideoManageActivity.this, bitmap, false));
                            Bitmap bitmap2 = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            LiveViewFragment.bitmap = compressBitmap(bitmap2);
                            startViewPlayActivity(playItem2);
                        }

                        @Override
                        public void onError() {
                            startViewPlayActivity(playItem2);
                        }
                    });
        } else {
            startViewPlayActivity(playItem);
        }
    }

    public void startViewPlayActivity(SpecStreamBean playItem) {
//        progressBarDialogFragment.dismiss();
        PlayItem playItem1 = new PlayItem(this, playItem);
        startActivity(playItem1.play());
        overridePendingTransition(R.anim.activity_enter_from_bottom, 0);
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

    @Override
    public void deleteStreamResult(String rs) {
        if (rs.equals("true")) {
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("删除成功")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if(progressBarDialogFragment!=null && progressBarDialogFragment.isAdded()){
                        progressBarDialogFragment.dismiss();
                    }
                            sweetAlertDialog.dismiss();
//                            EventBus.getDefault().post(new RefreshUserHomeVideoListEvent());
                            listItems.remove(deleteItem);
                            simpleRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("删除失败")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if(progressBarDialogFragment!=null && progressBarDialogFragment.isAdded()){
                                progressBarDialogFragment.dismiss();
                            }
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void play(StreamsBean streamsBean) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        liveItem = streamsBean;
        if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
            Toast.makeText(BlueUserVideoManageActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        } else if (RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("NETWORK_UNAVAILABLE")) {
            Toast.makeText(BlueUserVideoManageActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
        } else if (!appUserActivity.isLogined() || RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("KICKED_OFFLINE_BY_OTHER_CLIENT")) {
            appUserActivity.setIsLogined(false);
            new SweetAlertDialog(BlueUserVideoManageActivity.this)
                    .setTitleText("请重新登录")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            startActivity(new Intent(BlueUserVideoManageActivity.this, BFLoginActivity.class));
                        }
                    })
                    .show();

        } else {
//                    if(!progressBarDialogFragment.isAdded()){
//                        progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
//                    }
            presenter.play2(String.valueOf(liveItem.getId()));
        }
    }

    @Override
    public void delete(final StreamsBean streamsBean) {
        deleteItem=streamsBean;
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("删除视频", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (!progressBarDialogFragment.isAdded()) {
                                    progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
                                }
                                presenter.deleteStream2(String.valueOf(streamsBean.getId()));
                            }
                        }).show();
    }

    @Override
    public void share(final StreamsBean streamsBean) {

        if(shareBordFrament==null){
            shareBordFrament=new ShareBordFrament();
        }
        if(!shareBordFrament.isAdded()){
            Bundle bundle=new Bundle();
            bundle.putString("avatarSmall",streamsBean.getAvatarSmall());
            bundle.putString("name",streamsBean.getName());
            bundle.putInt("qnId",streamsBean.getId());
            bundle.putString("topic",streamsBean.getSubject());
            shareBordFrament.setArguments(bundle);
            shareBordFrament.show(getSupportFragmentManager(),"share");
        }
    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
        simpleRecyclerViewAdapter.notifyDataChanged(listItems);
    }
}
