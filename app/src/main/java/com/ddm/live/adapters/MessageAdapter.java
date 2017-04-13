package com.ddm.live.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.constants.Constants;
import com.ddm.live.utils.DrawableSettingUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 这里是聊天列表的数据适配器，比如大家使用的是环信或者第三方直播的聊天室功能，都会用的listview，
 * 对于聊天列表里面的交互以及显示方式，大家都可以在这里修改，以及布局文件
 * <p>
 * Success is the sum of small efforts, repeated day in and day out.
 * 成功就是日复一日那一点点小小努力的积累。
 * AndroidGroup：158423375
 * Author：Johnny
 * AuthorQQ：956595454
 * AuthorWX：Qiang_it
 * AuthorPhone：nothing
 * Created by 2016/9/22.
 */
public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String[]> data;
    private static final int MSG_TYPE_COUNT = 7;

    public MessageAdapter(Context context, List<String[]> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回当前布局的样式type
    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(data.get(position)[4]);

    }

    //返回有多少个不同的布局
    @Override
    public int getViewTypeCount() {
        return MSG_TYPE_COUNT;
    }

    public void NotifyAdapter(List<String[]> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String tvName = data.get(position)[1];
        String tvContent = data.get(position)[2];
        String headimgUrl = data.get(position)[3];
        String level = data.get(position)[6];
        String drawableName = "level_" + level;
        int drawResId= DrawableSettingUtils.getDrawResourceID(drawableName);
        int msgType = getItemViewType(position);
        ViewHolderSystemType viewHolderSystemType = null;
        ViewHolderNormalType viewHolderNormalType = null;
        ViewHolderJoinType viewHolderJoinType = null;
        ViewHolderQuitType viewHolderQuitType = null;
        ViewHolderFocusType viewHolderFocusType = null;
        ViewHolderLightType viewHolderLightType = null;
        ViewHolderGiftType viewHolderGiftType = null;
        if (convertView == null) {
            switch (msgType) {
                /***************************系统通知*******************/
                case Constants.MSG_TYPE_SYSTEM:
                    viewHolderSystemType = new ViewHolderSystemType();
                    convertView = View.inflate(mContext, R.layout.add_system_msg_layout, null);
                    viewHolderSystemType.tvContent = (TextView) convertView.findViewById(R.id.systemToast);
                    convertView.setTag(R.id.viewHolderSystemType, viewHolderSystemType);
                    break;
                /***************************普通消息*******************/
                case Constants.MSG_TYPE_NORMAL:
                    viewHolderNormalType = new ViewHolderNormalType();
                    convertView = View.inflate(mContext, R.layout.add_msg_scroll_layout, null);
                    viewHolderNormalType.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    viewHolderNormalType.imgUserHeader = (ImageView) convertView.findViewById(R.id.msg_user_header);
                    viewHolderNormalType.tvWords = (TextView) convertView.findViewById(R.id.msg_words);
                    convertView.setTag(R.id.viewHolderNormalType, viewHolderNormalType);
                    break;
                /***************************进入房间******************/
                case Constants.MSG_TYPE_JOIN:
                    viewHolderJoinType = new ViewHolderJoinType();
                    convertView = View.inflate(mContext, R.layout.add_msg_join_layout, null);
                    viewHolderJoinType.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    convertView.setTag(R.id.viewHolderJoinType, viewHolderJoinType);
                    break;

                /***************************退出房间******************/
                case Constants.MSG_TYPE_QUIT:
                    viewHolderQuitType = new ViewHolderQuitType();
                    convertView = View.inflate(mContext, R.layout.add_msg_quit_layout, null);
                    viewHolderQuitType.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    convertView.setTag(R.id.viewHolderQuitType, viewHolderQuitType);
                    break;

                /***************************添加关注******************/
                case Constants.MSG_TYPE_FOCUS:
                    viewHolderFocusType = new ViewHolderFocusType();
                    convertView = View.inflate(mContext, R.layout.add_msg_focus_layout, null);
                    viewHolderFocusType.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    convertView.setTag(R.id.viewHolderFocusType, viewHolderFocusType);
                    break;
                /**************************赠送礼物*********************/
                case Constants.MSG_TYPE_LIGHT:
                    viewHolderLightType = new ViewHolderLightType();
                    convertView = View.inflate(mContext, R.layout.add_msg_light_layout, null);
                    viewHolderLightType.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    convertView.setTag(R.id.viewHolderGiftType, viewHolderLightType);
                    break;
                /**************************赠送礼物*********************/
                case Constants.MSG_TYPE_GIFT:
                    viewHolderGiftType = new ViewHolderGiftType();
                    convertView = View.inflate(mContext, R.layout.add_msg_gift_layout, null);
                    viewHolderGiftType.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    convertView.setTag(R.id.viewHolderGiftType, viewHolderGiftType);
                    break;
            }
        } else {
            switch (msgType) {
                /***************************系统通知*******************/
                case Constants.MSG_TYPE_SYSTEM:
                    viewHolderSystemType = (ViewHolderSystemType) convertView.getTag(R.id.viewHolderSystemType);
                    break;
                /***************************普通消息*******************/
                case Constants.MSG_TYPE_NORMAL:
                    viewHolderNormalType = (ViewHolderNormalType) convertView.getTag(R.id.viewHolderNormalType);
                    break;
                /***************************进入房间*******************/
                case Constants.MSG_TYPE_JOIN:
                    viewHolderJoinType = (ViewHolderJoinType) convertView.getTag(R.id.viewHolderJoinType);
                    break;
                /***************************退出房间*******************/
                case Constants.MSG_TYPE_QUIT:
                    viewHolderQuitType = (ViewHolderQuitType) convertView.getTag(R.id.viewHolderQuitType);
                    break;
                /***************************添加关注*******************/
                case Constants.MSG_TYPE_LIGHT:
                    viewHolderLightType = (ViewHolderLightType) convertView.getTag(R.id.viewHolderLightType);
                    break;
                /***************************添加关注*******************/
                case Constants.MSG_TYPE_FOCUS:
                    viewHolderFocusType = (ViewHolderFocusType) convertView.getTag(R.id.viewHolderFocusType);
                    break;
                /***************************赠送礼物*******************/
                case Constants.MSG_TYPE_GIFT:
                    viewHolderGiftType = (ViewHolderGiftType) convertView.getTag(R.id.viewHolderGiftType);
                    break;
            }
        }

        switch (msgType) {
            /***************************系统消息*******************/
            case Constants.MSG_TYPE_SYSTEM:
//                String msg = "系统消息 欢迎" + tvName + "来到小主直播！小主直播提倡绿色直播，封面和直播内容含吸烟、低俗、引诱、暴露等都将会被封停账号，同时禁止直播聚众闹事、集会，网警24小时在线巡查哦！";
//                viewHolderSystemType.tvContent.setText(msg);

                String name_sys = "系统消息：";
                String content_sys = name_sys + "欢迎" + tvName + "来到小主直播！小主直播提倡绿色直播，封面和直播内容含吸烟、低俗、引诱、暴露等都将会被封停账号，同时禁止直播聚众闹事、集会，网警24小时在线巡查哦！";

                //同一个TextView中设置不同的字体
                SpannableStringBuilder sb_sys = new SpannableStringBuilder(content_sys);
                ForegroundColorSpan fcs_sys1 = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.white));
                ForegroundColorSpan fcs_sys2 = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.system_message_light_yellow));
//                StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14);  // 设置字体大小

                sb_sys.setSpan(fcs_sys1, 0, name_sys.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
//        sb.setSpan(ass,0,content.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体大小
//                sb.setSpan(bss, 0, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
                sb_sys.setSpan(fcs_sys2, name_sys.length(), content_sys.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

                viewHolderSystemType.tvContent.setText(sb_sys);

                break;
            /***************************普通消息******************/
            case Constants.MSG_TYPE_NORMAL:
                /**代码中设置textView的图片*/
//                Drawable nav_up = mContext.getResources().getDrawable(drawResId);
                Drawable nav_up=ContextCompat.getDrawable(mContext,drawResId);
                nav_up.setBounds(1, 1, nav_up.getMinimumWidth() / 7 * 3, nav_up.getMinimumHeight() / 7 * 3);
                viewHolderNormalType.tvContent.setCompoundDrawables(nav_up, null, null, null);
                viewHolderNormalType.tvContent.setCompoundDrawablePadding(10);
//                if (tvName.length() > 4) {
//                    tvName = tvName.substring(0, 4) + "...";
//                }
                String name = tvName + "  ";
                String content = name + tvContent;

                //同一个TextView中设置不同的字体
//                SpannableStringBuilder sb = new SpannableStringBuilder(content);
                SpannableStringBuilder sb = new SpannableStringBuilder(name);
                ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.user_name_blue_green));
//                ForegroundColorSpan fcs2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.white));
//                ForegroundColorSpan fcs2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.black));
                ForegroundColorSpan fcs2 = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.black));
//                StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14);  // 设置字体大小

//                sb.setSpan(fcs, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
                sb.setSpan(fcs2, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
//        sb.setSpan(ass,0,content.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体大小
//                sb.setSpan(bss, 0, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
//                sb.setSpan(fcs2, name.length(), content.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

                Picasso.with(mContext)
                        .load(headimgUrl)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .placeholder(R.mipmap.user1)
                        .error(R.mipmap.user1)
                        .into(viewHolderNormalType.imgUserHeader);
                viewHolderNormalType.tvContent.setText(sb);
                viewHolderNormalType.tvWords.setText(tvContent);
                break;

            /***************************进入房间*******************/
            case Constants.MSG_TYPE_JOIN:
                /**代码中设置textView的图片*/
                Drawable nav_up_join = ContextCompat.getDrawable(mContext,drawResId);
//                nav_up_join.setBounds(3, 3, nav_up_join.getMinimumWidth() / 3, nav_up_join.getMinimumHeight() / 3);
                nav_up_join.setBounds(2, 2, nav_up_join.getMinimumWidth() / 8 * 4, nav_up_join.getMinimumHeight() / 8 * 4);
                viewHolderJoinType.tvContent.setCompoundDrawables(nav_up_join, null, null, null);
                viewHolderJoinType.tvContent.setCompoundDrawablePadding(6);
//                if (tvName.length() > 4) {
//                    tvName = tvName.substring(0, 4) + "...";
//                }
                String nameJoin = tvName + "  ";
//                String contentJoin = nameJoin + tvContent;
                String contentJoin = nameJoin + Constants.MSG_JOIN;
                //同一个TextView中设置不同的字体
                SpannableStringBuilder sbJoin = new SpannableStringBuilder(contentJoin);
                ForegroundColorSpan fcsJoin = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.user_name_blue_green));
                ForegroundColorSpan fcs2Join = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.join_message_orange));
                ForegroundColorSpan fcs3Join = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.white));
//                StyleSpan bssJoin = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14);  // 设置字体大小

                sbJoin.setSpan(fcsJoin, 0, nameJoin.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
//        sb.setSpan(ass,0,content.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体大小
//                sbJoin.setSpan(bssJoin, 0, contentJoin.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
                sbJoin.setSpan(fcs3Join, nameJoin.length(), contentJoin.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

                viewHolderJoinType.tvContent.setText(sbJoin);
                break;

            /***************************退出房间*******************/
            case Constants.MSG_TYPE_QUIT:
                /**代码中设置textView的图片*/
                Drawable nav_up_quit = ContextCompat.getDrawable(mContext,drawResId);
                nav_up_quit.setBounds(3, 3, nav_up_quit.getMinimumWidth() / 8 * 4, nav_up_quit.getMinimumHeight() / 8 * 4);
                viewHolderQuitType.tvContent.setCompoundDrawables(nav_up_quit, null, null, null);
                viewHolderQuitType.tvContent.setCompoundDrawablePadding(10);
//                if (tvName.length() > 4) {
//                    tvName = tvName.substring(0, 4) + "...";
//                }
                String nameQuit = tvName + "  ";
                String contentQuit = nameQuit + tvContent;

                //同一个TextView中设置不同的字体
                SpannableStringBuilder sbQuit = new SpannableStringBuilder(contentQuit);
                ForegroundColorSpan fcsQuit = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.user_name_blue_green));
                ForegroundColorSpan fcs2Quit = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.white));
                ForegroundColorSpan fcs3Quit = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.join_message_orange));
//                StyleSpan bssQuit = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14);  // 设置字体大小

                sbQuit.setSpan(fcsQuit, 0, nameQuit.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
//        sb.setSpan(ass,0,content.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体大小
//                sbQuit.setSpan(bssQuit, 0, contentQuit.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
                sbQuit.setSpan(fcs3Quit, nameQuit.length(), contentQuit.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

//                viewHolderQuitType.tvContent.setText(sbQuit);//不显示退出消息
                break;

            /***************************添加关注*******************/
            case Constants.MSG_TYPE_FOCUS:
                /**代码中设置textView的图片*/
                Drawable nav_up_focus = ContextCompat.getDrawable(mContext,drawResId);
                nav_up_focus.setBounds(3, 3, nav_up_focus.getMinimumWidth() / 8 * 4, nav_up_focus.getMinimumHeight() / 8 * 4);
                viewHolderFocusType.tvContent.setCompoundDrawables(nav_up_focus, null, null, null);
                viewHolderFocusType.tvContent.setCompoundDrawablePadding(10);
//                if (tvName.length() > 4) {
//                    tvName = tvName.substring(0, 4) + "...";
//                }
                String nameFocus = tvName + "  ";
                String contentFocus = nameFocus + tvContent;

                //同一个TextView中设置不同的字体
                SpannableStringBuilder sbFocus = new SpannableStringBuilder(contentFocus);
                ForegroundColorSpan fcsFocus = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.user_name_blue_green));
                ForegroundColorSpan fcs2Focus = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.focus_master_message_pink));
                ForegroundColorSpan fcs3Focus = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.join_message_orange));
//                StyleSpan bssFocus = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14);  // 设置字体大小

                sbFocus.setSpan(fcsFocus, 0, nameFocus.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
//        sb.setSpan(ass,0,content.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体大小
//                sbFocus.setSpan(bssFocus, 0, contentFocus.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
                sbFocus.setSpan(fcs3Focus, nameFocus.length(), contentFocus.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

                viewHolderFocusType.tvContent.setText(sbFocus);
                break;

            /***************************点亮*******************/
            case Constants.MSG_TYPE_LIGHT:

                /**代码中设置textView的图片*/
                Drawable nav_up_light = ContextCompat.getDrawable(mContext,drawResId);
                nav_up_light.setBounds(3, 3, nav_up_light.getMinimumWidth() / 8 * 4, nav_up_light.getMinimumHeight() / 8 * 4);
                viewHolderLightType.tvContent.setCompoundDrawables(nav_up_light, null, null, null);
                viewHolderLightType.tvContent.setCompoundDrawablePadding(10);
//                    if (tvName.length() > 4) {
//                        tvName = tvName.substring(0, 4) + "...";
//                    }
                String namelight = tvName + "  ";
                String contentlight = namelight + tvContent;
                //同一个TextView中设置不同的字体
                SpannableStringBuilder sbLight = new SpannableStringBuilder(contentlight);
                ForegroundColorSpan fcsLight = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.user_name_blue_green));
                ForegroundColorSpan fcs2Light = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.font_dark_orange));
//                    StyleSpan bssLight = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
                sbLight.setSpan(fcsLight, 0, namelight.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
                sbLight.setSpan(fcsLight, 0, contentlight.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
                sbLight.setSpan(fcs2Light, namelight.length(), contentlight.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

                viewHolderLightType.tvContent.setText(sbLight);
                break;

            /***************************赠送礼物*******************/
            case Constants.MSG_TYPE_GIFT:
                /**代码中设置textView的图片*/
                Drawable nav_up_gift = ContextCompat.getDrawable(mContext,drawResId);
                nav_up_gift.setBounds(3, 3, nav_up_gift.getMinimumWidth() / 8 * 4, nav_up_gift.getMinimumHeight() / 8 * 4);
                viewHolderGiftType.tvContent.setCompoundDrawables(nav_up_gift, null, null, null);
                viewHolderGiftType.tvContent.setCompoundDrawablePadding(10);
//                if (tvName.length() > 4) {
//                    tvName = tvName.substring(0, 4) + "...";
//                }
                String nameGift = tvName + "  ";
                String contentGift = nameGift + tvContent;

                //同一个TextView中设置不同的字体
                SpannableStringBuilder sbGift = new SpannableStringBuilder(contentGift);
                ForegroundColorSpan fcsGift = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.user_name_blue_green));
                ForegroundColorSpan fcs2Gift = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.red));
                ForegroundColorSpan fcs3Gift = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.join_message_orange));
//                StyleSpan bssGift = new StyleSpan(android.graphics.Typeface.BOLD); // 设置字体样式
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14);  // 设置字体大小

                sbGift.setSpan(fcsGift, 0, nameGift.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置名字的字体颜色
//        sb.setSpan(ass,0,content.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体大小
//                sbGift.setSpan(bssGift, 0, contentGift.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);//设置字体样式
                sbGift.setSpan(fcs3Gift, nameGift.length(), contentGift.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);//设置消息颜色

                viewHolderGiftType.tvContent.setText(sbGift);
                break;
        }
        return convertView;
    }


    class ViewHolderNormalType {
        ImageView imgUserHeader;
        TextView tvContent;
        TextView tvWords;
    }

    class ViewHolderSystemType {
        TextView tvContent;
    }

    class ViewHolderJoinType {
        TextView tvContent;
    }

    class ViewHolderQuitType {
        TextView tvContent;
    }

    class ViewHolderFocusType {
        TextView tvContent;
    }

    class ViewHolderLightType {
        TextView tvContent;
    }

    class ViewHolderGiftType {
        TextView tvContent;
    }
}