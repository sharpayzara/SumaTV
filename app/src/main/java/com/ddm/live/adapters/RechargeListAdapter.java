package com.ddm.live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.presenter.AccountPresenter;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RechargeListAdapter extends BaseAdapter {
    List<String> money;
    List<String> lebi;
    Context context;
    AccountPresenter presenter;

    public RechargeListAdapter(Context context, List<String> money, List<String> lebi, AccountPresenter presenter) {
        this.context = context;
        this.money = money;
        this.lebi = lebi;
        this.presenter = presenter;
    }

    @Override
    public int getCount() {
        return money.size();
    }

    @Override
    public Object getItem(int position) {
        return money.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recharge_list_item, null);
            holder = new ViewHolder();
            holder.lebiText = (TextView) convertView.findViewById(R.id.recharge_diamonds_num);
            holder.moneyText = (TextView) convertView.findViewById(R.id.recharge_list_money_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lebiText.setText(lebi.get(position));
        holder.moneyText.setText(money.get(position));
        final Integer buyMoney = Integer.parseInt(lebi.get(position)) / 10;
        holder.moneyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((RechargeActivity) context).showProgressBar();
                presenter.buyVirtualCoins(buyMoney);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView lebiText;
        TextView moneyText;
        ImageView lebiImage;
    }
}
