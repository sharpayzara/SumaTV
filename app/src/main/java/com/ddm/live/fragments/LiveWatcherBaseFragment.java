package com.ddm.live.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.activities.RechargeActivity;
import com.ddm.live.adapters.AudienceAdapter;
import com.ddm.live.adapters.MessageAdapter;
import com.ddm.live.animation.MagicTextView;
import com.ddm.live.constants.Constants;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.giftbeans.GiftData;
import com.ddm.live.models.bean.common.userbeans.FansFocusBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserFansInfoBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.models.bean.msgtype.LiveGiftMessage;
import com.ddm.live.models.bean.msgtype.LiveStatusMessage;
import com.ddm.live.models.bean.msgtype.LiveUserMessage;
import com.ddm.live.models.bean.msgtype.MsgGiftBean;
import com.ddm.live.models.bean.msgtype.MsgUserInfoBean;
import com.ddm.live.presenter.AccountPresenter;
import com.ddm.live.presenter.GiftPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.presenter.StartLivePresenter;
import com.ddm.live.presenter.UserInfoPresenter;
import com.ddm.live.presenter.VideoPlayerPresenter;
import com.ddm.live.ui.widget.CustomDialog;
import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.RongIMClient;
import rx.Observer;

/**
 * Created by Administrator on 2016/11/25.
 */

public class LiveWatcherBaseFragment extends BaseFragment {
    protected RongYunPresenter rongPresenter;
    protected StartLivePresenter startLivePresenter;
    protected String roomID;
    protected Bundle bundle;
    protected VideoPlayerPresenter videoPlayerPresenter;
    protected GiftPresenter giftPresenter;
    protected AccountPresenter accountPresenter;
    protected UserInfoPresenter userInfoPresenter;
    protected DanmakuView mDanmakuView;
    protected MessageAdapter messageAdapter;
    protected List<String[]> messageData = new LinkedList<>();
    protected ListView lvmessage;
    protected List<SpecUserInfoBean> userInfoList = new ArrayList<>();
    protected AudienceAdapter audienceAdapter;
    protected ShareBordFrament shareBordFrament;
    /**
     * 动画相关
     */
    protected LinearLayout llgiftcontent;
    protected List<GiftData> giftDataList;
    protected NumAnim giftNumAnim;
    protected TranslateAnimation inAnim;
    protected TranslateAnimation outAnim;
    protected List<View> giftViewCollection = new ArrayList<View>();
    protected AnimatorSet animatorSetHide = new AnimatorSet();
    protected AnimatorSet animatorSetShow = new AnimatorSet();

    protected CustomDialog rechargeDialog;
    protected CustomDialog.Builder rechargeBuilder;
    protected CustomDialog customDialog;
    protected CustomDialog.Builder quitChatroomBuilder;
    protected final int USERJOINMSGDIMISS = 5645645;
    protected String topic;
    protected int totalPeople;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        roomID = bundle.getString("roomID");
        topic=bundle.getString("topic","");
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out_left);
        giftNumAnim = new NumAnim();


        rechargeBuilder = new CustomDialog.Builder(getContext());
        rechargeBuilder.setMessage("当前余额不足");
        rechargeBuilder.setTitle("余额不足");
        rechargeBuilder.setPositiveButton("去充值", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(getContext(), RechargeActivity.class));
            }
        });
    }

    //显示用户信息
    public void setLiveUserMessageScroll2(LiveUserMessage liveUserMessage, MessageAdapter messageAdapter, List<String[]> messageData, ListView lvmessage) {
        String msgType = String.valueOf(liveUserMessage.getType());
        //用户消息显示部分，针对不同消息类型的显示在messageAdapter中处理
        MsgUserInfoBean msgUserInfoBean = liveUserMessage.getUser();
        Integer uID = msgUserInfoBean.getId();
        String uName = msgUserInfoBean.getName();
        String headerImgUrl = msgUserInfoBean.getAvatar_small();
        String level = String.valueOf(msgUserInfoBean.getLevel());
        int gender = msgUserInfoBean.getGender();
        String dmContent = liveUserMessage.getConent();
        String[] msgData = {String.valueOf(uID), uName, dmContent, headerImgUrl, msgType, String.valueOf(gender), level};
        messageData.add(msgData);
        messageAdapter.NotifyAdapter(messageData);
        lvmessage.setSelection(messageData.size());
    }

    //发送用户消息
    protected void rongSendLiveUserMessage2(final boolean isLiving,String dmContent, int msgType, MsgUserInfoBean msgUserInfoBean) {
        LiveUserMessage liveUserMessage = new LiveUserMessage(dmContent, msgType, msgUserInfoBean);
        rongPresenter.sendLiveUserMessage(liveUserMessage, roomID).subscribe(new Observer<LiveUserMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showSweetAlertDialog(e);
            }

            @Override
            public void onNext(LiveUserMessage contentMessage) {
                int msgType = contentMessage.getType();
                if (msgType == Constants.MSG_TYPE_QUIT ) {
                    rongPresenter.quitChartRoom(roomID);
                    if(isLiving){
                        rongSendStatusMessage(3);
                    }
                }
            }
        });
    }

    //发送状态消息,0 主播暂时离开，1 主播回来了，2 点赞
    public void rongSendStatusMessage(int type) {
        LiveStatusMessage liveStatusMessage = new LiveStatusMessage(type);
        rongPresenter.sendLiveStatusMessage(liveStatusMessage, roomID).subscribe(new Observer<LiveStatusMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                showSweetAlertDialog();
            }

            @Override
            public void onNext(LiveStatusMessage statusMessage) {

            }
        });
    }


    //获取发送消息用户实体bean
    protected MsgUserInfoBean getMsgUserInfoBean2() {
        MsgUserInfoBean msgUserInfoBean = new MsgUserInfoBean(appUser.getHeadimgurl(), appUser.getSmallHeadimgurl(),
                appUser.getCity(), appUser.getGender(),
                appUser.getId(), 0, appUser.getLevel(),
                appUser.getNickname(), appUser.getLeNum(), "");
        return msgUserInfoBean;
    }

    //显示弹幕消息
    public void showDanmuMesg(String mContent) {
        IDanmakuItem item = new DanmakuItem(getActivity(), new SpannableString(mContent), mDanmakuView.getWidth(), 0, R.color.font_dark_orange, 0, 1);
        mDanmakuView.addItemToHead(item);
        item.setTextSize(14);
        mDanmakuView.setVisibility(View.VISIBLE);
        mDanmakuView.show();
    }

    public void showSweetAlertDialog(Throwable e) {
        //多设备登录同一账号
        String rongConnectionStatus = RongIMClient.getInstance().getCurrentConnectionStatus().name();
        if (rongConnectionStatus.equals("KICKED_OFFLINE_BY_OTHER_CLIENT")) {
            appUser.setIsLogined(false);
            if (getActivity() == null) {
                return;
            }
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity());
            sweetAlertDialog.setTitleText("当前账号在另一台设备上登录")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            startActivity(new Intent(getActivity(), BFLoginActivity.class));
                            getActivity().finish();
                        }
                    })
                    .show();
            sweetAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    startActivity(new Intent(getActivity(), BFLoginActivity.class));
                    getActivity().finish();
                    return false;
                }
            });
        }

    }

    /**
     * 显示接收礼物消息及动画
     */
    public void showGiftMesgAndAnim(LiveGiftMessage giftMessage, String desc) {
        MsgUserInfoBean msgUserInfoBean = giftMessage.getUser();
        MsgGiftBean msgGiftBean = giftMessage.getGift();
        String id = String.valueOf(msgGiftBean.getId());
        String giftName = msgGiftBean.getName();
        //显示赠送礼物消息
        String uName = msgUserInfoBean.getName();
        String headerImgUrl = msgUserInfoBean.getAvatar();

        LiveUserMessage liveUserMessage = new LiveUserMessage(desc + giftName, Constants.MSG_TYPE_GIFT, msgUserInfoBean);

        setLiveUserMessageScroll2(liveUserMessage, messageAdapter, messageData, lvmessage);
        //显示礼物赠送动画
        showGift(id, headerImgUrl, uName, giftName);
    }

    /**
     * 显示礼物的方法
     */
    protected void showGift(String giftId, String headUrl, String presenterName, String giftName) {
        View giftView = llgiftcontent.findViewWithTag(giftId);
        /*该用户不在礼物显示列表*/
        if (giftView == null) {
                    /*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
            if (llgiftcontent.getChildCount() > 2) {
                View giftView1 = llgiftcontent.getChildAt(0);
                CircleImageView picTv1 = (CircleImageView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag();
                View giftView2 = llgiftcontent.getChildAt(1);
                CircleImageView picTv2 = (CircleImageView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag();
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*获取礼物的View的布局*/
//            giftView.setTag(tag);/*设置view标识*/
            giftView.setTag(giftId);/*设置view标识*/
            CircleImageView crvheadimage = (CircleImageView) giftView.findViewById(R.id.crvheadimage);
            CircleImageView ivgiftimage = (CircleImageView) giftView.findViewById(R.id.ivgift);
//            ivgiftimage.setBackground(getResources().getDrawable(giftImgArray[Integer.parseInt(tag)]));
            Picasso.with(getActivity())
                    .load(giftDataList.get(Integer.parseInt(giftId)).getPicUrl())
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(ivgiftimage);
            TextView presenterNameText = (TextView) giftView.findViewById(R.id.presenter_name);
            TextView giftNameText = (TextView) giftView.findViewById(R.id.giftName);
//            if (presenterName.length() > 4) {
//                presenterName = presenterName.substring(0, 4) + "...";
//            }
//            String content = "送给主播" + giftDataList.get(Integer.parseInt(tag)).getName();
            String content = "送给主播" + giftName;
            presenterNameText.setText(presenterName);
            giftNameText.setText(content);
            if (headUrl == null || headUrl.isEmpty()) {
                crvheadimage.setImageResource(R.mipmap.user1);
            } else {
                Picasso.with(getActivity())
                        .load(headUrl)
                        .placeholder(R.mipmap.user1)
                        .error(R.mipmap.user1)
                        .into(crvheadimage);

            }
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            giftNum.setText("x1");/*设置礼物数量*/
            crvheadimage.setTag(System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(1);/*给数量控件设置标记*/

            llgiftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            llgiftcontent.invalidate();/*刷新该view*/
            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*该用户在礼物显示列表*/
            CircleImageView crvheadimage = (CircleImageView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            int showNum = (Integer) giftNum.getTag() + 1;
            giftNum.setText("x" + showNum);
            giftNum.setTag(showNum);
            crvheadimage.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
        }
    }

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


    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    protected View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 50;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    /**
     * 删除礼物view
     */
    protected void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeView.startAnimation(outAnim);
                }
            });
        }
    }

    /**
     * 定时清除礼物
     */
    protected void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = llgiftcontent.getChildCount();
                if (count != 0) {
                    for (int i = 0; i < count; i++) {
                        View view = llgiftcontent.getChildAt(i);
                        CircleImageView crvheadimage = (CircleImageView) view.findViewById(R.id.crvheadimage);
                        long nowtime = System.currentTimeMillis();
                        long upTime = (Long) crvheadimage.getTag();
                        if ((nowtime - upTime) >= 3000) {
                            removeGiftView(i);
                            return;
                        }
                    }
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 3000);
    }
    //用户初次进入一次加载头像
    public void updateAudienHeaderList(List<SpecUserInfoBean> list){
        if (audienceAdapter == null) {
            audienceAdapter = new AudienceAdapter(userInfoList, getContext());
        }
                audienceAdapter.setData(userInfoList);
                audienceAdapter.notifyDataSetChanged();
          }
    //用户进入或退出时刷新用户头像列表
    public void updateAudienceHeaderList(MsgUserInfoBean msgUserInfoBean, int type) {
        switch (type) {

            case Constants.MSG_TYPE_JOIN://进入房间
//               int count = (int) (Math.random() * userInfoList.size() - 1);
                SpecUserInfoBean userInfoBean = new SpecUserInfoBean();

                userInfoBean.setId(msgUserInfoBean.getId());
                userInfoBean.setName(msgUserInfoBean.getName());
                userInfoBean.setAvatar(msgUserInfoBean.getAvatar());
                userInfoBean.setAvatarSmall(msgUserInfoBean.getAvatar_small());
                userInfoBean.setGender(msgUserInfoBean.getGender());
                userInfoBean.setCity(msgUserInfoBean.getCity());
                userInfoBean.setUserNumber(msgUserInfoBean.getUser_number());
                userInfoBean.setLevel(msgUserInfoBean.getLevel());
                FansFocusBean fansFocusBean = new FansFocusBean();
                SpecUserFansInfoBean specUserFansInfoBean = new SpecUserFansInfoBean();
                if (msgUserInfoBean.getIs_followed() == 0) {
                    fansFocusBean.setIsFollowed(false);
                } else {
                    fansFocusBean.setIsFollowed(true);
                }
                specUserFansInfoBean.setData(fansFocusBean);
                userInfoBean.setFans(specUserFansInfoBean);
                userInfoBean.setFansNumber(0);
                userInfoBean.setFansNumber(0);
                userInfoList.add(0, userInfoBean);
                if(userInfoList.size()<=100){
                    audienceAdapter.setData(userInfoList);
                }else {
                    audienceAdapter.setData(userInfoList.subList(0,99));//显示100个
                }
                audienceAdapter.notifyDataSetChanged();
                break;
            case Constants.MSG_TYPE_QUIT://退出房间
                for (SpecUserInfoBean userInfo : userInfoList) {
                    if (userInfo.getId().equals(msgUserInfoBean.getId())) {
                        userInfoList.remove(userInfo);
                        break;
                    }
                }
                if(userInfoList.size()<=100){
                    audienceAdapter.setData(userInfoList);
                }else {
                    audienceAdapter.setData(userInfoList.subList(0,99));//显示100个
                }
                audienceAdapter.setData(userInfoList);
                audienceAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();
        rongPresenter = presenterComponent.getRongYunPresenter();
        videoPlayerPresenter = presenterComponent.getVideoPlayerPresenter();
        accountPresenter = presenterComponent.getAccountPresenter();
        giftPresenter = presenterComponent.getGiftPresenter();
        userInfoPresenter = presenterComponent.getUserInfoPresenter();
        startLivePresenter = presenterComponent.getStartLivePresenter();
    }

}
