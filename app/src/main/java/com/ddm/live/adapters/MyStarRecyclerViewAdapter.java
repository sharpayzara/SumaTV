package com.ddm.live.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.models.bean.account.StarDiamondExchangeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ytzys on 2017/2/22.
 */
public class MyStarRecyclerViewAdapter extends RecyclerView.Adapter {

    List<StarDiamondExchangeBean> list = new ArrayList<StarDiamondExchangeBean>();
    Context context;

    public MyStarRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<StarDiamondExchangeBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recharge_list_item, null, false); // 和钻石购买页面中的recyclerView使用同样的布局文件
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.diamonds.setText(list.get(position).getDiamonds());
        viewHolder.stars.setText(list.get(position).getStars());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recharge_list_diamonds_num)
        TextView diamonds;

        @BindView(R.id.recharge_list_money_num)
        TextView stars;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
