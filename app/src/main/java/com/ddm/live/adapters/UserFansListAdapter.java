package com.ddm.live.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.models.bean.common.fansbeans.FansBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JiGar on 2016/10/11.
 */
public class UserFansListAdapter extends BaseAdapter {

    private Context context;
    private List<FansBean> userInfo;

    public UserFansListAdapter(Context context, List<FansBean> userInfo) {
        this.context = context;
        this.userInfo = userInfo;
    }

    @Override
    public int getCount() {
        return userInfo.size();
    }

    @Override
    public FansBean getItem(int position) {
        return userInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userInfo.get(position).getFansId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_fans_list_item, null);
            holder = new ViewHolder();
            holder.headImg = (ImageView) convertView.findViewById(R.id.user_fans_list_img);
            holder.userName = (TextView) convertView.findViewById(R.id.user_fans_list_name);
            holder.userContent = (TextView) convertView.findViewById(R.id.user_fans_list_content);
            holder.addImg = (ImageView) convertView.findViewById(R.id.user_fans_list_add_img);
            holder.addTxt = (TextView) convertView.findViewById(R.id.user_fans_list_add_txt);
            holder.userLevTag = (ImageView) convertView.findViewById(R.id.user_fans_list_user_level_tag);
            holder.userSex = (ImageView) convertView.findViewById(R.id.user_fans_list_sex);
            holder.userLevel = (ImageView) convertView.findViewById(R.id.user_fans_list_level);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.userName.setText(userInfo.get(position).getFans().getData().getName());
        if (userInfo.get(position).getFans().getData().getSign().isEmpty()) {
            holder.userContent.setText(R.string.user_introduction);
        } else {
            holder.userContent.setText(userInfo.get(position).getFans().getData().getSign());
        }
        if (userInfo.get(position).getFans().getData().getAvatarSmall().isEmpty()) {
            holder.headImg.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(context)
                    .load(userInfo.get(position).getFans().getData().getAvatarSmall())
                    .placeholder(R.mipmap.user1)
//                    .config(Bitmap.Config.RGB_565)
//                    .resize(DensityUtils.dip2px(context, 60), DensityUtils.dip2px(context, 60))
//                    .centerCrop()
                    .error(R.mipmap.user1)
                    .into(holder.headImg);
        }

        if (0==userInfo.get(position).getFans().getData().getGender()) {
            holder.userSex.setImageResource(R.mipmap.ic_profile_female);
        } else {
            holder.userSex.setImageResource(R.mipmap.ic_profile_male);
        }

        Integer level = userInfo.get(position).getFans().getData().getLevel();
        String drawableName = "level_" + level;
        int drawResId = getDrawResourceID(drawableName);
        holder.userLevel.setImageResource(drawResId);

        int levelTag = (level + 16 - 1) / 16;
        String levelTagStr;
        if (levelTag < 10) {
            levelTagStr = "0" + String.valueOf(levelTag);
        } else {
            levelTagStr = String.valueOf(levelTag);
        }
        String tagName = "resource_v_1" + levelTagStr;
        int tagId = getDrawResourceID(tagName);
        holder.userLevTag.setImageResource(tagId);

        if (userInfo.get(position).getFans().getData().getFans().getData().getIsFollowed()) {
//            holder.addImg.setImageResource(R.mipmap.user_fans_item_added);
//            holder.addImg.setVisibility(View.VISIBLE);
            holder.addTxt.setVisibility(View.GONE);
        } else {
            /*holder.addImg.setImageResource(R.mipmap.user_fans_item_add);
            holder.addImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "addImg", Toast.LENGTH_SHORT).show();
                }
            });*/
//            holder.addImg.setVisibility(View.VISIBLE);
            holder.addTxt.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView headImg;
        TextView userName;
        TextView userContent;
        ImageView addImg;
        TextView addTxt;
        ImageView userSex;
        ImageView userLevel;
        ImageView userLevTag;
    }

    /**
     * 根据图片的名称获取对应的资源id
     *
     * @param resourceName
     * @return
     */
    public int getDrawResourceID(String resourceName) {
        Resources res = context.getResources();
        int picid = res.getIdentifier(resourceName, "mipmap", context.getPackageName());
        return picid;
    }
}
