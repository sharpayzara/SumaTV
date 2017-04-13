package com.ddm.live.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.activities.FansContributionListActivity;
import com.ddm.live.activities.RechargeActivity;
import com.ddm.live.adapters.AudienceAdapter;
import com.ddm.live.adapters.GiftPagerAdapter;
import com.ddm.live.adapters.MessageAdapter;
import com.ddm.live.adapters.PlayGiftAdapter;
import com.ddm.live.animation.HorizontalListView;
import com.ddm.live.animation.SoftKeyBoardListener;
import com.ddm.live.constants.Constants;
import com.ddm.live.models.bean.account.BuyVirtualCoinsResponse;
import com.ddm.live.models.bean.account.GetRechargeListResponseData;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;
import com.ddm.live.models.bean.common.accountbeans.AccountTypesDataBean;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.ddm.live.models.bean.common.giftbeans.GiftData;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.models.bean.msgtype.LiveContentMessage;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserListMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;
import com.ddm.live.models.bean.msgtype.MsgGiftBean;
import com.ddm.live.models.bean.msgtype.MsgUserInfoBean;
import com.ddm.live.ui.DensityUtils;
import com.ddm.live.utils.DateUtils;
import com.ddm.live.utils.DrawableSettingUtils;
import com.ddm.live.utils.RongYunListenerContext;
import com.ddm.live.utils.Utils;
import com.ddm.live.views.iface.IAccountView;
import com.ddm.live.views.iface.IBaseFragmentLayerView;
import com.ddm.live.views.iface.IGiftListView;
import com.ddm.live.views.iface.IOnTouchView;
import com.ddm.live.views.iface.IRongYunView;
import com.ddm.live.views.iface.IUserInfoBaseView;
import com.ddm.live.views.iface.IVideoPlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opendanmaku.DanmakuView;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import me.yifeiyuan.library.PeriscopeLayout;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 收看端控件层
 */
public class WatcherLayerFragment extends LiveWatcherBaseFragment implements
        IRongYunView,
        IVideoPlayerView,
        IAccountView,
        IOnTouchView,
        IGiftListView,
        IUserInfoBaseView, PersionalInfoPanelFragment.OnAtUserListener {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case USERJOINMSGDIMISS:
                    userJoin.setVisibility(View.GONE);
                    userJoinTv.setVisibility(View.INVISIBLE);
                    if (getContext() != null) {
                        ainmator(userJoin, "translationX", 0, Utils.dip2px(getContext(), -300), 500l);
                    }
                    break;
                case CURRTIME_CHANGE:
                    currtime++;
                    progress.setProgress((int) (currtime * 100 / totaltime));
                    currTime.setText(DateUtils.formatTime(currtime));
                    sendProgressChangeMsg();
                    break;
            }
        }
    };

    /**
     * 标示判断
     */
    private boolean isOpen;
    private long liveTime;


    /**
     * 界面相关
     */

    private LinearLayout headLayout;
    private LinearLayout input;
    private LinearLayout msgLayout;
    private LinearLayout llInputParent;
    private LinearLayout llGiftLayout;
    //    private LinearLayout llgiftcontent;
    private RelativeLayout scrollView;
    private RelativeLayout rlsentimenttime;
    private HorizontalListView hlvaudience;
    private HorizontalScrollView headScroll;

    private PersionalInfoPanelFragment persionalInfoPanel;
    private ProgressBarDialogFragment progressBarDialogFragment;

    private IBaseFragmentLayerView iBaseFragmentLayerView;
    private BFPlayOverFragment bfPlayOverFragment;
    private InputDialogFragment inputDialogFragment;
    private SpecUserInfoBean masterInfoBean;

    private List<ImageView> giftDots = new ArrayList<>();
//    private List<SpecUserInfoBean> userInfoList = new ArrayList<>();
//    private ListView lvmessage;
//    private List<View> giftViewCollection = new ArrayList<View>();
//    private List<String[]> messageData = new LinkedList<>();
//    private List<GiftData> giftDataList;

    private boolean isFocus = false;
    private UMImage image;
    private PeriscopeLayout periscopeLayout;
    private boolean isPaused = false;
    private boolean isJumpedOut = false;//调到别的界面，或进入后台的时候视频结束了
    /**
     * 动画相关
     */
//    private NumAnim giftNumAnim;
//    private TranslateAnimation inAnim;
//    private TranslateAnimation outAnim;
//    private AnimatorSet animatorSetHide = new AnimatorSet();
//    private AnimatorSet animatorSetShow = new AnimatorSet();

    private TextView leId;
    private TextView userName;
    private TextView tvtime;
    private TextView tvdate;
    private TextView lebiText;
    private TextView diamondsText;
    private TextView txTotalPeople;
    private ImageView masterLevelImage;

    private String TAG = "WatcherLayerFragment2";
    private String videoID;
    private Integer anchorID;
    private Integer qid;
    private String uname;
    private String userNumber;
    private int gender;
    private String sign;
    private String headimgUrl;
    private String avatarSmall;
    private String messageContent;
    private boolean isLive;//是直播还是回看
    private boolean isPlay = true;//回看是播放、、、暂停
    private int totalWatched;
    private final int MOBILE_QUERY = 1;
    private static final int REQ_DELAY_MILLS = 3000;
    private static final int MSG_ADD_VIEW = 0x00;
    private final int CURRTIME_CHANGE = 4785451;//当前时间变化的消息
    private int tagImgID;

    private int sWidth, sHeight;
    private int currentPosition;
    private int userBalance;
    private int SEARCH_ROOM_MEMBERS_INFO = 0;
    private int payNum;

    //    private MessageAdapter messageAdapter;
//    private AudienceAdapter audienceAdapter;
    private PlayGiftAdapter giftAdapter;

    public int getWatchers() {
//        return totalPeople;
        return totalWatched;
    }

    public void attachView(IBaseFragmentLayerView iBaseFragmentLayerView) {
        this.iBaseFragmentLayerView = iBaseFragmentLayerView;
    }

    @BindView(R2.id.tvChat)
    LinearLayout tvChat;
    @BindView(R2.id.presentBtn)
    ImageView giftBtn;
    @BindView(R2.id.bf_back_btn)
    ImageView mBackBtn;
    @BindView(R2.id.shareBtn)
    ImageView shareBtn;
    @BindView(R2.id.play_user_money)
    LinearLayout playUserBtn;
    @BindView(R2.id.msg_scroll_view)
    ScrollView sView;
    @BindView(R2.id.focus_btn)
    TextView focusBtn;
    @BindView(R2.id.headImage)
    ImageView headImage;

    @BindView(R2.id.bf_room_people_num_txt)
    TextView txTotalPeopleWord;
    @BindView(R2.id.user_join)
    RelativeLayout userJoin;
    @BindView(R2.id.user_join_tv)
    TextView userJoinTv;
    @BindView(R.id.play_gift_pager)
    ViewPager giftPager;
    @BindView(R.id.ll_gift_dot)
    LinearLayout giftDotLayout;
    @BindView(R.id.review_ui)
    RelativeLayout reviewLayout;
    @BindView(R.id.tv_start_pause)
    ImageView startPauseBtn;
    @BindView(R.id.media_progress)
    SeekBar progress;
    @BindView(R.id.total_time)
    TextView totalTime;
    @BindView(R.id.curr_time)
    TextView currTime;
    @BindView(R.id.layout_bottom_button)
    LinearLayout layoutBottomButton;//底部礼物、分享等button的父控件

    @OnClick(R2.id.fans_contribution_list_go_btn)
    public void fansContribution() {
        Intent intent = new Intent(getActivity(), FansContributionListActivity.class);
        intent.putExtra("uID", bundle.getInt("anchorID", 0));
        startActivity(intent);
    }

    @OnClick(R2.id.tvChat)
    public void tvChat() {
        showChat("");
    }

    @OnClick(R2.id.presentBtn)
    public void presentBtn() {
        llGiftLayout.setVisibility(View.VISIBLE);
        bottomShow(false);
    }

    @OnClick(R2.id.bf_back_btn)
    public void back() {
        stopPlay();
    }

    @OnClick(R2.id.shareBtn)
    public void share() {
//        new ShareAction(getActivity())
//                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.ALIPAY.QQ,SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
//                .setShareboardclickCallback(new ShareBoardlistener() {
//                    @Override
//                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//                        new ShareAction(getActivity()).
//                                setPlatform(share_media).setCallback(umShareListener)
//                                .withMedia(image)
//                                .withTitle(Constants.SHARE_TITLE)
//                                .withText(getResources().getString(R.string.xiaozhu_share_pre_title) + uname + getResources().getString(R.string.xiaozhu_share_after_title))
//                                .withTargetUrl(Constants.SHARE_SERVER_URL + qid + "/")
//                                .setCallback(umShareListener)
//                                .share();
//                    }
//                }).open();
        if (shareBordFrament == null) {
            shareBordFrament = new ShareBordFrament();
        }
        if (!shareBordFrament.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putString("avatarSmall", avatarSmall);
            bundle.putString("name", uname);
            bundle.putInt("qnId", qid);
            bundle.putString("topic", topic);
            shareBordFrament.setArguments(bundle);
            shareBordFrament.show(getActivity().getSupportFragmentManager(), "share");
        }

    }

    @OnClick(R2.id.play_user_money)
    public void plauUserMoney() {
        startActivity(new Intent(getActivity(), RechargeActivity.class));
    }

    @OnClick(R.id.gift_send)
    public void sendGift () {
        if (appUser.getId().equals(anchorID)) {
            toast("不能送给自己礼物哦");
            return;
        }
        payNum = Integer.valueOf(giftDataList.get(currentPosition).getToGold());
//                giftAdapter.setIsSelected(position);
//                giftAdapter.notifyDataSetChanged();
        buyVirtualCoins(payNum, anchorID, currentPosition, Constants.MSG_TYPE_GIFT);
    }

    @OnClick(R2.id.focus_btn)
    public void focus() {
        Integer[] ids = {bundle.getInt("anchorID", 0)};
        videoPlayerPresenter.userFocusMaster(ids);
    }

    @OnClick(R2.id.headImage)
    public void onHeadimgeClick() {
        persionalInfoPanel = new PersionalInfoPanelFragment();
        persionalInfoPanel.attachFramgent(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", masterInfoBean);
        bundle.putInt("anchorId", anchorID);
        bundle.putBoolean("isLive",isLive);
        persionalInfoPanel.setArguments(bundle);
        persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "PersionalInfoPanel");
        persionalInfoPanel.setOnAtUserListener(this);
    }

    @OnClick(R.id.tv_start_pause)
    public void onStartOrPause() {
        if (isPlay) {
            startPauseBtn.setImageResource(R.mipmap.liveshow_playback_btn_selected);
            removeProgressChangeMsg();
        } else {
            startPauseBtn.setImageResource(R.mipmap.liveshow_playback_btn_nor);
            sendProgressChangeMsg();
        }
        isPlay = !isPlay;
        if (onPlayPauseChangeListener != null)
            onPlayPauseChangeListener.onPlayPauseChange(isPlay);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_layer, container, false);

        ButterKnife.bind(this, view);

        ainmator(userJoin, "translationX", 0, Utils.dip2px(getContext(), -300), 1);

        progressBarDialogFragment = new ProgressBarDialogFragment();
        Integer[] ids = {bundle.getInt("anchorID", 0)};
        videoPlayerPresenter.queryFocusedStatus(ids, 0);

        scrollView = (RelativeLayout) view.findViewById(R.id.scroll_view);
        rlsentimenttime = (RelativeLayout) view.findViewById(R.id.rlsentimenttime);
        hlvaudience = (HorizontalListView) view.findViewById(R.id.hlvaudience);
        hlvaudience.setOnItemClickListener(new AdapterView.OnItemClickListener() {//头像列表头像监听事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInfo", userInfoList.get(position));
                bundle.putInt("anchorId", anchorID);
                bundle.putBoolean("isLive",isLive);
                persionalInfoPanel = new PersionalInfoPanelFragment();
                persionalInfoPanel.setArguments(bundle);
                persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "PersionalInfoPanel");
                persionalInfoPanel.setOnAtUserListener(WatcherLayerFragment.this);
            }
        });
        hlvaudience.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showChat("@"+userInfoList.get(position).getName());
                return true;
            }
        });

        tvtime = (TextView) view.findViewById(R.id.tvtime);
        tvdate = (TextView) view.findViewById(R.id.tvdate);
        llgiftcontent = (LinearLayout) view.findViewById(R.id.llgiftcontent);
        lvmessage = (ListView) view.findViewById(R.id.lvmessage);
        llInputParent = (LinearLayout) view.findViewById(R.id.llinputparent);
//        etInput = (EditText) view.findViewById(R.id.etInput);
        mDanmakuView = (DanmakuView) view.findViewById(R.id.danmakuView);

        userName = (TextView) view.findViewById(R.id.player_user_name);

        focusBtn.setVisibility(View.GONE);
        // 聊天室用户头像ScrollView
        headScroll = (HorizontalScrollView) view.findViewById(R.id.blue_play_room_user_view);
        headLayout = (LinearLayout) view.findViewById(R.id.blue_play_room_user_layout);
        leId = (TextView) view.findViewById(R.id.leid);
        masterLevelImage = (ImageView) view.findViewById(R.id.master_level_tag);
        // 聊天室消息ScrollView
        msgLayout = (LinearLayout) view.findViewById(R.id.msg_linear_layout);

        llGiftLayout = (LinearLayout) view.findViewById(R.id.play_gift_layout);

        txTotalPeople = (TextView) view.findViewById(R.id.bf_room_people_num);

        lebiText = (TextView) view.findViewById(R.id.lebi_value);
        diamondsText = (TextView) view.findViewById(R.id.diamonds_value);

        ViewGroup.LayoutParams lp = sView.getLayoutParams();
        sWidth = DensityUtils.getScreenW(getActivity()) / 7 * 5;
        sHeight = DensityUtils.getScreenH(getActivity()) / 5 * 2;
        lp.width = sWidth;
        lp.height = sHeight;

        periscopeLayout = (PeriscopeLayout) view.findViewById(R.id.periscope);
        periscopeLayout.getLayoutParams().height = sHeight;
//        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in);
//        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out_left);
//        giftNumAnim = new NumAnim();
        clearTiming();
        initView();
        messageAdapter = new MessageAdapter(getActivity(), messageData);
        lvmessage.setAdapter(messageAdapter);
        lvmessage.setSelection(messageData.size());
        lvmessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                String[] data = messageData.get(position);
                persionalInfoPanel = new PersionalInfoPanelFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("anchorId", anchorID);
                bundle.putBoolean("isLive",isLive);
                SpecUserInfoBean masterInfoBean = new SpecUserInfoBean();
                masterInfoBean.setId(Integer.parseInt(data[0]));
                masterInfoBean.setName(data[1]);
                masterInfoBean.setAvatar("");
                masterInfoBean.setAvatarSmall(data[3]);
                masterInfoBean.setGender(Integer.parseInt(data[5]));
                masterInfoBean.setUserNumber(0);
                masterInfoBean.setLevel(Integer.parseInt(data[6]));
                masterInfoBean.setSign("");
                masterInfoBean.setFansNumber(0);
                masterInfoBean.setFansNumber(0);
                bundle.putSerializable("userInfo", masterInfoBean);
                persionalInfoPanel.setArguments(bundle);
                persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "LiveMasterPersionalInfoPanel");
            }
        });
        initRongYun();
        // 每2秒自动根据直播间人数自动添加心形效果
        timeHandler.removeCallbacks(runnable);
        timeHandler.postDelayed(runnable, 1000);
        setProgress();
        if (isLive) {//观看直播显示用户列表、消息列表
            hlvaudience.setVisibility(View.VISIBLE);
            lvmessage.setVisibility(View.VISIBLE);
        }
        else if(anchorID.equals(appUser.getId())) {
            giftBtn.setVisibility(View.GONE);
        }
        return view;
    }

    private int currProgress;
    private boolean fromUser;
    private long totaltime;
    private int currtime;

    public void setTotalTime(long totaltime) {
        if (isLive)
            return;
        this.totaltime = totaltime;
        totalTime.setText(DateUtils.formatTime((int) totaltime));
    }

    /**
     * 设置进度条的相关回调
     */
    private void setProgress() {
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isReview) {
                    isReview = false;
                } else {
                    WatcherLayerFragment.this.fromUser = fromUser;
                    currProgress = progress;
                    currtime = (int) (progress * totaltime / 100);
                    currTime.setText(DateUtils.formatTime((int) (progress * totaltime / 100)));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                removeProgressChangeMsg();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (fromUser) {
                    if (onProgressChangeListener != null) {
                        onProgressChangeListener.onProgressChange(currProgress * totaltime * 1000);
                    }
                }
            }
        });
    }

    private Handler timeHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //要做的事情

            if (totalPeople < 5) {
                for (int i = 0; i < 6; i++) {
                    periscopeLayout.addHeart();
                }
                timeHandler.postDelayed(this, 2000);
            }
            if (5 < totalPeople && totalPeople < 10) {
                for (int i = 0; i < 15; i++) {
                    periscopeLayout.addHeart();
                }
                timeHandler.postDelayed(this, 2000);
            }
            if (totalPeople > 10) {
                for (int i = 0; i < 25; i++) {
                    periscopeLayout.addHeart();
                }
                timeHandler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llGiftLayout.getVisibility() == View.VISIBLE) {

                    llGiftLayout.setVisibility(View.INVISIBLE);
                    bottomShow(true);
                } else {
                    periscopeLayout.addHeart();
                    rongSendStatusMessage(2);//发送点赞消息
//                    LiveUserMessage userMessageLight = new LiveUserMessage("点亮了", Constants.MSG_TYPE_LIGHT, getMsgUserInfoBean2());
//                    setLiveUserMessageScroll2(userMessageLight, messageAdapter, messageData, lvmessage);
                }
            }
        });
        softKeyboardListnenr();
        startTimer();
    }

    private void bottomShow(boolean show) {
        if(show) {
            layoutBottomButton.setVisibility(View.VISIBLE);
            if(isLive)
                tvChat.setVisibility(View.VISIBLE);
            else
                reviewLayout.setVisibility(View.VISIBLE);
        }
        else {
            layoutBottomButton.setVisibility(View.GONE);
            if(isLive)
                tvChat.setVisibility(View.GONE);
            else
                reviewLayout.setVisibility(View.GONE);
        }
    }

    public void initView () {

        accountPresenter.getAccountInfo();//获取账号信息
        giftPresenter.getGiftList();
        if (bundle != null) {
            headimgUrl = bundle.getString("headimgUrl");
            avatarSmall = bundle.getString("avatarsmall");
            if (avatarSmall == null || avatarSmall.isEmpty()) {
                headImage.setImageResource(R.mipmap.user1);
            } else {
                Picasso.with(getActivity())
                        .load(avatarSmall)
                        .placeholder(R.mipmap.user1)
                        .error(R.mipmap.user1)
                        .into(headImage);
            }
            isLive = bundle.getBoolean("isLive");
            uname = bundle.getString("uname");
            userName.setText(uname);
            totalWatched = bundle.getInt("totalWatched");
            txTotalPeople.setText(totalWatched + "");
            videoID = bundle.getString("videoID");
            qid = bundle.getInt("id", 0);
            userNumber = bundle.getString("userNumber");
            leId.setText(userNumber);
            anchorID = bundle.getInt("anchorID", 0);
            accountPresenter.getFansContributionTotal(anchorID);
                 /*主播头像设置星级小图标*/
            Integer level = bundle.getInt("tagLevel", 1);
            int levelTag = (level + 16 - 1) / 16;
            String tagName = "resource_v_10" + levelTag;
//            int tagId = DrawableSettingUtils.getDrawResourceID(getActivity(), tagName);
            tagImgID = DrawableSettingUtils.getDrawResourceID(tagName);
            masterLevelImage.setImageResource(tagImgID);

            gender = bundle.getInt("gender", 0);
            sign = bundle.getString("sign", "");
            masterInfoBean = new SpecUserInfoBean();
            masterInfoBean.setId(anchorID);
            masterInfoBean.setName(uname);
            masterInfoBean.setAvatar(headimgUrl);
            masterInfoBean.setAvatarSmall(avatarSmall);
            masterInfoBean.setGender(gender);
            masterInfoBean.setUserNumber(Integer.parseInt(userNumber));
            masterInfoBean.setLevel(level);
            masterInfoBean.setSign(sign);
            masterInfoBean.setFansNumber(0);
            masterInfoBean.setFavoursNumber(0);
            if (isLive) {
                tvChat.setVisibility(View.VISIBLE);
            } else {
                reviewLayout.setVisibility(View.VISIBLE);
            }
        }

        image = new UMImage(getActivity(), avatarSmall);
//        initDialog();
    }

    private void initRongYun() {

//        AppUser appUser = ((AppApplication) appComponent.getApplication()).getAppUser();
        if (!isLive)
            return;
        //初始化,并且加入房间
        rongPresenter.joinChartRoom(roomID, -1)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showSweetAlertDialog(e);
                    }

                    @Override
                    public void onNext(Object o) {
//                        toast("join success");
                        RongYunListenerContext.getInstance().attachIView(WatcherLayerFragment.this);

                        rongSendLiveUserMessage2(false, "进入直播间", Constants.MSG_TYPE_JOIN, getMsgUserInfoBean2());
                        LiveUserMessage userMessageSystem = new LiveUserMessage("进入直播间", Constants.MSG_TYPE_SYSTEM, getMsgUserInfoBean2());
                        LiveUserMessage userMessageNormal = new LiveUserMessage("进入直播间", Constants.MSG_TYPE_JOIN, getMsgUserInfoBean2());

                        setLiveUserMessageScroll2(userMessageSystem, messageAdapter, messageData, lvmessage);
                        setLiveUserMessageScroll2(userMessageNormal, messageAdapter, messageData, lvmessage);

//                        if (isLive)//观看直播时在进行查询用户列表
//                            rongPresenter.getChatRoomMember(roomID, 200, ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_DESC);
                    }
                });
    }


    //发送礼物赠送消息
    private void rongSendLiveGiftMessage(String dmContent, int msgType, int id) {
        MsgUserInfoBean msgUserInfoBean = getMsgUserInfoBean2();
        MsgGiftBean msgGiftBean = new MsgGiftBean(id, giftDataList.get(id).getName(), 0, 0, giftDataList.get(id).getPicUrl());
        LiveGiftMessage liveGiftMessage = new LiveGiftMessage(dmContent, msgType, msgUserInfoBean, msgGiftBean);
        rongPresenter.sendLiveGiftMessage(liveGiftMessage, roomID).subscribe(new Observer<LiveGiftMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showSweetAlertDialog(e);
            }

            @Override
            public void onNext(LiveGiftMessage giftMessage) {

            }
        });
    }


    private void rongSendMessage(String uid, String uname, String megContent, String headerImgUrl, final int msgType) {
        TextMessage textMessage = TextMessage.obtain(megContent);
//        textMessage.setExtra(msgType + "," + roomID);
        textMessage.setExtra(String.valueOf(msgType));
        Uri uri;
        if (headerImgUrl == null) {
            uri = null;
        } else {
            uri = Uri.parse(headerImgUrl);
        }
        UserInfo userInfo = new UserInfo(uid, uname, uri);
        textMessage.setUserInfo(userInfo);

        if (msgType == Constants.MSG_TYPE_NORMAL) {

            MsgUserInfoBean msgUserInfoBean = new MsgUserInfoBean(appUser.getHeadimgurl(), appUser.getSmallHeadimgurl(),
                    appUser.getCity(), appUser.getGender(),
                    appUser.getId(), 0, appUser.getLevel(),
                    appUser.getNickname(), appUser.getLeNum(), "");
            LiveUserMessage liveUserMessage = new LiveUserMessage(megContent, msgType, msgUserInfoBean);
            rongPresenter.sendLiveUserMessage(liveUserMessage, roomID).subscribe(new Observer<LiveUserMessage>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(LiveUserMessage contentMessage) {
                    RongYunListenerContext.getInstance().attachIView(WatcherLayerFragment.this);
//                    if (msgType.equals(Constants.MSG_TYPE_QUIT)) {
//                        rongPresenter.quitChartRoom(roomID);
//                    }
                }
            });
        }
    }


    private RelativeLayout addHeaderScroll(String userID, String headerImgUrl) {
        RelativeLayout layout_root_relative = new RelativeLayout(getActivity());
        layout_root_relative.setId(Integer.parseInt(userID));

        ImageView imageView = new CircleImageView(getActivity());
        RelativeLayout.LayoutParams RL_WW = new RelativeLayout.LayoutParams(90, 90);
        RL_WW.setMargins(5, 0, 10, 0);
        RL_WW.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView.setLayoutParams(RL_WW);
        if (headerImgUrl == null || headerImgUrl.isEmpty()) {
            imageView.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(getActivity())
                    .load(headerImgUrl)
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .resize(30, 30)
                    .into(imageView);
        }
        layout_root_relative.addView(imageView);

        return layout_root_relative;
    }


    /**
     * 更新直播间当前总人数
     *
     * @param totalWatched 总观看人数
     */
    public void updateTotalPeopleNum(int totalWatched) {
        this.totalWatched = totalWatched;
        totalPeople=totalWatched;
        txTotalPeople.setText(String.valueOf(totalPeople));
//        txTotalPeople.setText(String.valueOf(totalWatched));
        txTotalPeople.setVisibility(View.VISIBLE);
        txTotalPeopleWord.setVisibility(View.VISIBLE);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            progressBarDialogFragment.dismiss();
//            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//                Toast.makeText(getActivity(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
//            } else {
////                Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            }
            if (t != null) {
//                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    private void initDialog() {
        if (inputDialogFragment == null) {
            inputDialogFragment = new InputDialogFragment();
        }
        inputDialogFragment.attachFramgent(this);
        inputDialogFragment.onAttach(getActivity());
    }

    /**
     * 显示聊天布局
     */
    private void showChat(String tag) {
//        tvChat.setVisibility(View.GONE);
//        reportBtn.setVisibility(View.GONE);
//        periscopeBtn.setVisibility(View.GONE);
//        giftBtn.setVisibility(View.GONE);
//        shareBtn.setVisibility(View.GONE);
//        llInputParent.setVisibility(View.VISIBLE);
//        llInputParent.requestFocus();
//        showKeyboard();
        initDialog();
        inputDialogFragment.show(getActivity().getSupportFragmentManager(), tag);

    }

    /**
     * 发送消息,消息处理模块
     */
    @Override
    public void sendLiveUserMesg(String mContent, int type) {
        this.messageContent = mContent;
        if (appUser.isLogined()) {
            if (type == Constants.MSG_TYPE_DANMU) {
                payNum = 1;
                buyVirtualCoins(1, Constants.DANMU_ACCOUNT_ID, -1, type);
            } else {
                LiveUserMessage liveUserMessage = new LiveUserMessage(mContent, type, getMsgUserInfoBean2());
                setLiveUserMessageScroll2(liveUserMessage, messageAdapter, messageData, lvmessage);
            }
            rongSendLiveUserMessage2(false, mContent, type, getMsgUserInfoBean2());
        } else {
            new SweetAlertDialog(getActivity())
                    .setTitleText("请重新登录")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            getActivity().startActivity(new Intent(getActivity(), BFLoginActivity.class));
                        }
                    })
                    .show();
        }
    }

    /**
     * 根据状态操作控件
     *
     * @param status Touch事件状态
     */
    private void midLayoutInVisable(boolean status) {
        if (status) {
            mBackBtn.setVisibility(View.VISIBLE);
        } else {
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f);
            mHiddenAction.setDuration(800);

            mBackBtn.startAnimation(mHiddenAction);
            mBackBtn.setVisibility(View.GONE);
        }
    }

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MOBILE_QUERY:
                    //当2秒到达后，作相应的操作。
                    midLayoutInVisable(false);
                    break;

                case MSG_ADD_VIEW:
                    int off = msgLayout.getMeasuredHeight() - sView.getHeight();
                    if (off > 0) {
                        sView.scrollTo(0, off);//改变滚动条的位置
                    }
                    break;

            }
        }
    };

    /**
     * 开始计时功能
     */
    private void startTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        Date time = calendar.getTime();
        liveTime = time.getTime();
        handler.post(timerRunnable);
    }

    /**
     * 显示软键盘并因此头布局
     */
    private void showKeyboard() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }


    Handler showAnimateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            animateToShow();
        }
    };
    /**
     * 软键盘显示与隐藏的监听
     */
    int keybordHeight;

    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                animateToHide();
                bottomShow(false);
//                Rect r = new Rect();
//                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                int screenHeight = com.ddm.live.utils.camera.util.DisplayUtil.getScreenHeight(getActivity());
//                keybordHeight = screenHeight - r.bottom;
//                dynamicChangeListviewH(r.bottom + lvmessage.getMeasuredHeight() / 2);
//                dynamicChangeGiftParentH(true);

            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                if (inputDialogFragment != null && inputDialogFragment.isAdded()) {
                    inputDialogFragment.dismiss();
                }
                inputDialogFragment=null;
                bottomShow(true);
                if (isOpen) {
                    new Thread() {
                        @Override
                        public void run() {
                            while (isOpen) ;
                            showAnimateHandler.sendEmptyMessage(0);
                        }
                    }.start();
                } else {
                    animateToShow();
                }

            }
        });
    }

    /**
     * 动态的修改listview的高度
     *
     * @param heightPX
     */
    private void dynamicChangeListviewH(int heightPX) {
        int[] location = new int[2];
        lvmessage.getLocationInWindow(location);//获取控件当前在屏幕中的位置

        ViewGroup.LayoutParams layoutParams = lvmessage.getLayoutParams();
        layoutParams.height = heightPX;
        lvmessage.setLayoutParams(layoutParams);
    }

    /**
     * 动态修改礼物父布局的高度
     *
     * @param showhide
     */
    private void dynamicChangeGiftParentH(boolean showhide) {
        if (showhide) {/*如果软键盘显示中*/
            if (llgiftcontent.getChildCount() != 0) {
                /*判断是否有礼物显示，如果有就修改父布局高度，如果没有就不作任何操作*/
                ViewGroup.LayoutParams layoutParams = llgiftcontent.getLayoutParams();
                layoutParams.height = llgiftcontent.getChildAt(0).getHeight();
                llgiftcontent.setLayoutParams(layoutParams);
            }
        } else {/*如果软键盘隐藏中*/
            /*就将装载礼物的容器的高度设置为包裹内容*/
            ViewGroup.LayoutParams layoutParams = llgiftcontent.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llgiftcontent.setLayoutParams(layoutParams);
        }
    }

    /**
     * 头部布局执行显示的动画
     */
    private void animateToShow() {
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(rlsentimenttime, "translationX", -rlsentimenttime.getWidth(), 0);
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(scrollView, "translationY", -scrollView.getHeight(), 0);
        animatorSetShow.playTogether(leftAnim, topAnim);
        animatorSetShow.setDuration(300);
        animatorSetShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetShow.start();
        }
    }

    /**
     * 头部布局执行退出的动画
     */
    private void animateToHide() {
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(rlsentimenttime, "translationX", 0, -rlsentimenttime.getWidth());
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(scrollView, "translationY", 0, -scrollView.getHeight());
        animatorSetHide.playTogether(leftAnim, topAnim);
        animatorSetHide.setDuration(300);
        animatorSetHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetHide.start();
        }
    }

    /**
     * 循环执行线程
     */
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(timerRunnable, 1000);
            long sysTime = System.currentTimeMillis();
            liveTime += 1000;
            CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", liveTime);
            CharSequence sysDateStr = DateFormat.format("yyyy/MM/dd", sysTime);
            tvtime.setText(sysTimeStr);
            tvdate.setText(sysDateStr);
        }
    };


    @Override
    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList) {
        String lebi = "0";
        String diamonds = "0";

        if (isPay) {
            for (AccountTypesDataBean data : accountList) {
                if (data.getType() == 1) {
                    lebi = String.valueOf((int) (Float.parseFloat(data.getAccountEarth().getData().getBalance())));
                    userBalance = (int) Float.parseFloat(data.getAccountEarth().getData().getBalance());
                } else if (data.getType() == 0) {
                    diamonds = String.valueOf((int) (Float.parseFloat(data.getAccountEarth().getData().getBalance())));
                }
            }
        }
        lebiText.setText(lebi);
    }

    @Override
    public void isPaySuccess(int position, int type) {
        String lebi = Integer.parseInt(lebiText.getText() + "") - payNum + "";
        userBalance = Integer.parseInt(lebi);
        lebiText.setText(lebi);
        if (type == Constants.MSG_TYPE_GIFT) {
            //发送赠送礼物消息及显示动画效果
            rongSendLiveGiftMessage("", 0, position);
            showGift(position + "", appUser.getHeadimgurl(), appUser.getNickname(), giftDataList.get(currentPosition).getName());

            if (isLive) {//只观看直播时往下进行
                //显示赠送礼物消息
                Integer uID = appUser.getId();
                String uName = appUser.getNickname();
                String headerImgUrl = appUser.getHeadimgurl();
                Integer level = appUser.getLevel();
                String gender = String.valueOf(appUser.getGender());
                String[] msgData = {String.valueOf(uID), uName, "赠送给主播" + giftDataList.get(currentPosition).getName(), headerImgUrl, String.valueOf(Constants.MSG_TYPE_GIFT), gender, String.valueOf(level)};
                messageData.add(msgData);
                messageAdapter.NotifyAdapter(messageData);
                lvmessage.setSelection(messageData.size());
            }
        } else if (type == Constants.MSG_TYPE_DANMU) {
            //显示弹幕消息
            showDanmuMesg(messageContent);
        }
    }

    @Override
    public void onQueryRechargeRecoderList(QueryChargeInfoListResponse response) {

    }

    @Override
    public void onGetPrepayId(BuyVirtualCoinsResponse response) {

    }

    //接收系统消息回调接口
    @Override
    public void onDrawLiveContentMessage(Observable<LiveContentMessage> messageObservable) {
        messageObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LiveContentMessage>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "messageObserver.observeOn onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onDrawMessage messageObserver.observeOn onError: " + e.toString());
            }

            @Override
            public void onNext(LiveContentMessage contentMessage) {
                String total = contentMessage.getContent();
                int type = contentMessage.getType();
                switch (type) {
                    case 0:
                        updateTotalPeopleNum(Integer.parseInt(total));
                        Log.e("cxx", total);
                        break;
                    case 1:
                        setFansContributionTotal(total);//更新粉丝贡献榜总数
                        break;
                }

            }
        });
    }

    //接收礼物消息回调接口
    @Override
    public void onDrawLiveGiftMessage(Observable<LiveGiftMessage> messageObservable) {
        messageObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LiveGiftMessage>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "messageObserver.observeOn onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onDrawMessage messageObserver.observeOn onError: " + e.toString());
            }

            @Override
            public void onNext(LiveGiftMessage giftMessage) {
                showGiftMesgAndAnim(giftMessage, Constants.PRESENT_GIFT_AUDIENCE_DESC);
            }
        });
    }

    //接收状态消息回调接口
    @Override
    public void onDrawLiveStatusMessage(Observable<LiveStatusMessage> messageObservable) {
        messageObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LiveStatusMessage>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "messageObserver.observeOn onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onDrawMessage messageObserver.observeOn onError: " + e.toString());
            }

            @Override
            public void onNext(LiveStatusMessage statusMessage) {
                switch (statusMessage.getType()) {
                    case 0:
                        //主播暂时离开
                        iBaseFragmentLayerView.pauseLive();
                        break;
                    case 1:
                        //主播回来
                        iBaseFragmentLayerView.restartLive();
                        break;
                    case 2:
                        //点亮
                        periscopeLayout.addHeart();
                        break;
                    case 3:
                        //主播退出直播
                        iBaseFragmentLayerView.masterStopLive();
                        break;
                }
            }
        });
    }

    //接收用户消息回调接口
    @Override
    public void onDrawLiveUserMessage(Observable<LiveUserMessage> messageObservable) {
        if (!isLive)//回看的时候不进行观众列表刷新
            return;
        messageObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LiveUserMessage>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "messageObserver.observeOn onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onDrawMessage messageObserver.observeOn onError: " + e.toString());
            }

            @Override
            public void onNext(LiveUserMessage userMessage) {
                int msgType = userMessage.getType();
                Integer uID = userMessage.getUser().getId();
                String mContent = userMessage.getConent();

                //用户消息中针对不同类型消息不同操作处理部分
                switch (msgType) {
                    case Constants.MSG_TYPE_JOIN:
                        int index = -1;

//                        for (int i = 0; i < userInfoList.size(); i++) {
//                            if (uID.equals(userInfoList.get(i).getId())) {
//                                index = i;
//                                break;
//                            }
//                        }

//                        if (index == -1) {
                            totalPeople = totalPeople + 1;
//                            totalWatched = totalWatched + 1;
//                            updateTotalPeopleNum(totalPeople);
                            updateAudienceHeaderList(userMessage.getUser(), msgType);
                            addHeart();
                            userJoinShow(userMessage.getUser());
//                        }
                        break;
                    case Constants.MSG_TYPE_QUIT:
                        totalPeople = totalPeople - 1;
//                        updateTotalPeopleNum(totalPeople);
                        updateAudienceHeaderList(userMessage.getUser(), msgType);
                        return;
                    case Constants.MSG_TYPE_DANMU:
                        showDanmuMesg(mContent);
                        return;
                }
                setLiveUserMessageScroll2(userMessage, messageAdapter, messageData, lvmessage);//用户消息显示
            }
        });
    }

    //获取推送观众列表
    @Override
    public void onDrawLiveUserListMessage(Observable<LiveUserListMessage> messageObservable) {
        messageObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LiveUserListMessage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LiveUserListMessage liveUserListMessage) {
                        Gson gson=new Gson();
                        List<SpecUserInfoBean> list= gson.fromJson(liveUserListMessage.getUserListJsonString(), new TypeToken<List<SpecUserInfoBean>>(){}.getType());
                        userInfoList=list;
                        totalPeople=list.size();
                        updateTotalPeopleNum(totalPeople);
                        updateAudienHeaderList(list);
                        hlvaudience.setAdapter(audienceAdapter);

                    }
                });
    }
        /**
         * 用户进入直播间进行显示
         */

    private void userJoinShow(MsgUserInfoBean msgUserInfoBean) {
        handler.removeMessages(USERJOINMSGDIMISS);
        handler.sendEmptyMessageDelayed(USERJOINMSGDIMISS, 2000L);
        if (userJoin.getVisibility() == View.GONE) {
            ainmator(userJoin, "translationX", Utils.dip2px(getContext(), -300), 0, 500l);
        }
        userJoin.setVisibility(View.VISIBLE);
        String drawableName = "level_" + msgUserInfoBean.getLevel();
        int drawResId = DrawableSettingUtils.getDrawResourceID(drawableName);
        Drawable nav_up_join = ContextCompat.getDrawable(getContext(), drawResId);
        nav_up_join.setBounds(2, 2, nav_up_join.getMinimumWidth() / 8 * 4, nav_up_join.getMinimumHeight() / 8 * 4);
        userJoinTv.setCompoundDrawables(nav_up_join, null, null, null);
        userJoinTv.setCompoundDrawablePadding(6);
        userJoinTv.setText("登入用户" + msgUserInfoBean.getName() + " 进入直播间");
    }

    /**
     * 其他用户进入直播间进行提醒的相关动画
     *
     * @param view
     * @param animatorStr
     * @param start
     * @param end
     * @param duration
     */
    private void ainmator(View view, String animatorStr, final int start, final int end, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, animatorStr, start, end).setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (start < end)
                    userJoinTv.setVisibility(View.VISIBLE);
            }
        });
    }

    //增加心
    public void addHeart() {
        for (int i = 0; i < 3; i++) {
            periscopeLayout.addHeart();
        }
    }

    @Override
    public void focusResult(String result, String type) {
        switch (type) {
            case "0":
                if (result.equals("true")) {
                    isFocus = true;
//                    focusBtn.setImageDrawable(getResources().getDrawable(R.mipmap.blue_play_focus_already));
//                    rongSendMessage(appUser.getId(), appUser.getNickname(), "关注了主播", appUser.getHeadimgurl(), Constants.MSG_TYPE_FOCUS);
                    rongSendLiveUserMessage2(false, Constants.MSG_FOCUS, Constants.MSG_TYPE_FOCUS, getMsgUserInfoBean2());
//                    Toast.makeText(getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                } else {
//                    focusBtn.setImageDrawable(getResources().getDrawable(R.mipmap.blue_play_focus));
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "系统繁忙请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case "1":
                if (result.equals("true")) {
                    isFocus = false;
//                    focusBtn.setImageDrawable(getResources().getDrawable(R.mipmap.blue_play_focus));
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "关注已取消", Toast.LENGTH_SHORT).show();
                    }
                } else if (getActivity() != null) {
//                    focusBtn.setImageDrawable(getResources().getDrawable(R.mipmap.blue_play_focus_already));
                    Toast.makeText(getActivity(), "系统繁忙请稍后再试", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    @Override
    public void reportResult(String result) {
        if (result.equals("true") && getActivity() != null) {
//            reportBtn.setText(R.string.bf_report_on_text);
            Toast.makeText(getActivity(), "已举报该用户", Toast.LENGTH_SHORT).show();
        } else if (getActivity() != null) {
            Toast.makeText(getActivity(), "系统繁忙请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

    //初次进入获取房间用户信息
    @Override
    public void getChatRoomUser(ChatRoomInfo userArray) {
        int totalMember = userArray.getMemberInfo().size();
        Integer[] userIds = new Integer[totalMember];
        for (int i = 0; i < totalMember; i++) {
            userIds[i] = Integer.parseInt(userArray.getMemberInfo().get(i).getUserId());
        }
        videoPlayerPresenter.getSpecUserInfoByIdPlay(userIds, SEARCH_ROOM_MEMBERS_INFO);
    }

    @Override
    public void getUserInfoResult(List<SpecUserInfoBean> userInfoArrayList, int code) {
        if (code == SEARCH_ROOM_MEMBERS_INFO) {
            this.userInfoList = userInfoArrayList;

            if (audienceAdapter == null) {
                audienceAdapter = new AudienceAdapter(userInfoList, getContext());
                hlvaudience.setAdapter(audienceAdapter);
            }
        }
//        else if (code == SEARCH_MASTER_INFO) {
//            masterInfoBean = userInfoArrayList.get(0);
//            persionalInfoPanel = new PersionalInfoPanelFragment();
//            persionalInfoPanel.attachFramgent(this);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("userInfo", masterInfoBean);
//            persionalInfoPanel.setArguments(bundle);
//            persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "PersionalInfoPanel");
//        }

    }

    @Override
    public void quitRoomResult(String result) {
        if (result.equals("true")) {
            Log.d(TAG, "退出房间成功");
        }
    }

    /**
     * 查询关注状态返回结果
     */
    @Override
    public void queryFocusStatusResult(Boolean result, int code) {
        if (bundle != null) {
            if (!anchorID.equals(0) && anchorID.equals(appUser.getId())) {
                focusBtn.setVisibility(View.GONE);
                return;
            }
        } else {
            return;
        }
        if (result) {
            focusBtn.setVisibility(View.GONE);
            if (code == 1 && !isFocus) {
                rongSendLiveUserMessage2(false, Constants.MSG_FOCUS, Constants.MSG_TYPE_FOCUS, getMsgUserInfoBean2());
                if (isLive) {
                    LiveUserMessage liveUserMessage = new LiveUserMessage(Constants.MSG_FOCUS, Constants.MSG_TYPE_FOCUS, getMsgUserInfoBean2());
                    setLiveUserMessageScroll2(liveUserMessage, messageAdapter, messageData, lvmessage);
                }
            }
            isFocus = true;
        } else {
            isFocus = false;
            focusBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 取消关注
     */
    @Override
    public void cancelFocusMaster() {

    }

    /**
     * 关注主播
     */
    @Override
    public void focusMaster() {
        focusBtn.setVisibility(View.GONE);
//        rongSendMessage(appUser.getId(), appUser.getNickname(), appUser.getNickname() + "关注了主播", headimgUrl, Constants.MSG_TYPE_FOCUS);
        rongSendLiveUserMessage2(false, Constants.MSG_FOCUS, Constants.MSG_TYPE_FOCUS, getMsgUserInfoBean2());
        LiveUserMessage liveUserMessage = new LiveUserMessage(Constants.MSG_FOCUS, Constants.MSG_TYPE_FOCUS, getMsgUserInfoBean2());
//        setLiveUserMessageScroll(liveUserMessage);
        if (isLive)
            setLiveUserMessageScroll2(liveUserMessage, messageAdapter, messageData, lvmessage);
//        setMsgScrollDanmu(currentUserInfoBean, "关注了主播", Constants.MSG_TYPE_FOCUS);
    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }

    @Override
    public void getGiftList(List<GiftData> giftData) {
        this.giftDataList = giftData;

        GiftPagerAdapter giftPagerAdapter = new GiftPagerAdapter(getContext(), giftData);
        giftPager.setAdapter(giftPagerAdapter);
        giftPagerAdapter.setOnGiftClickListener(new GiftPagerAdapter.OnGiftClickListener() {
            @Override
            public void onGiftClick(int position) {
//                Log.e("cxx gift:", position + "");

                currentPosition = position;
            }
        });
        giftPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeGiftDot(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getGiftDots();

    }

    private void changeGiftDot (int pos) {
        int num = giftDots.size();
        for(int i=0; i<num; i++) {
            if(i == pos) {
                giftDots.get(i).setImageResource(R.mipmap.main_dot_red);
            }
            else {
                giftDots.get(i).setImageResource(R.mipmap.main_dot_light);
            }
        }
    }

    private void getGiftDots () {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dip2px(getContext(), 6), Utils.dip2px(getContext(), 6));
        lp.setMargins(Utils.dip2px(getContext(), 3), 0, Utils.dip2px(getContext(), 3), 0);
        int dotNum = giftDataList.size()%8 +1+1;
        for(int i=0; i<dotNum; i++) {
            ImageView dot = new ImageView(getContext());
            dot.setLayoutParams(lp);
            if(i == 0) {
                dot.setImageResource(R.mipmap.main_dot_red);
            }
            else {
                dot.setImageResource(R.mipmap.main_dot_light);
            }
            giftDotLayout.addView(dot);
            giftDots.add(dot);
        }
    }

    public void buyVirtualCoins(int payNums, int anchorId, int position, int type) {
        //测试代码，正式环境下关闭
//                    showGift(currentPosition + "", appUser.getHeadimgurl(), appUser.getNickname());
//                    rongSendMessage(appUser.getId(), appUser.getNickname(), currentPosition + "", appUser.getHeadimgurl(), MSG_TYPE_GIFT);
        if (userBalance >= payNums) {
            accountPresenter.presentVirtualCoins(payNums, anchorId, position, type);
        } else {
            rechargeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            rechargeDialog = rechargeBuilder.create(R.layout.dialog_normal_layout);
            rechargeDialog.show();
        }
    }

    @Override
    public void setupFragmentComponent() {
        super.setupFragmentComponent();
        rongPresenter.attachView(this);
        videoPlayerPresenter.attachView(this);
        accountPresenter.attachView(this);
        giftPresenter.attachIGiftListView(this);
        userInfoPresenter.attachView(this);
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (llGiftLayout.getVisibility() == View.VISIBLE) {
                llGiftLayout.setVisibility(View.INVISIBLE);
                bottomShow(true);
            } else if (bfPlayOverFragment != null && bfPlayOverFragment.isVisible()) {
                bfPlayOverFragment.getActivity().finish();
                bfPlayOverFragment.getActivity().overridePendingTransition(0, R.anim.activity_close_form_bottom);
            } else {
                stopPlay();
            }

        }
    }

    //停止收看直播
    public void stopPlay() {
        rongSendLiveUserMessage2(false,Constants.MSG_QUIT, Constants.MSG_TYPE_QUIT, getMsgUserInfoBean2());
        iBaseFragmentLayerView.closeLive();
        InputDialogFragment.isSwitchChecked = false;
    }

    @Override
    public void onTouch() {
        Integer[] ids = {anchorID};
        videoPlayerPresenter.queryFocusedStatus(ids, 1);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isPaused) {
            isPaused = false;
        }
        if (isJumpedOut) {//页面跳转回来视频已经结束
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments, bfPlayOverFragment).commit();
        }
        Integer[] ids = {anchorID};
        videoPlayerPresenter.queryFocusedStatus(ids, 0);
        accountPresenter.getAccountInfo();
    }

    public void quitLiveRoom() {
        if (persionalInfoPanel != null && persionalInfoPanel.isVisible()) {
            persionalInfoPanel.dismiss();
        }
        if (rechargeDialog != null && rechargeDialog.isShowing()) {
            rechargeDialog.dismiss();
        }
        if (inputDialogFragment != null && inputDialogFragment.isVisible()) {
            inputDialogFragment.dismiss();
        }
        if(shareBordFrament!=null && shareBordFrament.isAdded()){
            shareBordFrament.dismiss();
            shareBordFrament=null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("headimgUrl", headimgUrl);
        bundle.putString("uname", uname);
        bundle.putString("videoID", videoID);
        bundle.putString("avatarsmall", avatarSmall);
        bundle.putString("watchers", getWatchers() + "");
        bundle.putInt("levelTag", tagImgID);
        bundle.putInt("anchorID", anchorID);
        bundle.putBoolean("isFollowed", isFocus);
        bundle.putInt("level", masterInfoBean.getLevel());
        bundle.putString("userNumber", userNumber);
        bundle.putString("sign", masterInfoBean.getSign());
        bundle.putInt("gender", masterInfoBean.getGender());
        bundle.putInt("fansCount", masterInfoBean.getFansNumber());
        bundle.putInt("masterCount", masterInfoBean.getFavoursNumber());
        bundle.putBoolean("isLive", isLive);
        bundle.putString("topic", topic);
        bundle.putInt("qnid", qid);
        bfPlayOverFragment = new BFPlayOverFragment();
        bfPlayOverFragment.setArguments(bundle);
        if (!isPaused) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments, bfPlayOverFragment).commit();
        } else {
            isJumpedOut = true;//进入后台或调到到别的界面时视频结束了
        }
        InputDialogFragment.isSwitchChecked = false;
        rongSendLiveUserMessage2(false,Constants.MSG_QUIT, Constants.MSG_TYPE_QUIT, getMsgUserInfoBean2());
        clearProgress();

    }

    @Override
    public void onGetFansContributionToatal(String total) {
        setFansContributionTotal(total);
    }

    public void setFansContributionTotal(String total) {
        if (total != null) {
            String totalNum = ((int) Float.parseFloat(total)) + "";
            diamondsText.setText(totalNum);
        } else {
            diamondsText.setText("0");
        }
    }

    @Override
    public void getRechargeListInfoResponse(List<GetRechargeListResponseData> chargeList) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(RefreshUserCenterEvent tmp) {

    }

    @Override
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList, String total) {

    }

    @Override
    public void getUserInfoResult(List<UserInfoBean> userInfoBeen) {

    }

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", specUserInfoBean);
        bundle.putInt("anchorId", anchorID);
        bundle.putBoolean("isLive",isLive);
        persionalInfoPanel = new PersionalInfoPanelFragment();
        persionalInfoPanel.setArguments(bundle);
        persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "PersionalInfoPanel");
        persionalInfoPanel.setOnAtUserListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @TA按键时调用
     */
    @Override
    public void onAtUser(String nickName) {
        showChat("@"+nickName);
        if (persionalInfoPanel != null && persionalInfoPanel.isAdded()) {
            persionalInfoPanel.dismiss();
        }
    }

    public BFPlayOverFragment getBFPlayOverFragment () {
        handler.removeMessages(CURRTIME_CHANGE);
        return  bfPlayOverFragment;
    }

    private boolean isReview;
    /**
     * 清空进度条进度
     */
    public void clearProgress () {
        currtime = 0;
        currProgress = 0;
        currTime.setText("00:00");
        progress.setProgress(0);
        isReview = true;
    }


    public void removeProgressChangeMsg () {
        if(isLive)
            return;
        handler.removeMessages(CURRTIME_CHANGE);
    }

    public void sendProgressChangeMsg () {
        if (isLive)
            return;
        handler.removeMessages(CURRTIME_CHANGE);
        handler.sendEmptyMessageDelayed(CURRTIME_CHANGE, 1000L);
    }

    public interface OnProgressChangeListener {
        public void onProgressChange (long currTime);
    }
    private OnProgressChangeListener onProgressChangeListener;
    public void setOnProgressChangeListener (OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public interface OnPlayPauseChangeListener {
        public void onPlayPauseChange(boolean play);
    }
    private OnPlayPauseChangeListener onPlayPauseChangeListener;
    public void setOnPlayPauseChangeListener (OnPlayPauseChangeListener onPlayPauseChangeListener) {
        this.onPlayPauseChangeListener = onPlayPauseChangeListener;
    }
}