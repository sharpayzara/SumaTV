package com.ddm.live.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.adapters.UserHomePageListAdapter;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.MaskFragment;
import com.ddm.live.fragments.RefreshUserHomeVideoListEvent;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.models.bean.live.PlayItem;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.presenter.UserInfoPresenter;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.utils.ButtonUtils;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.utils.ViewUtils;
import com.ddm.live.utils.refreshlayout.PullToRefreshLayout;
import com.ddm.live.views.iface.IListLiveView;
import com.ddm.live.views.iface.IUserCenterView;
import com.ddm.live.views.iface.IUserInfoBaseView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.RongIMClient;

public class UserInfoHomeActivity extends BaseActivity implements IListLiveView, IUserCenterView, IUserInfoBaseView {
    @OnClick(R2.id.user_home_title_back_img)
    public void goBack() {
        finish();
    }

    @BindView(R2.id.user_home_head_img)
    ImageView imgUserIcon;

    @BindView(R2.id.user_home_user_name)
    TextView txUserName;

    @BindView(R2.id.user_home_user_id_num)
    TextView txUserID;

    @BindView(R2.id.user_home_user_description)
    TextView txUserDescription;

    @BindView(R2.id.user_home_headImageBitmap)
    ImageView headImageBitmap;

    @OnClick(R2.id.user_home_fans_layout)
    public void toUserFansList() {
        Intent intent = new Intent();
        intent.setClass(UserInfoHomeActivity.this, FansListActivity.class);
        intent.putExtra("uID", uID);
        startActivity(intent);
    }

    @BindView(R2.id.user_home_user_fan_num)
    TextView txUserFansNum;

    @OnClick(R2.id.user_home_focus_layout)
    public void toUserFocusList() {
        Intent intent = new Intent();
        intent.setClass(UserInfoHomeActivity.this, FocusListActivity.class);
        intent.putExtra("uID", uID);
        startActivity(intent);
    }

    @BindView(R.id.user_home_user_focus_num)
    TextView txUserFocusNum;

    @BindView(R2.id.user_home_add_focus_img)
    ImageView imgAddFocusImg;

    @BindView(R2.id.user_home_add_focus)
    TextView txAddFocusBtn;

    @BindView(R2.id.user_home_add_focus_layout)
    LinearLayout focusLayout;

    @OnClick(R2.id.user_home_add_focus_layout)
    public void addBtn() {
        if (userInformation == null) return;
        if (appUserActivity.getId().equals(userInformation.getId())) {
            toast("不能关注自己哦");
            return;
        }
        Integer[] ids = {uID};
        if (isFollow) {
            userCenterPresenter.userCancelFocusMaster(uID);
        } else {
            userCenterPresenter.userFocusMaster(ids);
        }
    }

    @BindView(R2.id.user_home_user_video_list)
    ListView lvUserVideoList;
    @BindView(R2.id.fans_contribution_headimage1)
    CircleImageView fansContributionHeadimag1;
    @BindView(R2.id.fans_contribution_headimage2)
    CircleImageView fansContributionHeadimag2;
    @BindView(R2.id.fans_contribution_headimage3)
    CircleImageView fansContributionHeadimag3;

    @OnClick(R2.id.fans_contribution_btn)
    public void fansContributionBtn() {
        Intent intent = new Intent(this, FansContributionListActivity.class);
        intent.putExtra("uID", uID);
        startActivity(intent);
    }

    @BindView(R2.id.user_home_user_gender)
    ImageView ivUserGender;

    @BindView(R2.id.user_home_user_level)
    ImageView ivUserLevel;

    @BindView(R2.id.refresh_view)
    PullToRefreshLayout pullToRefreshLayout;

    private List<StreamsBean> streamInfoList = new ArrayList<StreamsBean>();
    private SpecUserInfoBean userInformation;
    private LiveListPresenter liveListPresenter;
    private UserCenterPresenter userCenterPresenter;
    private UserInfoPresenter userInfoPresenter;
    private Integer uID;
    private String userImg;
    private String userName;
    private Integer level;
    private int gender;
    private String sign;
    private Integer userNumber;
    private Integer fansCount;
    private Integer masterCount;
    private int currentPage = 1;
    private boolean isFollow;
    private boolean isSelf;
    private boolean hasNext;
    private MaskFragment maskFragment;
    private UserHomePageListAdapter userHomePageListAdapter;
    private CircleImageView[] fansContributionHeadImage;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private Date curDate;


    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        UILApplication.getInstance().addActivity(this);
        StatusBarUtils.setWindowStatusBarColor(UserInfoHomeActivity.this, R.color.xz_font_purple);//设置状态栏颜色
        setContentView(R.layout.activity_user_info_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        fansContributionHeadImage = new CircleImageView[]{fansContributionHeadimag1, fansContributionHeadimag2, fansContributionHeadimag3};
//        progressBarDialogFragment = new ProgressBarDialogFragment();
        maskFragment = new MaskFragment();
        checkData();
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        userHomePageListAdapter = new UserHomePageListAdapter(UserInfoHomeActivity.this, streamInfoList, isSelf, headImageBitmap);
        lvUserVideoList.setAdapter(userHomePageListAdapter);

        lvUserVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
                    Toast.makeText(UserInfoHomeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (RongYunPresenter.isInLivingRoom) {
                    toast("正在直播间中，赶快添加关注吧");
                } else if (RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("NETWORK_UNAVAILABLE")) {
                    Toast.makeText(UserInfoHomeActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                } else if (!appUserActivity.isLogined() || RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("KICKED_OFFLINE_BY_OTHER_CLIENT")) {
                    String status = RongIMClient.getInstance().getCurrentConnectionStatus().name();
                    boolean is = appUserActivity.isLogined();
                    appUserActivity.setIsLogined(false);
                    new SweetAlertDialog(UserInfoHomeActivity.this)
                            .setTitleText("请重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    startActivity(new Intent(UserInfoHomeActivity.this, BFLoginActivity.class));
                                }
                            })
                            .show();

                } else {
                    if (maskFragment != null && !maskFragment.isAdded()) {
                        maskFragment.show(getFragmentManager(), "");
                    }
                    liveListPresenter.play2(String.valueOf(streamInfoList.get(position).getId()));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void checkData() {
        Intent intent = getIntent();
        uID = intent.getIntExtra("uID", 0);
        userName = intent.getStringExtra("username");
        userImg = intent.getStringExtra("userImg");
        level = intent.getIntExtra("level",1);
        userNumber = intent.getIntExtra("userNumber", 0);
        sign = intent.getStringExtra("sign");
        gender = intent.getIntExtra("gender",0);
        isFollow=intent.getBooleanExtra("isFollowed",false);
        fansCount=intent.getIntExtra("fansCount",0);
        masterCount=intent.getIntExtra("masterCount",0);
        initUI(userImg, userName, gender, level, userNumber, sign, fansCount, masterCount,isFollow);

        if (uID.equals(appUserActivity.getId())) {
            isSelf = true;
            focusLayout.setVisibility(View.GONE);
            ViewUtils.setMargins(pullToRefreshLayout, 0, 0, 0, 0);
        } else {
            isSelf = false;
        }
    }

    private void initData() {
        if (isRefresh) {
            curDate = new Date(System.currentTimeMillis());
            liveListPresenter.loadUserList2(1, uID);
        } else {
            liveListPresenter.loadUserList2(currentPage, uID);
        }
    }

    private void initUI(String userImg, String userName, int gender, Integer level, Integer userNumber, String sign, Integer fansCount, Integer masterCount,boolean isFollow) {
        lvUserVideoList.setFocusable(false);
        txUserName.setText(userName);
        txUserID.setText(String.valueOf(userNumber));
        txUserFansNum.setText(String.valueOf(fansCount));
        txUserFocusNum.setText(String.valueOf(masterCount));

        if (sign.isEmpty()) {
            txUserDescription.setText(R.string.user_introduction);
        } else {
            txUserDescription.setText(sign);
        }

        if (userImg == null || userImg.isEmpty()) {
            imgUserIcon.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(this)
                    .load(userImg)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(imgUserIcon);
        }

        if (0==gender) {
            ivUserGender.setImageResource(R.mipmap.ic_profile_female);
        } else {
            ivUserGender.setImageResource(R.mipmap.ic_profile_male);
        }
        String drawableName = "level_" + level;
        int drawResId = getDrawResourceID(drawableName);
        ivUserLevel.setImageResource(drawResId);

        if (isFollow) {
            txAddFocusBtn.setText("取消关注");
            imgAddFocusImg.setImageResource(R.mipmap.user_home_focused);
            isFollow = true;
        } else {
            txAddFocusBtn.setText("关注主播");
            imgAddFocusImg.setImageResource(R.mipmap.user_home_focus);
            isFollow = false;
        }
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
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

        liveListPresenter = presenterComponent.getLiveListPresenter();
        liveListPresenter.attachIView(this);

        userCenterPresenter = presenterComponent.getUserCenterPresenter();
        userCenterPresenter.attachView(this);

        userInfoPresenter = presenterComponent.getUserInfoPresenter();
        userInfoPresenter.attachView(this);
    }

    @Override
    public void getUserInfo(final SpecUserInfoBean userInfo) {
        userInformation = userInfo;
        userName = userInformation.getName();
        userNumber = userInformation.getUserNumber();
        fansCount = userInformation.getFans().getData().getFansCount();
        masterCount = userInformation.getFans().getData().getMastersCount();
        gender = userInformation.getGender();
        sign = userInformation.getSign();
        userImg = userInformation.getAvatarSmall();
        isFollow=userInformation.getFans().getData().getIsFollowed();

        initUI(userImg, userName, gender, level, userNumber, sign, fansCount, masterCount,isFollow);

    }

    @Override
    public void setInfo2List(final List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {

        Integer[] ids = {uID};
        userCenterPresenter.getUserInfo(ids);
        //查询榜单
        userInfoPresenter.searchFansContributionList(uID);

        this.currentPage = currentPage;
        if (hasNext) {
            this.hasNext = true;
        } else {
            this.hasNext = false;
        }
        if (isLoadMore) {
            streamInfoList.addAll(liveListItems);
            userHomePageListAdapter.notifyDataChange(streamInfoList);
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            isLoadMore = false;
        } else if (isRefresh) {
            Date endDate = new Date(System.currentTimeMillis());
            long diff = endDate.getTime() - curDate.getTime();
            if (diff < 3000) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        streamInfoList = liveListItems;
                        userHomePageListAdapter.notifyDataChange(streamInfoList);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                    }
                }.sendEmptyMessageDelayed(0, 3000 - diff);
            }
        } else if (!hasPrevious) {
            streamInfoList = liveListItems;
            userHomePageListAdapter.notifyDataChange(streamInfoList);
        }
    }

    @Override
    public void setInfo2MasterList(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean isFirst) {

    }

    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {
        final SpecStreamBean playItem2 = playItem;
        final String userImgUrl = String.valueOf(playItem.getAvatar());
        if (!userImgUrl.isEmpty()) {
            Picasso.with(this).load(userImgUrl)
                    .placeholder(R.mipmap.bg_error)
                    .error(R.mipmap.bg_error)
                    .into(headImageBitmap, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            headImageBitmap.setImageBitmap(BlurBitmap.blur(UserInfoHomeActivity.this, bitmap, false));
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

    private void startViewPlayActivity(SpecStreamBean playItem) {
        if (maskFragment != null && maskFragment.isAdded()) {
            maskFragment.dismiss();
        }
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
    protected void onPause() {
        super.onPause();
        Log.e("zz", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("zz", "onRestart");
    }

    @Override
    public void deleteStreamResult(String rs) {

    }

    @Override
    public void onfailed(String message) {
             super.onfaild(message);
    }

    @Override
    public void addFocusResult(boolean result, String msg) {
        if (result) {
            toast(msg);
//            Integer[] ids = {Integer.parseInt(uID)};
//            userCenterPresenter.getUserInfo(ids);
            String fansNum = Integer.parseInt(txUserFansNum.getText() + "") + 1 + "";
            txUserFansNum.setText(fansNum);
            txAddFocusBtn.setText("取消关注");
            imgAddFocusImg.setImageResource(R.mipmap.user_home_focused);
            isFollow = true;
        } else {
            toast(msg);
            txAddFocusBtn.setText("关注主播");
            imgAddFocusImg.setImageResource(R.mipmap.user_home_focus);
            isFollow = false;
        }
    }

    @Override
    public void cancelFocusResult(boolean result, String msg) {
        if (result) {
//            Integer[] ids = {Integer.parseInt(uID)};
//            userCenterPresenter.getUserInfo(ids);
            String fansNum = Integer.parseInt(txUserFansNum.getText() + "") - 1 + "";
            txUserFansNum.setText(fansNum);
            toast(msg);
            txAddFocusBtn.setText("关注主播");
            imgAddFocusImg.setImageResource(R.mipmap.user_home_focus);
            isFollow = false;
        } else {
            toast(msg);
            txAddFocusBtn.setText("取消关注");
            imgAddFocusImg.setImageResource(R.mipmap.user_home_focused);
            isFollow = true;
        }
    }

    @Override
    public void changeResult() {

    }

    //获取粉丝贡献榜
    @Override
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList, String total) {
        for (int i = 0; i < 3 && i < fansContributionInfoBeanList.size(); i++) {

            String headImageUrl = fansContributionInfoBeanList.get(i).getAvatarSmall();
            if (TextUtils.isEmpty(headImageUrl)) {
                fansContributionHeadImage[i].setImageResource(R.mipmap.bg_error);
            } else {
                Picasso.with(this)
                        .load(headImageUrl)
                        .placeholder(R.mipmap.bg_error)
                        .into(fansContributionHeadImage[i]);
            }
            fansContributionHeadImage[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getUserInfoResult(List<UserInfoBean> userInfoBeen) {

    }

    @Override
    public void getUploadToken(String token, String host) {

    }

    /**
     * 根据图片的名称获取对应的资源id
     *
     * @param resourceName
     * @return
     */
    public int getDrawResourceID(String resourceName) {
        Resources res = this.getResources();
        int picid = res.getIdentifier(resourceName, "mipmap", this.getPackageName());
        return picid;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Picasso.with(this).invalidate(userInformation.getAvatarSmall());//回收主播头像缓存
//            for (StreamsBean streamsBean : streamInfoList) {
//                Picasso.with(this).invalidate(streamsBean.getAvatar());//回收视频列表头像缓存
//                Picasso.with(this).invalidate(streamsBean.getAvatarSmall());//回收视频列表背景图片缓存
//            }
            finish();
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(RefreshUserHomeVideoListEvent tmp) {
        userHomePageListAdapter = null;
        initData();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        userInformation = null;
        liveListPresenter = null;
        userCenterPresenter = null;
        userInfoPresenter = null;
        System.gc();
        super.onDestroy();
    }

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {

    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            isRefresh = true;
            initData();
            /*new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);*/
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            if (hasNext) {
                // 加载操作
                isLoadMore = true;
                curDate = new Date(System.currentTimeMillis());
                liveListPresenter.loadUserList2(++currentPage, uID);
            } else {
                isLoadMore = false;
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
            /*new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件加载完毕了哦！
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);*/
        }

    }
}
