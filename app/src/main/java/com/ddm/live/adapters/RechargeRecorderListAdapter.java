package com.ddm.live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.models.bean.account.QueryChargeInfoListResponse;

/**
 * Created by cxx on 2016/9/18.
 */
public class RechargeRecorderListAdapter extends BaseAdapter {
    QueryChargeInfoListResponse response;
    Context context;

    public RechargeRecorderListAdapter(QueryChargeInfoListResponse response, Context context) {
        this.response = response;
        this.context = context;
    }

    @Override
    public int getCount() {
        return response.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return response.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rechargelist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.rechargeMoneyText = (TextView) convertView.findViewById(R.id.recharge_money2);
            viewHolder.rechargeTimeText = (TextView) convertView.findViewById(R.id.recharge_time);
            viewHolder.rechargeLebiText = (TextView) convertView.findViewById(R.id.recharge_lebi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.rechargeMoneyText.setText("购买:"+response.getData().get(position).getPriceInfo()+"元");
        viewHolder.rechargeTimeText.setText(response.getData().get(position).getCreatedAt());
        viewHolder.rechargeLebiText.setText("+"+response.getData().get(position).getAmt()+"乐币");
        return convertView;
    }

    class ViewHolder {
        TextView rechargeMoneyText;
        TextView rechargeTimeText;
        TextView rechargeLebiText;
    }

}
