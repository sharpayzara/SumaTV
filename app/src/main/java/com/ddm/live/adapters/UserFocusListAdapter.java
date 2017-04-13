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
import com.ddm.live.models.bean.common.fansbeans.MasterBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JiGar on 2016/10/12.
 */
public class UserFocusListAdapter extends BaseAdapter {

    private Context context;
    private List<MasterBean> userInfo;

    public UserFocusListAdapter(Context context, List<MasterBean> userInfo) {
        this.context = context;
        this.userInfo = userInfo;
    }

    @Override
    public int getCount() {
        return userInfo.size();
    }

    @Override
    public MasterBean getItem(int position) {
        return userInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userInfo.get(position).getMaster().getData().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_focus_list_item, null);
            holder = new ViewHolder();
            holder.headImg = (ImageView) convertView.findViewById(R.id.user_focus_list_img);
            holder.userName = (TextView) convertView.findViewById(R.id.user_focus_list_name);
            holder.userContent = (TextView) convertView.findViewById(R.id.user_focus_list_content);
            holder.userLevTag = (ImageView) convertView.findViewById(R.id.user_focus_list_user_level_tag);
            holder.userSex = (ImageView) convertView.findViewById(R.id.user_focus_list_sex);
            holder.userLevel = (ImageView) convertView.findViewById(R.id.user_focus_list_level);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.userName.setText(userInfo.get(position).getMaster().getData().getName());
        if (userInfo.get(position).getMaster().getData().getSign().isEmpty()) {
            holder.userContent.setText(R.string.user_introduction);
        } else {
            holder.userContent.setText(userInfo.get(position).getMaster().getData().getSign());
        }
        if (userInfo.get(position).getMaster().getData().getAvatarSmall().isEmpty()) {
            holder.headImg.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(context)
//                    .load(userInfo.get(position).getMaster().getData().getAvatar()+"?imageView2/1/w/100/h/100")
                    .load(userInfo.get(position).getMaster().getData().getAvatarSmall())
                    .placeholder(R.mipmap.user1)
//                    .config(Bitmap.Config.RGB_565)
//                    .resize(DensityUtils.dip2px(context,60), DensityUtils.dip2px(context,60))
//                    .centerCrop()
                    .error(R.mipmap.user1)
                    .into(holder.headImg);
        }

        if (0==userInfo.get(position).getMaster().getData().getGender()) {
            holder.userSex.setImageResource(R.mipmap.ic_profile_female);
        } else {
            holder.userSex.setImageResource(R.mipmap.ic_profile_male);
        }

        Integer level = userInfo.get(position).getMaster().getData().getLevel();
        String drawableName = "level_" + level;
        int drawResId = getDrawResourceID(drawableName);
        holder.userLevel.setImageResource(drawResId);

        int levelTag = (level + 16 - 1) / 16;
        String levelTagStr;
        if (levelTag < 10) {
            levelTagStr  = "0" + String.valueOf(levelTag);
        } else {
            levelTagStr = String.valueOf(levelTag);
        }
        String tagName = "resource_v_1" + levelTagStr;
        int tagId = getDrawResourceID(tagName);
        holder.userLevTag.setImageResource(tagId);

        return convertView;
    }

    private class ViewHolder {
        ImageView headImg;
        TextView userName;
        TextView userContent;
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
