package com.ddm.live.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.constants.Constants;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.ui.widget.CustomDialog;
import com.ddm.live.utils.DialogUtils;
import com.ddm.live.utils.DrawableSettingUtils;
import com.ddm.live.views.iface.IListLiveView;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/12.
 */

public class BFLiveOverFragment extends BaseFragment implements IListLiveView {
    private AppComponent appComponent;
    private Dialog preStartDialog;
    private String headUrl;
    private String avatarSmall;
    private String name;
    private String qnID;
    private String topic;
    private String watchersNum;
    private UMImage image;
    public static Bitmap bitmap = null;
    private CustomDialog deleteDialog;
    private CustomDialog.Builder deleteBuilder;
    private LiveListPresenter presenter;
    private ProgressBarDialogFragment progressBarDialogFragment;
    @BindView(R2.id.bf_over_bg)
    ImageView imgBackground;

    @BindView(R2.id.bf_over_user_img)
    ImageView imgUserHeader;

    @BindView(R2.id.bf_over_user_name)
    TextView txUserName;

    @BindView(R2.id.bf_over_total_num)
    TextView txTotalNum;
    @BindView(R.id.live_over_level_tag)
    ImageView levelTagView;

    @OnClick(R2.id.bf_over_back_btn)
    public void overToBack() {
//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.activity_close_form_bottom);
    }

    @OnClick(R2.id.bf_over_delete)
    public void delete() {
            deleteBuilder = new CustomDialog.Builder(getActivity());
            deleteBuilder.setMessage("删除后将不再保存该直播视频");
            deleteBuilder.setTitle("确定删除？");
            deleteBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if(progressBarDialogFragment==null){
                        progressBarDialogFragment=new ProgressBarDialogFragment();
                    }
                    if(!progressBarDialogFragment.isAdded()){
                        progressBarDialogFragment.show(getActivity().getFragmentManager(),"");
                    }
                    presenter.deleteStream2(qnID);//删除视频
                }
            });
            deleteBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            deleteDialog = deleteBuilder.create(R.layout.quit_room_tip_layout);
            deleteDialog.show();
    }

    //分享到朋友圈
    @OnClick(R2.id.blue_over_wx_circle_share_btn)
    public void preShareWxCircle() {
        share(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    //分享到微信好友
    @OnClick(R2.id.blue_over_wechat_share_btn)
    public void weChatShare() {
        share(SHARE_MEDIA.WEIXIN);
    }
    //分享到QQ好友

    @OnClick(R2.id.blue_over_qq_share_btn)
    public void qqShare() {
        share(SHARE_MEDIA.QQ);
    }

    //分享到QQ空间
    @OnClick(R2.id.blue_over_qq_zone_share_btn)
    public void qqZoneShare() {
        share(SHARE_MEDIA.QZONE);
    }

    //分享到微博
    @OnClick(R2.id.blue_over_weibo_share_btn)
    public void weiboShare() {
        share(SHARE_MEDIA.SINA);
    }

    public void share(SHARE_MEDIA shareMedia) {
        preStartDialog.show();
        new ShareAction(getActivity()).setPlatform(shareMedia).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(Constants.SHARE_TITLE)
                .withText(name +getResources().getString(R.string.xiaozhu_share_pre_title) + topic + getResources().getString(R.string.xiaozhu_share_after_title))
                .withTargetUrl(Constants.SHARE_SERVER_URL + qnID + "/")
                .share();
        preStartDialog.cancel();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"分享成功",Toast.LENGTH_SHORT).show();

            preStartDialog.cancel();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AppApplication.getInstance(),"分享失败",Toast.LENGTH_SHORT).show();
            preStartDialog.cancel();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"取消分享",Toast.LENGTH_SHORT).show();

            preStartDialog.cancel();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bflive_over, container, false);
//        StatusBarUtils.setWindowStatusBarTranslucent(getActivity());
        ButterKnife.bind(this, view);
        name = appUser.getNickname();
        headUrl = appUser.getHeadimgurl();
        avatarSmall = appUser.getSmallHeadimgurl();
        preStartDialog = DialogUtils.createLoadingDialog(getActivity(), "");
        watchersNum = getArguments().getString("watchers");
        qnID = getArguments().getString("qnID");
        topic=getArguments().getString("topic");
        presenter.attachIView(this);
        initUI();
        return view;
    }

    private void initUI() {
        image = new UMImage(getActivity(), avatarSmall);
        txUserName.setText(name);
        txTotalNum.setText(watchersNum);
        //设置小头像
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

        //设置背景图片
        if (headUrl == null || headUrl.isEmpty() || bitmap == null) {
            imgBackground.setImageResource(R.mipmap.bg_error);
        } else if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            imgBackground.setVisibility(View.VISIBLE);
            imgBackground.setImageDrawable(drawable);
        }

        int levelTag = (appUser.getLevel() + 16 - 1) / 16;
        String tagName = "resource_v_10" + levelTag;
        int tagId = DrawableSettingUtils.getDrawResourceID(tagName);
        levelTagView.setImageResource(tagId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        presenter = presenterComponent.getLiveListPresenter();
    }

    @Override
    public void setInfo2List(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean isFirst) {

    }

    @Override
    public void setInfo2MasterList(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean isFirst) {

    }

    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {

    }

    @Override
    public void deleteStreamResult(String rs) {
        if(progressBarDialogFragment!=null && progressBarDialogFragment.isAdded()){
            progressBarDialogFragment.dismiss();
        }
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.activity_close_form_bottom);
    }

    @Override
    public void onfailed(String message) {
        if(progressBarDialogFragment!=null && progressBarDialogFragment.isAdded()){
            progressBarDialogFragment.dismiss();
        }
        toast(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmap = null;
    }
}
