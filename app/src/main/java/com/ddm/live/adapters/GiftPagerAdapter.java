package com.ddm.live.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.ddm.live.R;
import com.ddm.live.models.bean.common.giftbeans.GiftData;
import com.ddm.live.ui.widget.GiftGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjx on 2017/2/21.
 */
public class GiftPagerAdapter extends PagerAdapter {
    private ArrayList<List<GiftData>> giftDatas;
    private Context context;
    private ArrayList<View> grids;
    private ArrayList<PlayGiftAdapter> adapters = new ArrayList<>();

    public GiftPagerAdapter (Context context, List<GiftData> giftDataList) {
        this.context = context;
        giftDatas = new ArrayList<>();
        grids = new ArrayList<>();
        giftDatas.add(giftDataList);
        giftDatas.add(giftDataList);
    }

    @Override
    public int getCount() {
        return giftDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int pos) {

        GiftGridView gridView = new GiftGridView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.addRule();
        gridView.setLayoutParams(lp);
        gridView.setNumColumns(4);
        gridView.setVerticalScrollBarEnabled(false);
//        View view = LayoutInflater.from(context).inflate(R.layout.item_gift_pager, null);
//        GridView gridView = (GridView) view.findViewById(R.id.play_present_layout);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        PlayGiftAdapter playGiftAdapter = new PlayGiftAdapter(context, giftDatas.get(pos));
        gridView.setAdapter(playGiftAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onGiftClickListener != null) {
//                    onGiftClickListener.onGiftClick(pos*4+position);
                    onGiftClickListener.onGiftClick(position);
                    int count = adapters.size();
                    for(int i=0; i<count; i++) {
                        if(i == pos) {
                            adapters.get(i).setIsSelected(position);
                        }
                        else {
                            adapters.get(i).setIsSelected(-1);
                        }
                        adapters.get(i).notifyDataSetChanged();
                    }
                }
            }
        });
        grids.add(gridView);
        adapters.add(playGiftAdapter);
        container.addView(gridView);
        return gridView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(grids.get(position));
    }

    public interface OnGiftClickListener {
        void onGiftClick(int position);
    }
    private OnGiftClickListener onGiftClickListener;
    public void setOnGiftClickListener (OnGiftClickListener onGiftClickListener) {
        this.onGiftClickListener = onGiftClickListener;
    }
}
