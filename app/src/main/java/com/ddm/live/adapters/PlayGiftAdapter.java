package com.ddm.live.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.models.bean.common.giftbeans.GiftData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by JiGar on 2016/9/14.
 */
public class PlayGiftAdapter extends BaseAdapter {

    public List<HashMap<String, Object>> FiltArray;
    public List<GiftData> giftDataList;
    private LayoutInflater inflater;
    private int currFocusId = -1;
    private Context mContext;

    /*int[] giftImgArray = new int[]{
            R.mipmap.blue_giftsheet_bg_1, R.mipmap.blue_giftsheet_bg_2,
            R.mipmap.blue_giftsheet_bg_3, R.mipmap.blue_giftsheet_bg_4,
            R.mipmap.blue_giftsheet_bg_5, R.mipmap.blue_giftsheet_bg_6,
            R.mipmap.blue_giftsheet_bg_7, R.mipmap.blue_giftsheet_bg_8,
    };

    String[] giftExpArray = new String[]{
            "6", "16", "30", "60", "120", "180", "360", "600"
    };

    String[] giftMoneyArray = new String[]{
            "6", "16", "30", "60", "120", "180", "360", "600"
    };*/

    public PlayGiftAdapter(Context context, List<GiftData> giftDataList) {
        super();
        mContext=context;
        inflater = LayoutInflater.from(context);
       this.giftDataList=giftDataList;
    }

    @Override
    public int getCount() {
        return giftDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return giftDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GiftHolder tmp;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.play_present_grid_item, null);
            TextView giftExp = (TextView) convertView.findViewById(R.id.play_gift_experience);
            ImageView giftImage = (ImageView) convertView.findViewById(R.id.play_gift_img);
            TextView giftMoney = (TextView) convertView.findViewById(R.id.play_gift_money);
            ImageView giftSelected = (ImageView) convertView.findViewById(R.id.play_gift_checked);

            tmp = new GiftHolder();
            tmp.giftExperience = giftExp;
            tmp.giftImage = giftImage;
            tmp.giftMoney = giftMoney;
            tmp.giftSelected = giftSelected;
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, 306
//            );
//            convertView.setLayoutParams(params);
            convertView.setTag(tmp);

        } else {
            tmp = (GiftHolder) convertView.getTag();
        }

        tmp.giftExperience.setText(giftDataList.get(position).getToExp());
//        tmp.giftImage.setImageResource(giftDataList.get(position).get);
        Picasso.with(mContext)
                .load(giftDataList.get(position).getPicUrl())
//                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .error(R.mipmap.user1)
                .placeholder(R.mipmap.user1)
                .into(tmp.giftImage);
        tmp.giftMoney.setText(giftDataList.get(position).getToGold());
//        if (currFocusId == position)
//            tmp.giftSelected.setVisibility(View.VISIBLE);
//        else
//            tmp.giftSelected.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public class GiftHolder {
        TextView giftExperience;
        ImageView giftImage;
        TextView giftMoney;
        ImageView giftSelected;
    }

    public void setIsSelected(int pos) {
        currFocusId = pos;
    }
}
