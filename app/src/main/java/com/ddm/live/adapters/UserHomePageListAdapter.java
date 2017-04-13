package com.ddm.live.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.activities.UserInfoHomeActivity;
import com.ddm.live.constants.Constants;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.MaskFragment;
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
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.ui.ActionSheetDialog;
import com.ddm.live.ui.DensityUtils;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.views.iface.IListLiveView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.rong.imlib.RongIMClient;

/**
 * Created by JiGar on 2016/10/11.
 */
public class UserHomePageListAdapter extends BaseAdapter implements IListLiveView {

    private Context context;
    private List<StreamsBean> streamInfoList;
    private boolean isSelf;
    private LiveListPresenter presenter;
    private LiveListPresenter presenterPlay;
    private StreamsBean item;
    private ImageView headImageBitmap;
    private ShareBordFrament shareBordFrament;
    private AppUser appUser = AppApplication.getInstance().getAppUser();
    private MaskFragment maskFragment;
    public UserHomePageListAdapter(Context context, List<StreamsBean> streamInfoList, boolean isSelf, ImageView headImageBitmap) {
        this.context = context;
        this.streamInfoList = streamInfoList;
        this.isSelf = isSelf;
        this.headImageBitmap = headImageBitmap;
        maskFragment = new MaskFragment();
        setupFragmentComponent();
    }

    @Override
    public int getCount() {
        return streamInfoList.size();
    }

    public void notifyDataChange(List<StreamsBean> streamInfoList) {
        this.streamInfoList = streamInfoList;
        notifyDataSetChanged();
    }

    @Override
    public StreamsBean getItem(int position) {
        return streamInfoList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return this.streamInfoList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recycler_view_adapter_userself_page, null);
            holder = new ViewHolder();
            holder.bgImg = (ImageView) convertView.findViewById(R.id.user_home_page_video_img);
            String screenWidth = String.valueOf(DensityUtils.getScreenW(context));
            ViewGroup.LayoutParams lp = holder.bgImg.getLayoutParams();
            lp.height = Integer.parseInt(screenWidth);
            lp.width = Integer.parseInt(screenWidth);

           // holder.userImg = (ImageView) convertView.findViewById(R.id.user_home_page_user_img);
            holder.videoTimeStatus = (ImageView) convertView.findViewById(R.id.user_home_page_video_status);
            holder.videoTimeContentDate = (TextView) convertView.findViewById(R.id.user_home_page_video_date);
            holder.videoAddress = (TextView) convertView.findViewById(R.id.user_home_page_video_address);
            holder.videoTitle = (TextView) convertView.findViewById(R.id.user_home_video_title);
            holder.deleteBtn = (ImageView) convertView.findViewById(R.id.user_home_page_video_deleted_btn);
            holder.viewerNum = (TextView) convertView.findViewById(R.id.viewer_num);
            holder.shareBtn = (ImageView) convertView.findViewById(R.id.user_home_page_video_share_btn);
            holder.videoMore = (ImageView) convertView.findViewById(R.id.user_home_video_more_btn);
            holder.deleteBtn.setVisibility(View.INVISIBLE);
            holder.shareBtn.setVisibility(View.INVISIBLE);
           // holder.videoTimeStatus.setVisibility(View.INVISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (streamInfoList.get(position).getAvatar().isEmpty()) {
            Picasso.with(context)
                    .load(R.mipmap.bg_error)
                    .placeholder(R.mipmap.bg_error)
                    .error(R.mipmap.bg_error)
                    .into(holder.bgImg);

        } else {
            String screenWidth = String.valueOf(DensityUtils.getScreenW(context));
            Picasso.with(context)
                    .load(streamInfoList.get(position).getAvatar())
                    .placeholder(R.mipmap.bg_error)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .resize(Integer.parseInt(screenWidth), Integer.parseInt(screenWidth))
                    .centerCrop()
                    .error(R.mipmap.bg_error)
                    .into(holder.bgImg);
        }
        /*Resources resources = context.getResources();
        if (item.getStatus() == 0) {
            // 橘色直播文字
            String statusString = resources.getString(R.string.stream_status_living);
            holder.videoStatus.setText(statusString);
//                holder.videoStatusNew.setVisibility(View.VISIBLE);
            holder.videoStatusNew.setBackgroundResource(R.mipmap.zhibo_status);
//                Drawable drawable = resources.getDrawable(R.drawable.corner_bg_organge);
//                holder.videoStatus.setBackground(drawable);
        } else {
            // 蓝色录像文字
            String statusString = resources.getString(R.string.stream_status_record);
            holder.videoStatus.setText(statusString);
            holder.videoStatusNew.setBackgroundResource(R.mipmap.huifang_status);
//                Drawable drawable = resources.getDrawable(R.drawable.corner_bg_blue);
//                holder.videoStatus.setBackground(drawable);
        }*/
    /*    if (streamInfoList.get(position).getAvatarSmall().isEmpty()) {
            holder.userImg.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(context)
                    .load(streamInfoList.get(position).getAvatarSmall())
//                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(holder.userImg);
        }*/
        // 主播地址
        holder.videoAddress.setText(streamInfoList.get(position).getAddress());

        // 视频时间
        String timeString = streamInfoList.get(position).getCreatedAt();
        String[] strArray = timeString.split(" ");
        holder.videoTimeContentDate.setText(strArray[0]);
        if (streamInfoList.get(position).getStatus() == 0) {
            holder.videoTimeStatus.setBackgroundResource(R.mipmap.zhibo_status);
            holder.videoTimeStatus.setVisibility(View.VISIBLE);
            holder.viewerNum.setText((Html.fromHtml("<font color=#333333>"+String.valueOf(streamInfoList.get(position).getWatchedNums())+"</font>")+"人观看"));
        } else {

            holder.videoTimeStatus.setBackgroundResource(R.mipmap.huifang_status);
           // holder.videoTimeStatus.setVisibility(View.INVISIBLE);
            holder.viewerNum.setText((Html.fromHtml("<font color=#333333>"+String.valueOf(streamInfoList.get(position).getWatchedNums())+"</font>")+"人看过"));
        }

//        holder.videoTitle.setText(streamInfoList.get(position).getStreamTitle());
        holder.videoTitle.setText(streamInfoList.get(position).getSubject());
        final UserInfoHomeActivity activity = (UserInfoHomeActivity) context;
        holder.videoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appUser.isLogined() || !RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("CONNECTED")) {
                    appUser.setIsLogined(false);
                    new SweetAlertDialog(context)
                            .setTitleText("请重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    context.startActivity(new Intent(context, BFLoginActivity.class));
                                }
                            })
                            .show();

                } else {
                    if (isSelf) {
                        new ActionSheetDialog(activity)
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("播放视频", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                if (RongYunPresenter.isInLivingRoom) {
                                                    Toast.makeText(context, "正在直播间中，赶快添加关注吧", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if(!maskFragment.isAdded()){
                                                        maskFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                    }
                                                    presenterPlay.play2(String.valueOf(streamInfoList.get(position).getId()));
                                                }

                                            }
                                        })
                                .addSheetItem("分享", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                if(shareBordFrament==null){
                                                    shareBordFrament=new ShareBordFrament();
                                                }
                                                if(!shareBordFrament.isAdded()){
                                                    Bundle bundle=new Bundle();
                                                    bundle.putString("avatarSmall",streamInfoList.get(position).getAvatarSmall());
                                                    bundle.putString("name",streamInfoList.get(position).getName());
                                                    bundle.putInt("qnId",streamInfoList.get(position).getId());
                                                    shareBordFrament.setArguments(bundle);
                                                    shareBordFrament.show(((UserInfoHomeActivity)context).getSupportFragmentManager(),"share");
                                                }
                                            }
                                        })
                                .addSheetItem("删除视频", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                deleteItem = position;
                                                if(!maskFragment.isAdded()){
                                                    maskFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                }
                                                presenterPlay.deleteStream2(String.valueOf(streamInfoList.get(position).getId()));
                                            }
                                        }).show();
                    } else {
                        new ActionSheetDialog(activity)
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("播放视频", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                if (RongYunPresenter.isInLivingRoom) {
                                                    Toast.makeText(context, "正在直播间中，赶快添加关注吧", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if(!maskFragment.isAdded()){
                                                        maskFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                    }
                                                    presenterPlay.play2(String.valueOf(streamInfoList.get(position).getId()));
                                                }
                                            }
                                        })
//                            .addSheetItem("举报", ActionSheetDialog.SheetItemColor.Blue,
//                                    new ActionSheetDialog.OnSheetItemClickListener() {
//                                        @Override
//                                        public void onClick(int which) {
//                                            deleteItem = position;
//                                            progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
//                                            presenterPlay.deleteStream2(String.valueOf(streamInfoList.get(position).getId()));
//                                        }
//                                    })
                                .addSheetItem("分享", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                new ShareAction(activity)
                                                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                                                        .setShareboardclickCallback(new ShareBoardlistener() {
                                                            @Override
                                                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                                if(!maskFragment.isAdded()){
                                                                    maskFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                                }
                                                                new ShareAction(activity).
                                                                        setPlatform(share_media).setCallback(umShareListener)
                                                                        .withMedia(new UMImage(context, streamInfoList.get(position).getAvatar()))
                                                                        .withTitle(Constants.SHARE_TITLE)
                                                                        .withText(streamInfoList.get(position).getName()+context.getResources().getString(R.string.xiaozhu_share_pre_title) +streamInfoList.get(position).getSubject() + context.getResources().getString(R.string.xiaozhu_share_after_title))
                                                                        .withTargetUrl(Constants.SHARE_SERVER_URL + streamInfoList.get(position).getId() + "/")
                                                                        .setCallback(umShareListener)
                                                                        .share();
                                                            }
                                                        }).open();
                                            }
                                        }).show();
                    }
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {

        private ImageView bgImg;
       // private ImageView userImg;
        private ImageView videoTimeStatus;
        private TextView videoTimeContentDate;
        private TextView videoAddress;
        private ImageView deleteBtn;
        private ImageView shareBtn;
        private TextView videoTitle;
        private ImageView videoMore;
        private TextView viewerNum;

    }

    private int deleteItem = 0;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(context).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenter = presenterComponent.getLiveListPresenter();
        UserInfoHomeActivity activity = new UserInfoHomeActivity();
        presenter.attachIView(activity);

        presenterPlay = presenterComponent.getLiveListPresenter();
        presenterPlay.attachIView(this);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"分享成功",Toast.LENGTH_SHORT).show();
            maskFragment.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AppApplication.getInstance(),"分享失败",Toast.LENGTH_SHORT).show();
            maskFragment.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"取消分享",Toast.LENGTH_SHORT).show();
            maskFragment.dismiss();
        }
    };

    @Override
    public void setInfo2List(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean hasNext) {

    }

    @Override
    public void setInfo2MasterList(List<StreamsBean> liveListItems, int currentPage, boolean hasPrevious, boolean isFirst) {

    }

    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {
        final SpecStreamBean playItem2 = playItem;
        final String userImgUrl = String.valueOf(playItem.getAvatar());
        if (!userImgUrl.isEmpty()) {
            Picasso.with(context).load(userImgUrl)
                    .placeholder(R.mipmap.bg_error)
                    .error(R.mipmap.bg_error)
                    .into(headImageBitmap, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            headImageBitmap.setImageBitmap(BlurBitmap.blur(context, bitmap,false));
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
        PlayItem playItem1 = new PlayItem(context, playItem);
        context.startActivity(playItem1.play());
        ((UserInfoHomeActivity)context).overridePendingTransition(R.anim.activity_enter_from_bottom, 0);
    }

    @Override
    public void deleteStreamResult(String rs) {
        if (rs.equals("true")) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("删除成功");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            builder.show();*/

            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("删除成功")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            maskFragment.dismiss();
                            sweetAlertDialog.dismiss();
//                            EventBus.getDefault().post(new RefreshUserHomeVideoListEvent());
                            streamInfoList.remove(deleteItem);
                            notifyDataSetChanged();
                        }
                    })
                    .show();
        } else {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("删除失败");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            builder.show();*/

            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("删除失败")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            maskFragment.dismiss();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onfailed(String message) {
        switch (message) {
            case com.ddm.live.constants.Constants.FAILED_CONNECT:
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("当前网络状况不佳")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                maskFragment.dismiss();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;

            default:
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(message)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                maskFragment.dismiss();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    private Bitmap compressBitmap(Bitmap bitmap) {
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

}
