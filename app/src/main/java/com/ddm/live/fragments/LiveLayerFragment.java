package com.ddm.live.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.adapters.AudienceAdapter;
import com.ddm.live.adapters.MessageAdapter;
import com.ddm.live.adapters.PlayGiftAdapter;
import com.ddm.live.animation.DisplayUtil;
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
import com.ddm.live.models.bean.msgtype.MsgUserInfoBean;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsEndLiveRequest;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.ui.DensityUtils;
import com.ddm.live.ui.widget.CustomDialog;
import com.ddm.live.utils.DrawableSettingUtils;
import com.ddm.live.utils.RongYunListenerContext;
import com.ddm.live.utils.Utils;
import com.ddm.live.views.iface.IAccountView;
import com.ddm.live.views.iface.IFragmentLayerView;
import com.ddm.live.views.iface.IGiftListView;
import com.ddm.live.views.iface.IOnTouchView;
import com.ddm.live.views.iface.IRongYunView;
import com.ddm.live.views.iface.IStreamBaseView;
import com.ddm.live.views.iface.IUserInfoBaseView;
import com.google.gson.Gson;
import com.opendanmaku.DanmakuView;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.Conversation;
import me.yifeiyuan.library.PeriscopeLayout;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by cxx on 2016/10/10.
 */
public class LiveLayerFragment extends LiveWatcherBaseFragment implements
        IRongYunView,
        IStreamBaseView,
        IUserInfoBaseView,
        IGiftListView,
        IAccountView,
        IOnTouchView,
        PersionalInfoPanelFragment.OnAtUserListener {

    /**
     * 界面相关
     */
    private LinearLayout llpicimage;
    private LinearLayout llInputParent;
    //    private LinearLayout llgiftcontent;
    private LinearLayout llGiftLayout;
    private LinearLayout msgLayout;

    private RelativeLayout rlsentimenttime;
    private HorizontalListView hlvaudience;
    private PeriscopeLayout periscopeLayout;
    private boolean streamState;
//    private ListView lvmessage;


    //    private List<GiftData> giftDataList;
    private List<View> giftViewCollection = new ArrayList<View>();
    //    private List<String[]> messageData = new LinkedList<>();
    private List<Integer> userLevelList = new ArrayList<>();
    //    private List<SpecUserInfoBean> userInfoList = new ArrayList<>();
    private Set<String> lightIdsSet = new HashSet();

    private InputDialogFragment inputDialogFragment;
    private FansContributonListFragment fansContributonListFragment;
    private PersionalInfoPanelFragment persionalInfoPanel;
    /**
     * 动画相关
     */
//    private NumAnim giftNumAnim;
//    private TranslateAnimation inAnim;
//    private TranslateAnimation outAnim;
//    private AnimatorSet animatorSetHide = new AnimatorSet();
//    private AnimatorSet animatorSetShow = new AnimatorSet();

    private String headimgUrl;
    private String avatarSmall;
    private String videoID;
    private String uname;
    private String TAG = "ddm";
    private String messageContent;
    int currentPosition;
    private long liveTime;
    private long contribution;//当前直播获得星光数

    private int diamonds;
    private int payNum;
    private int sWidth, sHeight;
    private int count;
    private int SEARCH_MASTER_INFO = 1;
    private int totalWatched = 1;
    private int userBalance;
    private int SEARCH_ROOM_MEMBERS_INFO = 0;
    private final int MOBILE_QUERY = 1;
    private static final int REQ_DELAY_MILLS = 3000;
    private static final int MSG_ADD_VIEW = 0x00;
    boolean isQuit = false;
    private boolean timerFlag = false;
    private boolean isFocus = false;
    private boolean isOpen;


    private ImageView focusBtn;
    private ImageView periscopeBtn;
    private TextView diamondValue;
    private TextView userNmae;
    private TextView tvtime;
    private TextView tvdate;
    private TextView playPayBtn;
    private TextView txTotalPeople;
    private GridView gvPresentLayout;
    private UMImage image;
    private EditText etInput;
    private Button reportBtn;
    private Timer pushWatchedNumTimer;
    private ImageView masterLevelImage;
    IFragmentLayerView iFragmentLayerView;
    long randomCount = 10;
    //    private AudienceAdapter audienceAdapter;
    private SpecUserInfoBean iUserInfoBean;
    //    private MessageAdapter messageAdapter;
    private PlayGiftAdapter giftAdapter;
    private AppUser appUser;
    private Set<Integer> watchedPersonNumSet = new HashSet<>();//观看过的人
    private Integer watchedPersonNum;//观看过的人数
    private List<MsgUserInfoBean> robotUserList = new ArrayList<>();//维护当前房间中的在线人观众信息
    @BindView(R2.id.tvChat)
    LinearLayout tvChat;

    @BindView(R2.id.sendInput)
    TextView sendInput;

    @BindView(R2.id.bf_back_btn)
    ImageView mBackBtn;

    @BindView(R2.id.shareBtn)
    ImageView shareBtn;

    @BindView(R.id.play_user_money)
    TextView playUserBtn;

    @BindView(R2.id.msg_scroll_view)
    ScrollView sView;

    @BindView(R2.id.headImage)
    ImageView headImage;

    @BindView(R2.id.camera_switch_btn)
    ImageView cameraSwitchBtn;

    @BindView(R2.id.xiaozhu_number)
    TextView xiaozhuNumber;
    @BindView(R.id.user_join)
    RelativeLayout userJoin;
    @BindView(R.id.user_join_tv)
    TextView userJoinTv;

    @OnClick(R2.id.tvChat)
    public void tvChat() {
        showChat("");
    }

    @OnClick(R2.id.sendInput)
    public void sendInput() {
        sendText();
    }


    @OnClick(R2.id.bf_back_btn)
    public void back() {
//        if (!timerFlag && getActivity() != null) {
//            Toast.makeText(getActivity(), "亲，再多呆一会嘛！", Toast.LENGTH_SHORT).show();
//        }
        showDialog();
    }

    @OnClick(R2.id.shareBtn)
    public void share() {
//        new ShareAction(getActivity())
//                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.ALIPAY.QQ,SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
//                .setShareboardclickCallback(new ShareBoardlistener() {
//                    @Override
//                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//
//                        new ShareAction(getActivity()).
//                                setPlatform(share_media).setCallback(umShareListener)
//                                .withMedia(image)
//                                .withTitle(Constants.SHARE_TITLE)
//                                .withText(getResources().getString(R.string.xiaozhu_share_pre_title) + uname + getResources().getString(R.string.xiaozhu_share_after_title))
//                                .withTargetUrl(Constants.SHARE_SERVER_URL + videoID + "/")
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
            bundle.putInt("qnId", Integer.parseInt(videoID));
            bundle.putString("topic", topic);
            shareBordFrament.setArguments(bundle);
            shareBordFrament.show(getActivity().getSupportFragmentManager(), "share");
        }
    }

    @OnClick(R2.id.camera_switch_btn)
    public void switchCamera() {
        if (iFragmentLayerView != null) {
            iFragmentLayerView.switchCamera();
        }
    }

    @OnClick(R2.id.headImage)
    public void onHeadimgeClick() {
        persionalInfoPanel = new PersionalInfoPanelFragment();
        Bundle bundle = new Bundle();
        SpecUserInfoBean masterInfoBean = new SpecUserInfoBean();
        masterInfoBean.setId(appUser.getId());
        masterInfoBean.setName(appUser.getNickname());
        masterInfoBean.setAvatar(appUser.getHeadimgurl());
        masterInfoBean.setAvatarSmall(appUser.getSmallHeadimgurl());
        masterInfoBean.setGender(appUser.getGender());
        masterInfoBean.setUserNumber(appUser.getLeNum());
        masterInfoBean.setLevel(appUser.getLevel());
        masterInfoBean.setSign(appUser.getSign());
        masterInfoBean.setFansNumber(0);
        masterInfoBean.setFansNumber(0);
        bundle.putSerializable("userInfo", masterInfoBean);
        bundle.putInt("anchorId", appUser.getId());
        persionalInfoPanel.setArguments(bundle);
        persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "LiveMasterPersionalInfoPanel");
        persionalInfoPanel.setOnAtUserListener(this);
    }

    @OnClick(R2.id.fans_contribution_list_go_btn)
    public void fansContribution() {
        if (fansContributonListFragment.isAdded()) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("uID", appUser.getId());
        fansContributonListFragment.setArguments(bundle1);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.frament_enter_right, R.anim.fragment_close);
        transaction.add(R.id.live_fragments, fansContributonListFragment);
        transaction.commit();
    }

    public int getWatchers() {
//        return totalWatched;
        watchedPersonNum = watchedPersonNumSet.size();
        return watchedPersonNum;

    }

    public void attachView(IFragmentLayerView iFragmentLayerView) {
        this.iFragmentLayerView = iFragmentLayerView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.live_fragment_layer, container, false);
        AppApplication application = (AppApplication) getActivity().getApplication();
        appUser = application.getAppUser();
        ButterKnife.bind(this, view);
        fansContributonListFragment = new FansContributonListFragment();
        llpicimage = (LinearLayout) view.findViewById(R.id.llpicimage);
        rlsentimenttime = (RelativeLayout) view.findViewById(R.id.rlsentimenttime);
        hlvaudience = (HorizontalListView) view.findViewById(R.id.hlvaudience);
        hlvaudience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInfo", userInfoList.get(position));
                bundle.putInt("anchorId", appUser.getId());
                persionalInfoPanel = new PersionalInfoPanelFragment();
                persionalInfoPanel.setArguments(bundle);
                persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "LiveMasterPersionalInfoPanel");
                persionalInfoPanel.setOnAtUserListener(LiveLayerFragment.this);
            }
        });
        tvtime = (TextView) view.findViewById(R.id.tvtime);
        tvdate = (TextView) view.findViewById(R.id.tvdate);
        llgiftcontent = (LinearLayout) view.findViewById(R.id.llgiftcontent);
        lvmessage = (ListView) view.findViewById(R.id.lvmessage);
        llInputParent = (LinearLayout) view.findViewById(R.id.llinputparent);
        etInput = (EditText) view.findViewById(R.id.etInput);
        sendInput = (TextView) view.findViewById(R.id.sendInput);
        userNmae = (TextView) view.findViewById(R.id.player_user_name);

        diamondValue = (TextView) view.findViewById(R.id.diamonds_value);
        masterLevelImage = (ImageView) view.findViewById(R.id.master_level_tag);
        // 聊天室用户头像ScrollView
//        headScroll = (HorizontalScrollView) view.findViewById(R.id.blue_play_room_user_view);
//        headLayout = (LinearLayout) view.findViewById(R.id.blue_play_room_user_layout);
        mDanmakuView = (DanmakuView) view.findViewById(R.id.danmakuView);
        gvPresentLayout = (GridView) view.findViewById(R.id.play_present_layout);

        // 聊天室消息ScrollView
        msgLayout = (LinearLayout) view.findViewById(R.id.msg_linear_layout);
        llGiftLayout = (LinearLayout) view.findViewById(R.id.play_gift_layout);

        txTotalPeople = (TextView) view.findViewById(R.id.bf_room_people_num);
        playPayBtn = (TextView) view.findViewById(R.id.play_pay_btn);
        playUserBtn = (TextView) view.findViewById(R.id.play_user_money);
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
        accountPresenter.getAccountInfo();
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
                bundle.putInt("anchorId", appUser.getId());
                persionalInfoPanel.setArguments(bundle);
                persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "LiveMasterPersionalInfoPanel");
                persionalInfoPanel.setOnAtUserListener(LiveLayerFragment.this);
            }
        });
        createUserInfoListDatabase();
        initRongYun();
        pushWatchedNumTimer = new Timer();
        pushWatchedNumTimer.schedule(new WatchedNumTask(), 1000, 10000);
        return view;
    }
    //创建数据库，用来存储当前房间观众信息
    public void createUserInfoListDatabase(){

    }
    class WatchedNumTask extends TimerTask {
        @Override
        public void run() {
//            sendLiveContentMessage(String.valueOf(totalWatched), 0);//推送观看过的次数
//            startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalWatched));
            sendLiveContentMessage(String.valueOf(totalPeople), 0);//推送当前在线人数
            startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalPeople));
        }
    }

    private Handler timeHandler = new Handler();
    private Runnable timeRunnable = new Runnable() {
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
            }
        }
    };

    private void initRongYun() {
        //初始化,并且加入房间
        rongPresenter.joinChartRoom(roomID, -1)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showSweetAlertDialog(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        //监听
                        RongYunListenerContext.getInstance().attachIView(LiveLayerFragment.this);

                        rongSendLiveUserMessage2(true, Constants.MSG_JOIN, Constants.MSG_TYPE_JOIN, getMsgUserInfoBean2());

                        LiveUserMessage userMessageSystem = new LiveUserMessage(Constants.MSG_JOIN, Constants.MSG_TYPE_SYSTEM, getMsgUserInfoBean2());
                        LiveUserMessage userMessageNormal = new LiveUserMessage(Constants.MSG_JOIN, Constants.MSG_TYPE_JOIN, getMsgUserInfoBean2());

                        setLiveUserMessageScroll2(userMessageSystem, messageAdapter, messageData, lvmessage);//本地显示欢迎进入消息
                        setLiveUserMessageScroll2(userMessageNormal, messageAdapter, messageData, lvmessage);//本地显示进入消息

                        rongPresenter.getLiveChatRoomMember(roomID, 200, ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_DESC);
                        watchedPersonNumSet.add(appUser.getId());
                    }
                });
    }

    public void initView() {
        // 每2秒自动根据直播间人数自动添加心形效果
        timeHandler.postDelayed(timerRunnable, 1000);
        timer.start();
        accountPresenter.getFansContributionTotal(appUser.getId());
        giftPresenter.getGiftList();
        Bundle bundle = getArguments();
        if (bundle != null) {
            headimgUrl = appUser.getHeadimgurl();
            avatarSmall = appUser.getSmallHeadimgurl();
            if (avatarSmall == null || avatarSmall.isEmpty()) {
                headImage.setImageResource(R.mipmap.user1);
            } else {
                Picasso.with(getActivity())
                        .load(avatarSmall)
                        .placeholder(R.mipmap.user1)
                        .error(R.mipmap.user1)
                        .into(headImage);

            }
//            uname = bundle.getString("uname");
            userNmae.setText(appUser.getNickname());
//            roomID = bundle.getString("roomID");
            videoID = bundle.getString("videoID");

             /*主播头像设置星级小图标*/
            Integer level = appUser.getLevel();
            int levelTag = (level + 16 - 1) / 16;
            String tagName = "resource_v_10" + levelTag;
            int tagId = DrawableSettingUtils.getDrawResourceID(tagName);
            masterLevelImage.setImageResource(tagId);
        }
        String xznumber = appUser.getLeNum() + "";
        xiaozhuNumber.setText(xznumber);
        image = new UMImage(getActivity(), avatarSmall);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llInputParent.getVisibility() == View.VISIBLE) {
                    tvChat.setVisibility(View.VISIBLE);
                    hideKeyboard();
                }
            }
        });
        softKeyboardListnenr();
        startTimer();
    }

    /**
     * 显示聊天布局
     */
    private void showChat(String tag) {
//        tvChat.setVisibility(View.GONE);
//        reportBtn.setVisibility(View.GONE);
////        periscopeBtn.setVisibility(View.GONE);
//        shareBtn.setVisibility(View.GONE);
//        llInputParent.setVisibility(View.VISIBLE);
//        llInputParent.requestFocus();
//        showKeyboard();
        if (inputDialogFragment == null) {
            inputDialogFragment = new InputDialogFragment();
            inputDialogFragment.attachFramgent(this);
        }

        inputDialogFragment.show(getActivity().getSupportFragmentManager(), tag);
    }

    /**
     * 发送消息,消息处理模块
     */
    private void sendText() {
        if (appUser.isLogined()) {
            String dmContent = etInput.getText().toString();
            if (dmContent.isEmpty() && getActivity() != null) {
                Toast.makeText(getActivity(), "请先输入内容", Toast.LENGTH_SHORT).show();
            } else {
                rongSendLiveUserMessage2(true, dmContent, Constants.MSG_TYPE_NORMAL, getMsgUserInfoBean2());
                LiveUserMessage liveUserMessage = new LiveUserMessage(dmContent, Constants.MSG_TYPE_NORMAL, getMsgUserInfoBean2());
                setLiveUserMessageScroll2(liveUserMessage, messageAdapter, messageData, lvmessage);
//                setMsgScrollDanmu(iUserInfoBean, dmContent, Constants.MSG_TYPE_NORMAL);
                etInput.setText("");
                hideKeyboard();
            }
        } else if (getActivity() != null) {
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

    @Override
    public void onTouch() {
    }

    public void buyVirtualCoins(int payNums, int anchorId, int type) {
        //测试代码，正式环境下关闭
//                    showGift(currentPosition + "", appUser.getHeadimgurl(), appUser.getNickname());
//                    rongSendMessage(appUser.getId(), appUser.getNickname(), currentPosition + "", appUser.getHeadimgurl(), MSG_TYPE_GIFT);
        if (diamonds >= payNums) {
            accountPresenter.presentVirtualCoins(payNums, anchorId, -1, type);

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

    /**
     * 发送消息,消息处理模块
     */
    @Override
    public void sendLiveUserMesg(String mContent, int type) {
        this.messageContent = mContent;
        if (appUser.isLogined()) {
            if (type == Constants.MSG_TYPE_DANMU) {
                payNum = 1;
                buyVirtualCoins(1, Constants.DANMU_ACCOUNT_ID, type);
            } else {
                LiveUserMessage liveUserMessage = new LiveUserMessage(mContent, type, getMsgUserInfoBean2());
                setLiveUserMessageScroll2(liveUserMessage, messageAdapter, messageData, lvmessage);
            }
            rongSendLiveUserMessage2(true, mContent, type, getMsgUserInfoBean2());
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
                imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        llInputParent.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
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
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                animateToHide();
                shareBtn.setVisibility(View.GONE);
                tvChat.setVisibility(View.GONE);
                cameraSwitchBtn.setVisibility(View.GONE);
////                dynamicChangeListviewH(100);
//                dynamicChangeGiftParentH(true);
            }

            /*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
            @Override
            public void keyBoardHide(int height) {
                if (inputDialogFragment != null && inputDialogFragment.isAdded()) {
                    inputDialogFragment.dismiss();
                }
                shareBtn.setVisibility(View.VISIBLE);
                tvChat.setVisibility(View.VISIBLE);
                cameraSwitchBtn.setVisibility(View.VISIBLE);
                if (inputDialogFragment != null && inputDialogFragment.isVisible()) {
                    inputDialogFragment.dismiss();
                }
                inputDialogFragment = null;
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
////                dynamicChangeListviewH(150);
//                dynamicChangeGiftParentH(false);
            }
        });
    }


    /**
     * 动态的修改listview的高度
     *
     * @param heightPX
     */
    private void dynamicChangeListviewH(int heightPX) {
        ViewGroup.LayoutParams layoutParams = lvmessage.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getActivity(), heightPX);
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
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(llpicimage, "translationY", -llpicimage.getHeight(), 0);
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
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(llpicimage, "translationY", 0, -llpicimage.getHeight());
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

    /**
     * 数字放大动画
     */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
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

                showGiftMesgAndAnim(giftMessage, Constants.PRESENT_GIFT_MASTER_DESC);
                //刷新排行榜
                accountPresenter.getFansContributionTotal(appUser.getId());

            }
        });
    }


    @Override
    public void onDrawLiveStatusMessage(Observable<LiveStatusMessage> messageObservable) {
        messageObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LiveStatusMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LiveStatusMessage statusMessage) {
                if (statusMessage.getType() == 2) {
                    periscopeLayout.addHeart();
                }
            }
        });
    }

    //接收用户消息回调接口
    @Override
    public void onDrawLiveUserMessage(Observable<LiveUserMessage> messageObservable) {
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
                        watchedPersonNumSet.add(uID);
                        int index = -1;
//                        for (int i = 0; i < userInfoList.size(); i++) {
//                            if (uID.equals(userInfoList.get(i).getId())) {
//                                index = i;
//                                break;
//                            }
//                        }
//                        if () {
                            totalWatched = totalWatched + 1;
                            totalPeople = totalPeople + 1;
                            updateTotalPeopleNum();
                            sendLiveContentMessage(String.valueOf(totalPeople), 0);//推送当前在线人数
                            updateAudienceHeaderList(userMessage.getUser(), msgType);
//                            startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalWatched));
//                            if (totalWatched - totalPeople == randomCount) {
//                                startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalWatched));
//                                totalPeople = totalWatched;
//                                randomCount = 5 + (long) (Math.random() * 10);
//                            }
                            addHeart();
                            userJoinShow(userMessage.getUser());
                            //推送马甲用户
                            sendLiveUserListMessage(Conversation.ConversationType.PRIVATE, String.valueOf(uID));
//                        }
                        break;
                    case Constants.MSG_TYPE_QUIT:
                        updateAudienceHeaderList(userMessage.getUser(), msgType);
                        totalPeople = totalPeople - 1;
                        updateTotalPeopleNum();
                        return;
                    case Constants.MSG_TYPE_DANMU:
                        showDanmuMesg(mContent);
                        return;
                }
                //用户消息显示
                setLiveUserMessageScroll2(userMessage, messageAdapter, messageData, lvmessage);
            }
        });
    }

    @Override
    public void onDrawLiveUserListMessage(Observable<LiveUserListMessage> messageObservable) {

    }

    /**
     * 更新直播间当前总人数
     *
     * @param totalNum 总人数
     */
    public void updateTotalPeopleNum() {
//        txTotalPeople.setText(String.valueOf(totalWatched));
        txTotalPeople.setText(String.valueOf(totalPeople));
        txTotalPeople.setVisibility(View.VISIBLE);
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


    @Override
    public void getStreamChatRoomUser(ChatRoomInfo userArray) {
        int totalMember = userArray.getTotalMemberCount();
        totalWatched = totalMember;
        totalPeople = totalMember;
        updateTotalPeopleNum();//直播间总人数
        startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalPeople));
        Integer[] userIds = new Integer[userArray.getMemberInfo().size()];
        for (int i = 0; i < userArray.getMemberInfo().size(); i++) {
            userIds[i] = Integer.parseInt(userArray.getMemberInfo().get(i).getUserId());
        }
        videoPlayerPresenter.getSpecLiveUserInfoById(userIds, SEARCH_ROOM_MEMBERS_INFO);
    }

    @Override
    public void getStreamUserInfoResult(List<SpecUserInfoBean> userInfoArrayList, int code) {
        if (code == SEARCH_ROOM_MEMBERS_INFO) {
            this.userInfoList = userInfoArrayList;

            if (audienceAdapter == null) {
                audienceAdapter = new AudienceAdapter(userInfoList, getContext());
                hlvaudience.setAdapter(audienceAdapter);
                //随机获取一批用户
                userInfoPresenter.getRandomUsers();
            }
        }
//        else if (code == SEARCH_MASTER_INFO) {
//            iUserInfoBean = userInfoArrayList.get(0);
//            PersionalInfoPanelFragment persionalInfoPanel = new PersionalInfoPanelFragment();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("userInfo", iUserInfoBean);
//            persionalInfoPanel.setArguments(bundle);
//            persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "LiveMasterPersionalInfoPanel");
//        }
    }

    @Override
    public void quitStreamRoomResult(String result) {
        if (result.equals("true")) {
//            Log.d("ddm", "退出房间成功");
            myHandler.sendEmptyMessage(0);
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(), "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AppApplication.getInstance(), "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(), "取消分享", Toast.LENGTH_SHORT).show();
        }
    };


    public void onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fansContributonListFragment != null && fansContributonListFragment.isVisible()) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.frament_enter_right, R.anim.fragment_close);
                transaction.remove(fansContributonListFragment);
                transaction.commit();
            } else if (RongYunPresenter.isInLivingRoom) {
                showDialog();
            }
        }


    }

    public void showDialog() {
        quitChatroomBuilder = new CustomDialog.Builder(getActivity());
        quitChatroomBuilder.setTitle("确定退出直播？");
        if (streamState) {
            quitChatroomBuilder.setMessage("点击确定即可退出本次直播");
        } else {
            quitChatroomBuilder.setMessage("直播时间太短，将不予保存");
        }

        quitChatroomBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stopLiving();
            }
        });
        quitChatroomBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        customDialog = quitChatroomBuilder.create(R.layout.quit_room_tip_layout);
        customDialog.show();
    }

    private CountDownTimer timer = new CountDownTimer(15000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
        }

        @Override
        public void onFinish() {
            timerFlag = true;
        }
    };

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MsgUserInfoBean msgUserInfoBean = (MsgUserInfoBean) msg.obj;
            if (msgUserInfoBean != null) {
                LiveUserMessage liveUserMessage = new LiveUserMessage(Constants.MSG_JOIN, Constants.MSG_TYPE_JOIN, msgUserInfoBean);
                userJoinShow(liveUserMessage.getUser());
                setLiveUserMessageScroll(liveUserMessage);//马甲用户进入
                watchedPersonNumSet.add(liveUserMessage.getUser().getId());
                rongSendLiveUserMessage2(true, Constants.MSG_JOIN, Constants.MSG_TYPE_JOIN, msgUserInfoBean);
                addHeart();
                periscopeLayout.addHeart();
                robotUserList.add(liveUserMessage.getUser());//维护马甲用户列表
            }

        }
    };

    public void addHeart() {
        for (int i = 0; i < 3; i++) {
            periscopeLayout.addHeart();
        }
    }

    //结束直播
    public void stopLiving() {
        isQuit = true;
        startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalWatched));
        sendLiveContentMessage(String.valueOf(robotUserList.size()), 0);//推送观看过的次数
        ModifyStreamStateAsEndLiveRequest request = new ModifyStreamStateAsEndLiveRequest("结束直播", videoID);
        rongPresenter.closeStreams(request);
        InputDialogFragment.isSwitchChecked = false;
        rongSendLiveUserMessage2(true, Constants.MSG_QUIT, Constants.MSG_TYPE_QUIT, getMsgUserInfoBean2());
        iFragmentLayerView.closeLive();
        timeHandler.removeCallbacks(timeRunnable);

    }

    //发送内容消息
    public void sendLiveContentMessage(String content, int type) {
        LiveContentMessage liveContentMessage = new LiveContentMessage(content, type);
        rongPresenter.sendLiveContentMessage(liveContentMessage, roomID).subscribe(new Observer<LiveContentMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showSweetAlertDialog(e);
            }

            @Override
            public void onNext(LiveContentMessage contentMessage) {

            }
        });
    }

    //发送观众列表
    public void sendLiveUserListMessage(Conversation.ConversationType conversationType, String id) {
        Gson gson = new Gson();
        String userListJsonString;
        if(userInfoList.size()<=100){
             userListJsonString = gson.toJson(userInfoList);
        }else {
             userListJsonString = gson.toJson(userInfoList.subList(0,99));//最多推送100个
        }

        LiveUserListMessage liveUserListMessage = new LiveUserListMessage("", 0, userListJsonString);
        rongPresenter.sendLiveUserListMessage(conversationType, liveUserListMessage, id).subscribe(new Observer<LiveUserListMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showSweetAlertDialog(e);
            }

            @Override
            public void onNext(LiveUserListMessage contentMessage) {

            }
        });
    }

    //马甲用户进入需要单独进行人数统计
    public void setLiveUserMessageScroll(LiveUserMessage userMessage) {
        totalWatched = totalWatched + 1;
//        sendLiveContentMessage(String.valueOf(totalWatched), 0);//推送观看过的次数
//        startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalWatched));
//        if (totalWatched - totalPeople == randomCount) {
//            startLivePresenter.modifyWatchedNum(videoID, String.valueOf(totalWatched));
//            totalPeople = totalWatched;
//            randomCount = 5 + (long) (Math.random() * 10);
//        }
        super.setLiveUserMessageScroll2(userMessage, messageAdapter, messageData, lvmessage);
        int msgType = userMessage.getType();
        Integer uID = userMessage.getUser().getId();
        switch (msgType) {
            case Constants.MSG_TYPE_JOIN:
                int index = -1;
                for (int i = 0; i < userInfoList.size(); i++) {
                    if (uID.equals(userInfoList.get(i).getId())) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    updateAudienceHeaderList(userMessage.getUser(), msgType);
                    totalPeople = totalPeople + 1;
                    sendLiveContentMessage(String.valueOf(totalPeople), 0);//推送在线人数
                }
                break;
            case Constants.MSG_TYPE_QUIT:
                totalPeople = totalPeople - 1;
                sendLiveContentMessage(String.valueOf(totalPeople), 0);//推送在线人数
                updateAudienceHeaderList(userMessage.getUser(), msgType);

                break;
        }
        updateTotalPeopleNum();
    }

    @Override
    public void getUserInfoResult(final List<UserInfoBean> userInfoArrayList) {

        new Thread() {
            @Override
            public void run() {
                int i = 0;
                for (final UserInfoBean userInfoBean : userInfoArrayList) {
                    i++;
                    if(i==10){
                        return;
                    }
                    if (userInfoBean != null) {
                        long x = 0;
                        try {
                            if (totalPeople < 10) {
                                x = 2000 + (long) (Math.random() * 2000);
                            } else if (totalPeople < 50) {
                                x = 2000 + (long) (Math.random() * 3000);
                            } else if (totalPeople >= 50) {
                                x = 2000 + (long) (Math.random() * 4000);
                            }
                            Thread.sleep(x);
                        } catch (Exception e) {
                        }
                        String avatar = userInfoBean.getAvatar();
                        String avatarSmall = userInfoBean.getAvatarSmall();
                        String city = userInfoBean.getCity();
                        int gender = userInfoBean.getGender();
                        int id = userInfoBean.getId();
                        int isFollowed = 0;
                        Integer level = userInfoBean.getLevel();
                        String name = userInfoBean.getName();
                        int userNumber = userInfoBean.getUserNumber();
                        String typeImage = "";

                        MsgUserInfoBean msgUserInfoBean = new MsgUserInfoBean(avatar, avatarSmall, city, gender, id, isFollowed, level, name, userNumber, typeImage);
                        Message msg = new Message();
                        msg.obj = msgUserInfoBean;
                        if (isQuit) {
                            return;
                        }
                        myHandler.sendMessage(msg);

                    }

                }
            }
        }.start();
    }

    @Override
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList, String total) {

    }

    @Override
    public void setupFragmentComponent() {
        super.setupFragmentComponent();
        rongPresenter.attachLiveView(this);
        videoPlayerPresenter.attachLiveView(this);
        userInfoPresenter.attachView(this);
        accountPresenter.attachView(this);
        giftPresenter.attachIGiftListView(this);
        startLivePresenter.attachIStreamBaseView(this);
    }

    @Override
    public void getGiftList(List<GiftData> giftData) {
        this.giftDataList = giftData;

        giftAdapter = new PlayGiftAdapter(getActivity(), giftDataList);

        gvPresentLayout.setAdapter(giftAdapter);

        gvPresentLayout.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gvPresentLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
//                payNum = Integer.valueOf(giftDataList.get(position).getToGold());
                giftAdapter.setIsSelected(position);

                giftAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }

    public void onTouchEvent(MotionEvent event) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height1 = llpicimage.getMeasuredHeight();
        int height2 = dm.heightPixels / 2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (llInputParent.getVisibility() == View.VISIBLE) {
                    if (event.getY() > height1 && event.getY() < height2) {
                        hideKeyboard();
//                        tvChat.setVisibility(View.VISIBLE);
//                        reportBtn.setVisibility(View.GONE);
//                        periscopeBtn.setVisibility(View.VISIBLE);
//                        shareBtn.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void checkUserAccount(boolean isPay, List<AccountTypesDataBean> accountList) {
        if (isPay) {
            for (AccountTypesDataBean data : accountList) {
                if (data.getType() == 1) {
                    diamonds = (int) (Float.parseFloat(data.getAccountEarth().getData().getBalance()));
                }
            }
        }
    }

    @Override
    public void isPaySuccess(int position, int type) {
        diamonds = diamonds - payNum;
        if (type == Constants.MSG_TYPE_DANMU) {
            //显示弹幕消息
            showDanmuMesg(messageContent);
        }
    }

    @Override
    public void onGetPrepayId(BuyVirtualCoinsResponse response) {

    }

    @Override
    public void onQueryRechargeRecoderList(QueryChargeInfoListResponse response) {

    }

    @Override
    public void getRechargeListInfoResponse(List<GetRechargeListResponseData> chargeList) {

    }

    @Override
    public void onGetFansContributionToatal(String total) {
        if (total != null) {
            String totalNum = ((int) Float.parseFloat(total)) + "";
            diamondValue.setText(totalNum);
            sendLiveContentMessage(totalNum, 1);//1:推送贡献榜总数
        } else {
            diamondValue.setText("0");
            sendLiveContentMessage("0", 1);
        }

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

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", specUserInfoBean);
        bundle.putInt("anchorId", appUser.getId());
        PersionalInfoPanelFragment persionalInfoPanel = new PersionalInfoPanelFragment();
        persionalInfoPanel.setArguments(bundle);
        persionalInfoPanel.show(getActivity().getSupportFragmentManager(), "LiveMasterPersionalInfoPanel");
        persionalInfoPanel.setOnAtUserListener(this);

    }

    @Override
    public void modifyStreamStateAsLive(boolean streamState) {
        this.streamState = streamState;
    }

    @Override
    public void modifyWatchedNumResult() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeHandler.removeCallbacks(timeRunnable);
        pushWatchedNumTimer.cancel();
    }

    /**
     * @TA按键时调用
     */
    @Override
    public void onAtUser(String nickName) {
        showChat(nickName);
        if (persionalInfoPanel != null && persionalInfoPanel.isAdded()) {
            persionalInfoPanel.dismiss();
        }
    }
}