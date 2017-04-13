package com.ddm.live.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.activities.BlueUserVideoManageActivity;
import com.ddm.live.activities.VideoPlayerActivity;
import com.ddm.live.fragments.ProgressBarDialogFragment;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;
import com.ddm.live.models.bean.live.PlayItem;
import com.ddm.live.presenter.LiveListPresenter;
import com.ddm.live.ui.DensityUtils;
import com.ddm.live.views.iface.IUserVideoListView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class SimpleUserHomeAdapter extends UltimateViewAdapter<SimpleUserHomeAdapter.SimpleAdapterViewHolder> implements IUserVideoListView {
    private String TAG = "SimpleAdapter";
    private LiveListPresenter presenter;
    private List<StreamsBean> listItems;
    private Context context;
    //    private Dialog userManageDialog;
    private ProgressBarDialogFragment progressBarDialogFragment;

    public void setListItems(List<StreamsBean> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private MyLiveListListener myLiveListListener;

    public List<StreamsBean> getListItems() {
        return listItems;
    }

    public SimpleUserHomeAdapter(List<StreamsBean> listItems, Context context, MyLiveListListener myLiveListListener) {
        this.listItems = listItems;
        this.context = context;
        this.myLiveListListener = myLiveListListener;
        setupFragmentComponent();
    }

    public void notifyDataChanged(List<StreamsBean> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, final int position) {

        if (position < getItemCount() && (customHeaderView != null ? position <= listItems.size() : position < listItems.size()) && (customHeaderView != null ? position > 0 : true)) {

            final StreamsBean item = listItems.get(customHeaderView != null ? position - 1 : position);

            int width = 800;
            int height = 400;
//            ImageLoader.getInstance().displayImage((String) listItems.get(position).get("bgImg") + "?imageView2/1/w/" + width + "/h/" + height, holder.bgImg, options, animateFirstListener);
//            ImageLoader.getInstance().displayImage((String) mList.get(position).get("bgImg"), holder.bgImg, options, animateFirstListener);

            // 截图 ?imageView2/1/w/320/h/257
//            Log.d(TAG, "onBindViewHolder: " + item.getLogoAd());

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
//                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .placeholder(R.mipmap.bg_error)
                        .resize(Integer.parseInt(screenWidth), Integer.parseInt(screenWidth))
                        .centerCrop()
                        .error(R.mipmap.bg_error)
                        .into(holder.bgImg);
            }
            // 主播名称
//            holder.videoUser.setText(item.getName());
            holder.location.setText(item.getAddress());
            holder.audienceNum.setText(String.valueOf(item.getWatchedNums()));
            // 视频时间
            String timeString = item.getCreatedAt();
//            String timeString = toTime(1, 0);
//            holder.videoTimeStatus.setText(timeString);

            // 视频内容日期 时间
//            String videoTimeDate = formatData("yyyy-MM-dd HH:mm:ss", timeString);
//            String videoTimeDate = formatData("yyyy-MM-dd HH:mm:ss", 0);
            String[] strArray = timeString.split(" ");
            holder.videoTimeContentDate.setText(strArray[0]);

//            if (item.getAvatarSmall().isEmpty()) {
//                holder.userImg.setImageResource(R.mipmap.user1);
//            } else {
//                Picasso.with(context)
//                        .load(item.getAvatarSmall())
//                        .placeholder(R.mipmap.user1)
//                        .error(R.mipmap.user1)
//                        .into(holder.userImg);
//            }

            final Integer id = item.getId();
            final String qid = String.valueOf(item.getId());
            final String uname = item.getName();
            final String uHeaderImg = item.getAvatar();
            final BlueUserVideoManageActivity activity = (BlueUserVideoManageActivity) context;

            holder.bgImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myLiveListListener.play(item);
                }
            });

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 myLiveListListener.delete(item);
                }
            });

            holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myLiveListListener.share(item);
                }
            });

            if (mDragStartListener != null) {

            }
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
//            userManageDialog.cancel();
            progressBarDialogFragment.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
//            userManageDialog.cancel();
            progressBarDialogFragment.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
//            userManageDialog.cancel();
            progressBarDialogFragment.dismiss();
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
                .inflate(R.layout.recycler_view_adapter_user_home, parent, false);
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

        public ImageView bgImg;
        public ImageView userImg;
        public TextView videoTimeStatus;
        public TextView videoTimeContentDate;
        public TextView videoUser;
        public TextView deleteBtn;
        public TextView shareBtn;
        public TextView location;
        public TextView audienceNum;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            this.itemView = itemView;
//            userManageDialog = DialogUtils.createLoadingDialog(context, "");
            progressBarDialogFragment = new ProgressBarDialogFragment();
            if (isItem) {
                this.bgImg = (ImageView) itemView.findViewById(R.id.manage_video_img);
                String screenWidth = String.valueOf(DensityUtils.getScreenW(context));
                ViewGroup.LayoutParams lp = this.bgImg.getLayoutParams();
                lp.height = Integer.parseInt(screenWidth);
                lp.width = Integer.parseInt(screenWidth);

                this.userImg = (ImageView) itemView.findViewById(R.id.manage_user_img);
                this.videoTimeStatus = (TextView) itemView.findViewById(R.id.manage_video_status);
                this.videoTimeContentDate = (TextView) itemView.findViewById(R.id.manage_video_date);
//                this.videoUser = (TextView) itemView.findViewById(R.id.manage_video_user);
                this.deleteBtn = (TextView) itemView.findViewById(R.id.manage_video_deleted_btn);
                this.shareBtn = (TextView) itemView.findViewById(R.id.manage_video_share_btn);
                this.location = (TextView) itemView.findViewById(R.id.location);
                this.audienceNum = (TextView) itemView.findViewById(R.id.audience_num);
//                deleteBtn.setVisibility(View.INVISIBLE);
//                shareBtn.setVisibility(View.INVISIBLE);
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

    private String toTime(int eTime, int sTime) {
        String result = "";
        int videoTimeInt = eTime - sTime;
        long timeLong = videoTimeInt * 1000;
        long hours = timeLong / (1000 * 60 * 60);
        long minutes = (timeLong - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeLong - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        String hour;
        String minute;
        String second;
        if (hours < 10) {
            hour = "0" + hours;
        } else {
            hour = hours + "";
        }
        if (minutes < 10) {
            minute = "0" + minutes;
        } else {
            minute = minutes + "";
        }
        if (seconds < 10) {
            second = "0" + seconds;
        } else {
            second = seconds + "";
        }
        result = hour + ":" + minute + ":" + second;
        return result;
    }

    private static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
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
    public void userListIntentPlayerActivity(boolean result, PlayItem playItem) {
//        userManageDialog.cancel();
        progressBarDialogFragment.dismiss();
        if (result) {
            //0直播 1录播
            String playUrl = (playItem.getIslive() == 0 ? playItem.getLiveAd() : playItem.getRecordAd());
            Log.d(TAG, "onNext: 播放地址:" + playUrl);
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("isLive", playItem.getIslive() == 0);
            intent.putExtra("videoPath", playUrl);
            intent.putExtra("cid", String.valueOf(playItem.getCid()));
            intent.putExtra("anchorid", String.valueOf(playItem.getUid()));
            intent.putExtra("viedoid", String.valueOf(playItem.getId()));
            intent.putExtra("uname", String.valueOf(playItem.getCname()));
            intent.putExtra("userimgurl", String.valueOf(playItem.getHeadimgurl()));
            context.startActivity(intent);
        } else {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setMessage("视频资源异常，请刷新后再试");
            dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dlg.show();
        }
    }

    @Override
    public void userListDeleteStreamResult(String rs) {
//        userManageDialog.cancel();
        progressBarDialogFragment.dismiss();
        if (rs.equals("true")) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("删除失败");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.i(TAG, "删除视频失败");
                        }
                    });
            builder.show();
        }
    }

    @Override
    public void onfailed(String message) {
        if (!message.equals("The refresh token is invalid.") && !message.equals("The resource owner or authorization server denied the request.")) {//刷新token不成功，重新登录
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public interface MyLiveListListener {
        public void play(StreamsBean streamsBean);

        public void delete(StreamsBean streamsBean);

        public void share(StreamsBean streamsBean);
    }
}