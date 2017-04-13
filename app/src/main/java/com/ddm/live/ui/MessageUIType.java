package com.ddm.live.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddm.live.R;
import com.squareup.picasso.Picasso;

/**
 * Created by JiGar on 2016/8/5.
 */
public class MessageUIType {

    /**
     * 普通消息样式 格式：@用户名 消息内容
     * @param context           上下文
     * @param uname             用户名
     * @param dmContent         聊天内容
     * @param headerImgUrl      用户头像
     * @return
     */
    public static LinearLayout addMsgScroll(Context context, String uname, String dmContent, String headerImgUrl) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvTag = new TextView(context);
        LinearLayout.LayoutParams LP_TAG = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvTag.setText("@");
        tvTag.setGravity(Gravity.CENTER_VERTICAL);
        tvTag.setTextColor(context.getResources().getColor(R.color.font_blue_play_gray));
        tvTag.setTextSize(14);
        tvTag.setLayoutParams(LP_TAG);
        linearLayout.addView(tvTag);

        TextView tvName = new TextView(context);
        LinearLayout.LayoutParams LP_Name = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvName.setText(uname);
        tvTag.setGravity(Gravity.CENTER_VERTICAL);
        tvName.setTextColor(context.getResources().getColor(R.color.font_blue_play_gray));
        tvName.setTextSize(14);
        tvName.setLayoutParams(LP_Name);
        linearLayout.addView(tvName);

        TextView tvContent = new TextView(context);
        LinearLayout.LayoutParams LP_Content = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LP_Content.setMargins(8, 0, 0, 0);
        tvContent.setText(dmContent);
        tvContent.setTextColor(context.getResources().getColor(R.color.white));
        tvContent.setTextSize(16);
        tvContent.setLayoutParams(LP_Content);
        linearLayout.addView(tvContent);

        return linearLayout;
    }

    /**
     * 进入房间消息样式  格式：@用户名  进入/退出房间
     * @param context           上下文
     * @param uname             用户名
     * @param dmContent         聊天内容
     * @param headerImgUrl      用户头像
     * @return
     */
    public static LinearLayout addJoinOrOutRoomMsgScroll(Context context, String uname, String dmContent, String headimgUrl) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        //设置头像
        ImageView headImage=new ImageView(context);
        LinearLayout.LayoutParams LP_HEAD = new LinearLayout.LayoutParams(60,60);
        headImage.setLayoutParams(LP_HEAD);
        if (headimgUrl == null || headimgUrl.isEmpty()) {
            headImage.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(context)
                    .load(headimgUrl)
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(headImage);

        }
        linearLayout.addView(headImage);

        //设置TAG
        LinearLayout.LayoutParams LP_TAG = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LP_TAG.setMargins(10,0,0,0);
        TextView tvTag = new TextView(context);
        tvTag.setText("@");
        tvTag.setGravity(Gravity.CENTER_VERTICAL);
        tvTag.setTextColor(context.getResources().getColor(R.color.font_orange_light));
        tvTag.setTextSize(14);
        tvTag.setLayoutParams(LP_TAG);
        tvTag.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_top_black));
        linearLayout.addView(tvTag);

        //设置昵称
        TextView tvName = new TextView(context);
        LinearLayout.LayoutParams LP_Name = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvName.setText(uname);
        tvName.setGravity(Gravity.CENTER_VERTICAL);
        tvName.setTextColor(context.getResources().getColor(R.color.font_orange_light));
        tvName.setTextSize(14);
        tvName.setLayoutParams(LP_Name);
        tvName.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_top_black));
        linearLayout.addView(tvName);

        //设置消息内容
        TextView tvContent = new TextView(context);
        LinearLayout.LayoutParams LP_Content = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LP_Content.setMargins(8, 0, 0, 0);
        tvContent.setText(dmContent);
        tvContent.setTextColor(context.getResources().getColor(R.color.font_blue_join_room_word));
        tvContent.setTextSize(16);
        tvContent.setLayoutParams(LP_Content);
        tvContent.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_top_black));
        linearLayout.addView(tvContent);

        return linearLayout;
    }

    /**
     * 关注/取消关注主播消息样式  格式：@用户名 关注/取消关注了主播
     * @param context           上下文
     * @param uname             用户名
     * @param dmContent         聊天内容
     * @param headerImgUrl      用户头像
     * @return
     */
    public static LinearLayout focusHostMsgScroll(Context context, String uname, String dmContent, String headerImgUrl) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvTag = new TextView(context);
        LinearLayout.LayoutParams LP_TAG = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvTag.setText("@");
        tvTag.setGravity(Gravity.CENTER_VERTICAL);
        tvTag.setTextColor(context.getResources().getColor(R.color.font_blue_play_gray));
        tvTag.setTextSize(14);
        tvTag.setLayoutParams(LP_TAG);
        linearLayout.addView(tvTag);

        TextView tvName = new TextView(context);
        LinearLayout.LayoutParams LP_Name = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvName.setText(uname);
        tvTag.setGravity(Gravity.CENTER_VERTICAL);
        tvName.setTextColor(context.getResources().getColor(R.color.font_blue_play_gray));
        tvName.setTextSize(14);
        tvName.setLayoutParams(LP_Name);
        linearLayout.addView(tvName);

        TextView tvContent = new TextView(context);
        LinearLayout.LayoutParams LP_Content = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LP_Content.setMargins(8, 0, 0, 0);
        tvContent.setText(dmContent);
        tvContent.setTextColor(context.getResources().getColor(R.color.font_blue_join_room_word));
        tvContent.setTextSize(16);
        tvContent.setLayoutParams(LP_Content);
        linearLayout.addView(tvContent);

        return linearLayout;
    }

    /**
     * 链接消息服务器消息样式  格式：提示文字
     * @param context           上下文
     * @param dmContent         聊天内容
     * @return
     */
    public static LinearLayout connectMsgScroll(Context context, String dmContent) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvContent = new TextView(context);
        LinearLayout.LayoutParams LP_Content = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LP_Content.setMargins(8, 0, 0, 0);
        tvContent.setText(dmContent);
        tvContent.setTextColor(context.getResources().getColor(R.color.rect));
        tvContent.setTextSize(16);
        tvContent.setLayoutParams(LP_Content);
        linearLayout.addView(tvContent);

        return linearLayout;
    }
}
