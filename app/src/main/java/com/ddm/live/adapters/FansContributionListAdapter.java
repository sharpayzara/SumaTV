package com.ddm.live.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.ddm.live.utils.DrawableSettingUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class FansContributionListAdapter extends BaseAdapter {
    private List<FansContributionInfoBean> list;
    private Context context;

    public FansContributionListAdapter(List<FansContributionInfoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FansContributionInfoBean infoBean = list.get(position);
        int type = infoBean.getType();
        String headimgUrl = infoBean.getAvatarSmall();

        Integer level = infoBean.getLevel();
               /*设置星级小图标*/
        int levelTag = (level + 16 - 1) / 16;
        String tagName = "resource_v_10" + levelTag;
        int tagId = DrawableSettingUtils.getDrawResourceID(tagName);

        //设置level图标
        String drawableName = "level_" + level;
        int drawResId = DrawableSettingUtils.getDrawResourceID(drawableName);

        TopThreeViewHolder topThreeViewHolder = null;
        NormalViewHolder normalViewHolder = null;

        if (convertView == null) {
            switch (type) {
                case 0:
                    topThreeViewHolder = new TopThreeViewHolder();
                    convertView = View.inflate(context, R.layout.fans_contribution_topthree_list_item, null);
                    topThreeViewHolder.boldRankNumber = (TextView) convertView.findViewById(R.id.rank_bold_number);
                    topThreeViewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    topThreeViewHolder.diamondsNumber = (TextView) convertView.findViewById(R.id.diamonds_number);
                    topThreeViewHolder.headImage = (ImageView) convertView.findViewById(R.id.headImage);
                    topThreeViewHolder.startTag = (ImageView) convertView.findViewById(R.id.user_info_level_tag);
                    topThreeViewHolder.gender = (ImageView) convertView.findViewById(R.id.gender);
                    topThreeViewHolder.level = (ImageView) convertView.findViewById(R.id.level);
                    convertView.setTag(R.id.viewHolderFansContributionTopThree, topThreeViewHolder);
                    break;
                case 1:
                    normalViewHolder = new NormalViewHolder();
                    convertView = View.inflate(context, R.layout.fans_contribution_normal_item, null);
                    normalViewHolder.rankNumber = (TextView) convertView.findViewById(R.id.rank_number);
                    normalViewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    normalViewHolder.diamondsNumber = (TextView) convertView.findViewById(R.id.diamonds_number);
                    normalViewHolder.headImage = (ImageView) convertView.findViewById(R.id.headImage);
                    normalViewHolder.startTag = (ImageView) convertView.findViewById(R.id.user_info_level_tag);
                    normalViewHolder.gender = (ImageView) convertView.findViewById(R.id.gender);
                    normalViewHolder.level = (ImageView) convertView.findViewById(R.id.level);
                    convertView.setTag(R.id.viewHolderFansContributionNormal, normalViewHolder);
                    break;
            }

        } else {
            switch (type) {
                case 0:
                    topThreeViewHolder = (TopThreeViewHolder) convertView.getTag(R.id.viewHolderFansContributionTopThree);
                    break;
                case 1:
                    normalViewHolder = (NormalViewHolder) convertView.getTag(R.id.viewHolderFansContributionNormal);
                    break;
            }

        }
        switch (type) {
            case 0:
                String boldRankNumber = "NO." + String.valueOf(position + 1);
                topThreeViewHolder.boldRankNumber.setText(boldRankNumber);
                topThreeViewHolder.name.setText(infoBean.getName());
                String totalDiamonds = String.valueOf((int) Float.parseFloat(infoBean.getTotalCoins()));
                topThreeViewHolder.diamondsNumber.setText(totalDiamonds);

                //设置头像
                if (headimgUrl == null || headimgUrl.isEmpty()) {
                    topThreeViewHolder.headImage.setImageResource(R.mipmap.user1);
                } else {
                    Picasso.with(context)
                            .load(headimgUrl)
                            .placeholder(R.mipmap.user1)
                            .error(R.mipmap.user1)
                            .into(topThreeViewHolder.headImage);
                }
                //设置星级图标
                topThreeViewHolder.startTag.setImageResource(tagId);
                //设置level图标
                topThreeViewHolder.level.setImageResource(drawResId);

                if (0==infoBean.getGender()) {
                    topThreeViewHolder.gender.setImageResource(R.mipmap.ic_profile_female);
                } else {
                    topThreeViewHolder.gender.setImageResource(R.mipmap.ic_profile_male);
                }

                break;
            case 1:
                String rankNumber = position + 1 + "";
                normalViewHolder.rankNumber.setText(rankNumber);
                normalViewHolder.name.setText(infoBean.getName());
                String totalDiamonds2 = String.valueOf((int) Float.parseFloat(infoBean.getTotalCoins()));
                normalViewHolder.diamondsNumber.setText(totalDiamonds2);

                //设置头像
                if (headimgUrl == null || headimgUrl.isEmpty()) {
                    normalViewHolder.headImage.setImageResource(R.mipmap.user1);
                } else {
                    Picasso.with(context)
                            .load(headimgUrl)
                            .placeholder(R.mipmap.user1)
                            .error(R.mipmap.user1)
                            .into(normalViewHolder.headImage);
                }

                //设置星级图标
                normalViewHolder.startTag.setImageResource(tagId);
                //设置level图标
                normalViewHolder.level.setImageResource(drawResId);

                if (0==infoBean.getGender()) {
                    normalViewHolder.gender.setImageResource(R.mipmap.ic_profile_female);
                } else {
                    normalViewHolder.gender.setImageResource(R.mipmap.ic_profile_male);
                }
                break;
        }

        return convertView;
    }

    class TopThreeViewHolder {
        TextView  boldRankNumber, name, diamondsNumber;
        ImageView headImage, gender, level, startTag;
    }

    class NormalViewHolder {
        TextView rankNumber,  name, diamondsNumber;
        ImageView headImage, gender, level, startTag;
    }
}
