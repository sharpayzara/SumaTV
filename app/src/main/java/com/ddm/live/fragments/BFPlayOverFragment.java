package com.ddm.live.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.UserInfoHomeActivity;
import com.ddm.live.constants.Constants;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.utils.DialogUtils;
import com.ddm.live.views.iface.IUserCenterView;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/9.
 */

public class BFPlayOverFragment extends BaseFragment implements IUserCenterView {

    private static final int SET_BLURRY = 0x00;
    private Dialog preStartDialog;
    private UMImage image;
    private String headUrl;
    private String avatarSmall;
    private String name;
    private String watchersNum;
    private String videoID;
    private String topic;
    public static Bitmap bitmap;
    private Integer levelTag;
    private Integer anchorID;
    private int qnID;
    private boolean isFollow;
    private boolean isLive;

    private UserCenterPresenter userCenterPresenter;

    @BindView(R2.id.bf_end_bg)
    ImageView imgBackground;

    @BindView(R2.id.bf_end_user_img)
    ImageView imgUserHeader;

    @BindView(R2.id.bf_end_user_name)
    TextView txUserName;

    @BindView(R2.id.bf_end_total_num)
    TextView txTotalNum;

    @BindView(R2.id.bf_end_user_level_tag)
    ImageView imgUserLevelTag;

    @BindView(R.id.review)
    TextView review;

    @BindView(R.id.blue_over_share_layout)
    LinearLayout shareLayout;

    @BindView(R.id.blue_over_wx_circle_share_btn)
    ImageView wxCircleShare;

    @BindView(R.id.blue_over_qq_share_btn)
    ImageView qqShare;

    @BindView(R.id.blue_over_qq_zone_share_btn)
    ImageView qqzoneShare;

    @BindView(R.id.blue_over_weibo_share_btn)
    ImageView weboShare;

    @BindView(R.id.blue_over_wechat_share_btn)
    ImageView wechatShare;
    /*@BindView(R2.id.bf_over_add_focus_layout)
    LinearLayout llAddFocusLayout;

    @BindView(R2.id.bf_over_add_focus_tag)
    TextView txAddFocusTag;

    @BindView(R2.id.bf_over_add_focus_txt)
    TextView txAddFocusTxt;

    @OnClick(R2.id.bf_over_add_focus_layout)
    public void addFocus() {
        Integer[] ids = {Integer.parseInt(anchorID)};
        if (isFollow) {
            userCenterPresenter.userCancelFocusMaster(anchorID);
        } else {
            userCenterPresenter.userFocusMaster(ids);
        }
    }*/

    @BindView(R2.id.bf_over_add_focus_img)
    ImageView imgAddFocusImg;

    @OnClick(R2.id.bf_over_add_focus_img)
    public void addUserFocus() {
        Integer[] ids = {anchorID};
        if (isFollow) {
            userCenterPresenter.userCancelFocusMaster(anchorID);
        } else {
            userCenterPresenter.userFocusMaster(ids);
        }
    }

    @OnClick(R2.id.bf_end_back_btn)
    public void overToBack() {
//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.activity_close_form_bottom);
    }

    /**
     * 前往主播个人主页
     */
    @OnClick(R.id.to_homepager)
    public void toHomePager () {

        Intent intent = new Intent(getContext(), UserInfoHomeActivity.class);
        intent.putExtra("uID",anchorID);
        intent.putExtra("username",name);
        intent.putExtra("userImg",headUrl);
        intent.putExtra("level",level);
        intent.putExtra("username",userNumber);
        intent.putExtra("sign",sign);
        intent.putExtra("isFollowed",isFollow);
        intent.putExtra("gender",gender);
        intent.putExtra("fansCount",fansCount);
        intent.putExtra("masterCount",foucusCount);
        getActivity().startActivity(intent);

    }

    @OnClick(R.id.review)
    public void review () {
        if(onReviewListener != null)
            onReviewListener.onReview();
    }

    @OnClick(R.id.blue_over_wechat_share_btn)
    public void wechatShare () {
        share(SHARE_MEDIA.WEIXIN);
    }

    @OnClick(R.id.blue_over_wx_circle_share_btn)
    public void wxcircleShare () {
        share(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @OnClick(R.id.blue_over_qq_share_btn)
    public void qqShare () {
        share(SHARE_MEDIA.QQ);
    }

    @OnClick(R.id.blue_over_qq_zone_share_btn)
    public void qqzoneShare () {
        share(SHARE_MEDIA.QZONE);
    }

    @OnClick(R.id.blue_over_weibo_share_btn)
    public void weiboShare () {
        share(SHARE_MEDIA.SINA);
    }

    public void share(SHARE_MEDIA shareMedia) {
        new ShareAction(getActivity()).setPlatform(shareMedia).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(Constants.SHARE_TITLE)
                .withText(name + getResources().getString(R.string.xiaozhu_share_pre_title) + topic + getResources().getString(R.string.xiaozhu_share_after_title))
                .withTargetUrl(Constants.SHARE_SERVER_URL + qnID + "/")
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"分享成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AppApplication.getInstance(),"分享失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"取消分享",Toast.LENGTH_SHORT).show();
        }
    };

    private int level;
    private String userNumber;
    private String sign;
    private int gender;
    private int fansCount;
    private int foucusCount;

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bflive_end, container, false);
//        StatusBarUtils.setWindowStatusBarTranslucent(getActivity());
        ButterKnife.bind(this, view);
        preStartDialog = DialogUtils.createLoadingDialog(getActivity(), "");
        headUrl = getArguments().getString("headimgUrl");
        avatarSmall = getArguments().getString("avatarsmall");
        name = getArguments().getString("uname");
        watchersNum = getArguments().getString("watchers");
        videoID = getArguments().getString("videoID");
        levelTag = getArguments().getInt("levelTag");
        anchorID = getArguments().getInt("anchorID",0);
        isFollow = getArguments().getBoolean("isFollowed",false);
        level = getArguments().getInt("level");
        sign = getArguments().getString("sign");
        userNumber = getArguments().getString("userNumber");
        gender = getArguments().getInt("gender");
        fansCount = getArguments().getInt("fansCount");
        foucusCount = getArguments().getInt("masterCount");
        isLive = getArguments().getBoolean("isLive");
        topic = getArguments().getString("topic");
        qnID = getArguments().getInt("qnid");
        initUI();
        return view;
    }

    private void initUI() {
        if (avatarSmall.isEmpty()) {
            imgUserHeader.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(getActivity())
                    .load(avatarSmall)
                    .placeholder(R.mipmap.user1)
//                    .resize(DensityUtils.dip2px(getActivity(), 100), DensityUtils.dip2px(getActivity(), 100))
//                    .centerCrop()
                    .error(R.mipmap.user1)
                    .into(imgUserHeader);
        }
        txUserName.setText(name);
        txTotalNum.setText(watchersNum);

        if (headUrl.isEmpty() || bitmap == null) {
            imgBackground.setImageResource(R.mipmap.blue_over_test_bgd_livein);
        } else if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(BlurBitmap.darkBitmap(bitmap,0.8f));
            imgBackground.setImageDrawable(drawable);
        }

        imgUserLevelTag.setImageResource(levelTag);

        if (anchorID.equals(appUser.getId())) {
//            llAddFocusLayout.setVisibility(View.INVISIBLE);
            imgAddFocusImg.setVisibility(View.INVISIBLE);
        } else if (isFollow){
//            llAddFocusLayout.setVisibility(View.VISIBLE);
            imgAddFocusImg.setVisibility(View.VISIBLE);
            imgAddFocusImg.setImageResource(R.mipmap.xz_live_end_focus_selected);
        }else if(!isFollow){
            imgAddFocusImg.setVisibility(View.VISIBLE);
            imgAddFocusImg.setImageResource(R.mipmap.xz_live_end_focus);
        }

        if(!isLive) {
            review.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setupFragmentComponent() {
        appComponent = AppApplication.get(getActivity()).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        userCenterPresenter = presenterComponent.getUserCenterPresenter();
        userCenterPresenter.attachView(this);
    }

    @Override
    public void changeResult() {

    }

    @Override
    public void getUserInfo(SpecUserInfoBean userInfo) {

    }

    @Override
    public void addFocusResult(boolean result, String msg) {
        if (result) {
            toast(msg);
//            txAddFocusTxt.setText("已关注");
//            txAddFocusTag.setVisibility(View.GONE);
            imgAddFocusImg.setImageResource(R.mipmap.xz_live_end_focus_selected);
            isFollow = true;
        } else {
            toast(msg);
//            txAddFocusTxt.setText("关注");
//            txAddFocusTag.setVisibility(View.VISIBLE);
            imgAddFocusImg.setImageResource(R.mipmap.xz_live_end_focus);
            isFollow = false;
        }
    }

    @Override
    public void cancelFocusResult(boolean result, String msg) {
        if (result) {
//            Integer[] ids = {Integer.parseInt(uID)};
//            userCenterPresenter.getUserInfo(ids);
            toast(msg);
//            txAddFocusTxt.setText("关注");
//            txAddFocusTag.setVisibility(View.VISIBLE);
            imgAddFocusImg.setImageResource(R.mipmap.xz_live_end_focus);
            isFollow = false;
        } else {
            toast(msg);
//            txAddFocusTxt.setText("已关注");
//            txAddFocusTag.setVisibility(View.GONE);
            imgAddFocusImg.setImageResource(R.mipmap.xz_live_end_focus_selected);
            isFollow = true;
        }
    }

    @Override
    public void getUploadToken(String token, String host) {

    }

    @Override
    public void onfailed(String message) {
        super.onfaild(message);
    }

    public interface OnReviewListener {
        public void onReview();
    }
    private OnReviewListener onReviewListener;
    public void setOnReviewListener (OnReviewListener onReviewListener) {
        this.onReviewListener = onReviewListener;
    }
}
