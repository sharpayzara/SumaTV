package com.ddm.live.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.activities.BFLoginActivity;
import com.ddm.live.activities.HomeActivity;
import com.ddm.live.activities.UserInfoHomeActivity;
import com.ddm.live.fragments.LiveViewFragment;
import com.ddm.live.fragments.MaskFragment;
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
import com.ddm.live.ui.DensityUtils;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.utils.ButtonUtils;
import com.ddm.live.utils.DrawableSettingUtils;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.clip.BitmapUtil;
import com.ddm.live.views.iface.IListLiveView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.rong.imlib.RongIMClient;


public class SimpleAdapter extends UltimateViewAdapter<SimpleAdapter.SimpleAdapterViewHolder> implements IListLiveView {
    private String TAG = "SimpleAdapter";
    private List<StreamsBean> listItems;
    private Context context;
    private AppUser appUser;
    //    private Dialog liveListDialog;
//    private ProgressBarDialogFragment progressBarDialogFragment;
    private MaskFragment maskFragment;
    private LiveListPresenter presenter;
    public ImageView headImageBitmap;

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

    public void notifyDataChanged(List<StreamsBean> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();

    }

    public SimpleAdapter(List<StreamsBean> listItems, Context context, AppUser appUser, ImageView headImageBitmap) {
        this.listItems = listItems;
        this.context = context;
        this.appUser = appUser;
        this.headImageBitmap = headImageBitmap;
        maskFragment = new MaskFragment();
        setupFragmentComponent();
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, final int position) {

        if (position < getItemCount() && (customHeaderView != null ? position <= listItems.size() : position < listItems.size()) && (customHeaderView != null ? position > 0 : true)) {

            final StreamsBean item = listItems.get(customHeaderView != null ? position - 1 : position);
            holder.setId(item.getId());
            /*if(position == 0){
                holder.adLayout.setVisibility(View.VISIBLE);
                holder.adLayout.clearFocus();
//                holder.adLayout.setOnTouchListener(null);
//                Log.e("zz", "position0----"+holder.adLayout.isClickable());
            }else{
                holder.adLayout.setVisibility(View.GONE);
            }*/

            int width = 800;
            int height = 400;
//            ImageLoader.getInstance().displayImage((String) listItems.get(position).get("bgImg") + "?imageView2/1/w/" + width + "/h/" + height, holder.bgImg, options, animateFirstListener);
//            ImageLoader.getInstance().displayImage((String) mList.get(position).get("bgImg"), holder.bgImg, options, animateFirstListener);

            // 截图 ?imageView2/1/w/320/h/257
//            Log.d(TAG, "onBindViewHolder: "+item.getLogoAd() );

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
            // 标题
            if (item.getSubject().equals("")) {
                holder.llVideoTag.setVisibility(View.GONE);
            } else {
                holder.llVideoTag.setVisibility(View.VISIBLE);
                holder.videoTag.setText(item.getSubject());
            }

            // 主播名称
            holder.userName.setText(item.getName());
            // 地址
            holder.location.setText(item.getAddress());
            Resources resources = context.getResources();

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
            }

            //设置用户头像
            if (item.getAvatarSmall().isEmpty()) {
                holder.userImg.setImageResource(R.mipmap.user1);
            } else {

                Picasso.with(context)
                        .load(item.getAvatarSmall())
                        .placeholder(R.mipmap.user1)
                        .error(R.mipmap.user1)
                        .into(holder.userImg);
            }

        /*主播头像设置星级小图标*/
            Integer level = item.getLevel();
            int levelTag = (level + 16 - 1) / 16;
            String tagName = "resource_v_10" + levelTag;
            int tagId = DrawableSettingUtils.getDrawResourceID(tagName);
            holder.masterLevelImage.setImageResource(tagId);

            holder.userImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), UserInfoHomeActivity.class);
                    intent.putExtra("uID", item.getUserId());
                    intent.putExtra("userImg",item.getAvatarSmall());
                    intent.putExtra("username",item.getName());
                    intent.putExtra("gender",item.getGender());
                    intent.putExtra("level",item.getLevel());
                    intent.putExtra("userNumber",item.getUserNumber());
                    intent.putExtra("sign",item.getSign());
                    context.startActivity(intent);
                }
            });

            holder.weather.setText("晴");
            holder.city.setText("北京");
            holder.temperature.setText("18℃~24℃");
            holder.userName.setText(item.getName());
            holder.pointNum.setText("456");
            holder.total.setText("553");
            holder.audienceNum.setText(String.valueOf(item.getWatchedNums()));

//            holder.itemView.setOnClickListener(onItemClickListener);

//            ((SimpleAdapterViewHolder) holder).textViewSample.setText(item.toString());
            // ((ViewHolder) holder).itemView.setActivated(selectedItems.get(position, false));
            if (mDragStartListener != null) {

//                ((ViewHolder) holder).imageViewSample.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                            mDragStartListener.onStartDrag(holder);
//                        }
//                        return_back false;
//                    }
//                });

                /*((SimpleAdapterViewHolder) holder).bgImg.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("ws", "onTouch: " +listItems.get(position).getTitle());
                        return false;
                    }
                });*/
            }
        }

    }

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
                .inflate(R.layout.recycler_view_adapter, parent, false);
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

//        holder.userImg.setImageResource(R.mipmap.user1);

        /*TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
        textView.setText(String.valueOf(getItem(position).getTitle().charAt(0)));
//        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AA70DB93"));
        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.stick_img);

        SecureRandom imgGen = new SecureRandom();
        switch (imgGen.nextInt(3)) {
            case 0:
                imageView.setImageResource(R.drawable.test_back1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.test_back2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.test_back);
                break;
        }*/

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
        // notifyItemRemoved(position);
//        notifyDataSetChanged();
        super.onItemDismiss(position);
    }
//
//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return_back Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }


    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        public View itemView;
        public int id;

        public LinearLayout adLayout;
        public ImageView bgImg;
        public ImageView userImg;
        public ImageView masterLevelImage;
        public TextView weather;
        public TextView videoStatus;
        public TextView temperature;
        public TextView introduction;
        public TextView city;
        public TextView userName;
        public TextView location;
        public TextView pointNum;
        public TextView total;
        public TextView audienceNum;
        public ImageView videoStatusNew;
        public LinearLayout llVideoTag;
        public TextView videoTag;

        public SimpleAdapterViewHolder(final View itemView, boolean isItem) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ButtonUtils.isFastDoubleClick()) {
                        return;
                    }
                    if (!appUser.isLogined()) {
                        new SweetAlertDialog(context)
                                .setTitleText("请重新登录")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent intent = new Intent(context, BFLoginActivity.class);
                                        context.startActivity(intent);
                                        ((HomeActivity) context).finish();

                                    }
                                })
                                .show();
                    } else {
                        final CharSequence[] items = {"播放", "删除"};
                        if (appUser.getId().equals(8) || appUser.getId().equals(18) || appUser.getId().equals(38)) {

                            AlertDialog dlg = new AlertDialog.Builder(getContext()).setTitle("选择操作").setItems(items,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //这里item是根据选择的方式，   在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法

                                            if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
                                                Toast.makeText(context, "当前无网络", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("NETWORK_UNAVAILABLE")) {
                                                Toast.makeText(context, "当前无网络", Toast.LENGTH_SHORT).show();
                                            } else if (!appUser.isLogined() || RongIMClient.getInstance().getCurrentConnectionStatus().name().equals("KICKED_OFFLINE_BY_OTHER_CLIENT")) {
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
                                                if (maskFragment != null && !maskFragment.isAdded()) {
                                                    maskFragment.show(((HomeActivity) context).getFragmentManager(), "");
                                                }
                                                if (which == 1) {
                                                    presenter.deleteStream2(String.valueOf(id));
                                                } else {
                                                    presenter.play2(String.valueOf(id));
                                                }
                                            }

                                        }
                                    }).create();
                            dlg.show();
                        } else {
//                            liveListDialog.show();

                            if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
                                Toast.makeText(context, "当前无网络", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (maskFragment != null && !maskFragment.isAdded()) {
                                maskFragment.show(((HomeActivity) context).getFragmentManager(), "");
                            }

                            presenter.play2(String.valueOf(id));
                        }
                    }
                }
            });

            this.itemView = itemView;
//            itemView.setOnTouchListener(new SwipeDismissTouchListener(itemView, null, new SwipeDismissTouchListener.DismissCallbacks() {
//                @Override
//                public boolean canDismiss(Object token) {
//                    Logs.d("can dismiss");
//                    return_back true;
//                }
//
//                @Override
//                public void onDismiss(View view, Object token) {
//                   // Logs.d("dismiss");
//                    remove(getPosition());
//
//                }
//            }));
            if (isItem) {
                /*textViewSample = (TextView) itemView.findViewById(
                        R.id.textview);
                imageViewSample = (ImageView) itemView.findViewById(R.id.imageview);
                progressBarSample = (ProgressBar) itemView.findViewById(R.id.progressbar);
                progressBarSample.setVisibility(View.GONE);
                item_view = itemView.findViewById(R.id.itemview);*/

                //追加的
                this.adLayout = (LinearLayout) itemView.findViewById(R.id.item_head_ad);
                this.bgImg = (ImageView) itemView.findViewById(R.id.video_img);

                String screenWidth = String.valueOf(DensityUtils.getScreenW(context));
                ViewGroup.LayoutParams lp = this.bgImg.getLayoutParams();
                lp.height = Integer.parseInt(screenWidth);
                lp.width = Integer.parseInt(screenWidth);
                this.userImg = (ImageView) itemView.findViewById(R.id.user_img_weather);
                this.masterLevelImage = (ImageView) itemView.findViewById(R.id.selected_live_list_user_level_tag);
                this.videoStatus = (TextView) itemView.findViewById(R.id.video_status);
                this.weather = (TextView) itemView.findViewById(R.id.weather);
                this.city = (TextView) itemView.findViewById(R.id.city);
                this.temperature = (TextView) itemView.findViewById(R.id.temperature);
                this.introduction = (TextView) itemView.findViewById(R.id.introduction);
                this.userName = (TextView) itemView.findViewById(R.id.user_name);
                this.location = (TextView) itemView.findViewById(R.id.location);
                this.pointNum = (TextView) itemView.findViewById(R.id.point_num);
                this.total = (TextView) itemView.findViewById(R.id.total);
                this.audienceNum = (TextView) itemView.findViewById(R.id.audience_num);
                this.videoStatusNew = (ImageView) itemView.findViewById(R.id.video_status_new);
                this.llVideoTag = (LinearLayout) itemView.findViewById(R.id.video_tag_layout);
                this.videoTag = (TextView) itemView.findViewById(R.id.video_tag_name);

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

        public void setId(int id) {
            this.id = id;
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

    //启动播放页面
    public void startViewPlayActivity(SpecStreamBean playItem) {
        PlayItem playItem1 = new PlayItem(context, playItem);
        context.startActivity(playItem1.play());
        ((HomeActivity) context).overridePendingTransition(R.anim.activity_enter_from_bottom, 0);//页面从底部弹出
        if (maskFragment != null && maskFragment.isAdded()) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        maskFragment.dismiss();
                    }catch (Exception e){

                    }
                }
            }.start();

        }
    }

    //播放流查询结果
    @Override
    public void intentPlayerActivity(SpecStreamBean playItem) {
        final SpecStreamBean playItem2 = playItem;
        final String userImgurl = String.valueOf(playItem.getAvatar());

        if (!userImgurl.isEmpty()) {
            Picasso.with(context).load(userImgurl)
                    .placeholder(R.mipmap.bg_error)
                    .error(R.mipmap.bg_error)
                    .into(headImageBitmap, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            headImageBitmap.setImageBitmap(BlurBitmap.blur(context, bitmap, false));
                            Bitmap bitmap2 = ((BitmapDrawable) headImageBitmap.getDrawable()).getBitmap();
                            LiveViewFragment.bitmap = BitmapUtil.compressBitmap(bitmap2);
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

    @Override
    public void deleteStreamResult(String rs) {
        if(maskFragment!=null && maskFragment.isAdded()){
            maskFragment.dismiss();
        }
        if (rs.equals("true")) {
//            liveListDialog.cancel();
//            progressBarDialogFragment.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("删除成功");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Log.i(TAG, "删除视频成功");

                        }
                    });
            builder.show();
        } else {
//            liveListDialog.cancel();
//            progressBarDialogFragment.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("删除失败");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Log.i(TAG, "删除视频失败");
                        }
                    });
            builder.show();
        }
    }

    @Override
    public void onfailed(String message) {
        if (maskFragment != null && maskFragment.isAdded()) {
            maskFragment.dismiss();
        }
        if (!message.equals("The refresh token is invalid.") && !message.equals("The resource owner or authorization server denied the request.")) {//刷新token不成功，重新登录
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}