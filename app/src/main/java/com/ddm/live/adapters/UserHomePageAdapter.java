package com.ddm.live.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.activities.UserInfoHomeActivity;
import com.ddm.live.activities.VideoPlayerActivity;
import com.ddm.live.constants.Constants;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.ProgressBarDialogFragment;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.ui.ActionSheetDialog;
import com.ddm.live.ui.DensityUtils;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.views.iface.IListLiveView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
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


public class UserHomePageAdapter extends UltimateViewAdapter<UserHomePageAdapter.SimpleAdapterViewHolder> implements IListLiveView {
    private String TAG = "SimpleAdapter";
    private LiveListPresenter presenter;
    private List<StreamsBean> listItems;
    private boolean isSelf;
    private Context context;
    private ImageView headImageBitmap;
    private ProgressBarDialogFragment progressBarDialogFragment;
    private int deletePositon;

    public void setListItems(List<StreamsBean> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<StreamsBean> getListItems() {
        return listItems;
    }

    public UserHomePageAdapter(List<StreamsBean> listItems, Context context, boolean isSelf, ImageView headImageBitmap) {
        this.listItems = listItems;
        this.context = context;
        this.isSelf = isSelf;
        this.headImageBitmap = headImageBitmap;

        setupFragmentComponent();
        progressBarDialogFragment = new ProgressBarDialogFragment();
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, final int position) {

        if (position < getItemCount() && (customHeaderView != null ? position <= listItems.size() : position < listItems.size()) && (customHeaderView != null ? position > 0 : true)) {

            final StreamsBean item = listItems.get(customHeaderView != null ? position - 1 : position);

            if (item.getAvatar().isEmpty()) {
                Picasso.with(context)
                        .load(R.mipmap.bg_error)
                        .placeholder(R.mipmap.bg_error)
                        .error(R.mipmap.bg_error)
                        .into(holder.bgImg);

            } else {
                String screenWidth = String.valueOf(DensityUtils.getScreenW(context));
                Picasso.with(context)
                        .load(item.getAvatar())
                        .placeholder(R.mipmap.bg_error)
//                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .resize(Integer.parseInt(screenWidth), Integer.parseInt(screenWidth))
                        .centerCrop()
                        .error(R.mipmap.bg_error)
                        .into(holder.bgImg);
            }

            if (item.getAvatarSmall().isEmpty()) {
                holder.userImg.setImageResource(R.mipmap.user1);
            } else {
                Picasso.with(context)
                        .load(item.getAvatarSmall())
//                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .placeholder(R.mipmap.user1)
                        .error(R.mipmap.user1)
                        .into(holder.userImg);
            }
            // 主播名称
            holder.videoUser.setText(item.getName());

            // 视频时间
            String timeString = item.getCreatedAt();
            String[] strArray = timeString.split(" ");
            holder.videoTimeContentDate.setText(strArray[0]);

            Resources resources = context.getResources();
            if (item.getStatus() == 0) {
                String statusString = resources.getString(R.string.user_home_video_living_text);
                holder.videoTimeStatus.setVisibility(View.VISIBLE);
            } else {
                String statusString = resources.getString(R.string.user_home_video_record_text);
                holder.videoTimeStatus.setVisibility(View.INVISIBLE);
            }

//        holder.videoTitle.setText(item.getStreamTitle());
            holder.videoTitle.setText(item.getSubject());
            final UserInfoHomeActivity activity = (UserInfoHomeActivity) context;
            holder.videoMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelf) {
                        new ActionSheetDialog(activity)
                                .builder()
                                .setCancelable(false)
                                .setCanceledOnTouchOutside(false)
                                .addSheetItem("播放视频", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                if(!progressBarDialogFragment.isAdded()){
                                                    progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                }
                                                presenter.play2(String.valueOf(item.getId()));
                                            }
                                        })
                                .addSheetItem("分享", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                new ShareAction(activity)
                                                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                                        .setShareboardclickCallback(new ShareBoardlistener() {
                                                            @Override
                                                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                                if(!progressBarDialogFragment.isAdded()){
                                                                    progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                                }
                                                                new ShareAction(activity).
                                                                        setPlatform(share_media).setCallback(umShareListener)
                                                                        .withMedia(new UMImage(context, item.getAvatar()))
                                                                        .withTitle(Constants.SHARE_TITLE)
                                                                        .withText(item.getName()+context.getResources().getString(R.string.xiaozhu_share_pre_title) +item.getSubject()+ context.getResources().getString(R.string.xiaozhu_share_after_title))
                                                                        .withTargetUrl(Constants.SHARE_SERVER_URL + item.getId() + "/")
                                                                        .setCallback(umShareListener)
                                                                        .share();
                                                            }
                                                        }).open();
                                            }
                                        })
                                .addSheetItem("删除视频", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                deletePositon = position;
                                                if(!progressBarDialogFragment.isAdded()){
                                                    progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                }
                                                presenter.deleteStream2(String.valueOf(item.getId()));

                                            }
                                        }).show();
                    } else {
                        new ActionSheetDialog(activity)
                                .builder()
                                .setCancelable(false)
                                .setCanceledOnTouchOutside(false)
                                .addSheetItem("播放视频", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                if(!progressBarDialogFragment.isAdded()){
                                                    progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                }
                                                presenter.play2(String.valueOf(item.getId()));
                                            }
                                        })
                                .addSheetItem("删除举报", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                deletePositon = position;
                                                if(!progressBarDialogFragment.isAdded()){
                                                    progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                }
                                                presenter.deleteStream2(String.valueOf(item.getId()));
                                            }
                                        })
                                .addSheetItem("分享", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                new ShareAction(activity)
                                                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                                        .setShareboardclickCallback(new ShareBoardlistener() {
                                                            @Override
                                                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                                if(!progressBarDialogFragment.isAdded()){
                                                                    progressBarDialogFragment.show(((UserInfoHomeActivity) context).getFragmentManager(), "");
                                                                }
                                                                new ShareAction(activity).
                                                                        setPlatform(share_media).setCallback(umShareListener)
                                                                        .withMedia(new UMImage(context, item.getAvatar()))
                                                                        .withTitle(Constants.SHARE_TITLE)
                                                                        .withText(item.getName()+context.getResources().getString(R.string.xiaozhu_share_pre_title) +item.getSubject()+ context.getResources().getString(R.string.xiaozhu_share_after_title))
                                                                        .withTargetUrl(Constants.SHARE_SERVER_URL + item.getId() + "/")
                                                                        .setCallback(umShareListener)
                                                                        .share();
                                                            }
                                                        }).open();
                                            }
                                        }).show();
                    }
                }
            });

        }

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

    @Override
    public int getAdapterItemCount() {
        return listItems.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_weather, parent, false);
                .inflate(R.layout.recycler_view_adapter_user_home_page, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }


    public void insert(StreamsBean item, int position) {
        insert(listItems, item, position);
    }

    public void remove(int position) {
        remove(listItems, position);
    }

    public void clear() {
        clear(listItems);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }

    public void swapPositions(int from, int to) {
        swapPositions(listItems, from, to);
    }

    @Override
    public long generateHeaderId(int position) {
        // URLogs.d("position--" + position + "   " + getItem(position));
        /*if (getItem(position).length() > 0)
            return_back getItem(position).charAt(0);
        else return_back -1;*/
        //修改的
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);
        super.onItemDismiss(position);
    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }

    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        public View itemView;

        private ImageView bgImg;
        private ImageView userImg;
        private ImageView videoTimeStatus;
        private TextView videoTimeContentDate;
        private TextView videoUser;
        private ImageView deleteBtn;
        private ImageView shareBtn;
        private TextView videoTitle;
        private ImageView videoMore;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            this.itemView = itemView;
            if (isItem) {
                this.bgImg = (ImageView) itemView.findViewById(R.id.user_home_page_video_img);
                String screenWidth = String.valueOf(DensityUtils.getScreenW(context));
                ViewGroup.LayoutParams lp = this.bgImg.getLayoutParams();
                lp.height = Integer.parseInt(screenWidth);
                lp.width = Integer.parseInt(screenWidth);

                this.userImg = (ImageView) itemView.findViewById(R.id.user_home_page_user_img);
                this.videoTimeStatus = (ImageView) itemView.findViewById(R.id.user_home_page_video_status);
                this.videoTimeContentDate = (TextView) itemView.findViewById(R.id.user_home_page_video_date);
                this.videoUser = (TextView) itemView.findViewById(R.id.user_home_page_video_user);
                this.videoTitle = (TextView) itemView.findViewById(R.id.user_home_video_title);
                this.deleteBtn = (ImageView) itemView.findViewById(R.id.user_home_page_video_deleted_btn);
                this.shareBtn = (ImageView) itemView.findViewById(R.id.user_home_page_video_share_btn);
                this.videoMore = (ImageView) itemView.findViewById(R.id.user_home_video_more_btn);
                deleteBtn.setVisibility(View.INVISIBLE);
                shareBtn.setVisibility(View.INVISIBLE);
                videoTimeStatus.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public StreamsBean getItem(int position) {
        if (customHeaderView != null) {
            position--;
        }

        if (position < listItems.size()) {
            return listItems.get(position);
        } else {
            //需要修改
            return null;
        }
    }

    public void setupFragmentComponent() {

//        AppComponent appComponent = ((HomeActivity) getActivity()).getAppComponent();

        AppComponent appComponent = AppApplication.get(context).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenter = presenterComponent.getLiveListPresenter();

        presenter.attachIView(this);
    }

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
        progressBarDialogFragment.dismiss();
        String playUrl = (playItem.getStatus() == 0) ? playItem.getRtmpLiveUrls() : playItem.getHlsPlaybackUrls();
        String cid = String.valueOf(playItem.getRoomId());
        Integer anchorId = playItem.getUserId();
        String viedoid = String.valueOf(playItem.getStreamId());
        String uname = String.valueOf(playItem.getName());
        String userImgurl = String.valueOf(playItem.getAvatar());
        Integer id = playItem.getId();
        String userNumber = String.valueOf(playItem.getUserNumber());
        Integer tagLevel = playItem.getLevel();
        String avatarSmall = playItem.getAvatarSmall();
        String streamStatus = String.valueOf(playItem.getStatus());
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("isLive", playItem.getStatus() == 0);
        intent.putExtra("videoPath", playUrl);
        intent.putExtra("cid", cid);//roomId
        intent.putExtra("anchorid", anchorId);
        intent.putExtra("viedoid", viedoid);
        intent.putExtra("uname", uname);
        intent.putExtra("userimgurl", userImgurl);
        intent.putExtra("id", id);
        intent.putExtra("userNumber", userNumber);
        intent.putExtra("tagLevel", tagLevel);
        intent.putExtra("avatarsmall", avatarSmall);
        intent.putExtra("streamStatus", streamStatus);
        context.startActivity(intent);
    }

    @Override
    public void deleteStreamResult(String rs) {
        if (rs.equals("true")) {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("删除成功")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            progressBarDialogFragment.dismiss();
                            sweetAlertDialog.dismiss();
                            onItemDismiss(deletePositon);
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("删除失败")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            progressBarDialogFragment.dismiss();
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
                                progressBarDialogFragment.dismiss();
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
                                progressBarDialogFragment.dismiss();
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