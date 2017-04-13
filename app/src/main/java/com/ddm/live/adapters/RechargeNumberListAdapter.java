package com.ddm.live.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.models.bean.account.GetRechargeListResponseData;
import com.ddm.live.presenter.AccountPresenter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;


public class RechargeNumberListAdapter extends UltimateViewAdapter<RechargeNumberListAdapter.SimpleAdapterViewHolder> { // implements IAccountView
    private String TAG = "SimpleAdapter";

    private List<GetRechargeListResponseData> listItems;
    private AccountPresenter presenter;
    private Context context;

    public Context getContext() {
        return context;
    }
    public void notifyDataChanged(List<GetRechargeListResponseData> listItems){
        this.listItems=listItems;
        notifyDataSetChanged();
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public List<GetRechargeListResponseData> getListItems() {
        return listItems;
    }

    public void setListItems(List<GetRechargeListResponseData> listItems) {
        this.listItems = listItems;
    }

    public RechargeNumberListAdapter(List<GetRechargeListResponseData> listItems, Context context, AccountPresenter presenter) {
        this.listItems = listItems;
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, final int position) {

        if (position < getItemCount() && (customHeaderView != null ? position <= listItems.size() : position < listItems.size()) && (customHeaderView != null ? position > 0 : true)) {

            GetRechargeListResponseData item = listItems.get(customHeaderView != null ? position - 1 : position);

            holder.diamondText.setText(String.valueOf(item.getDiamonds()));
            holder.moneyText.setText(item.getMoney());
            final Integer buyMoney = item.getDiamonds() / 10;
            holder.moneyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((RechargeActivity) context).showProgressBar();
                    presenter.buyVirtualCoins(buyMoney);
                }
            });

            if (mDragStartListener != null) {

            }
        }

    }

    @Override
    public int getAdapterItemCount() {
        return listItems.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recharge_list_item, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }


    public void insert(GetRechargeListResponseData item, int position) {
        insert(listItems, item, position);
    }

    public void remove(int position) {
        remove(listItems, position);
    }

    public void clear() {
        clear(listItems);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }

    public void swapPositions(int from, int to) {
        swapPositions(listItems, from, to);
    }

    @Override
    public long generateHeaderId(int position) {
        // URLogs.d("position--" + position + "   " + getItem(position));
        /*if (getItem(position).length() > 0)
            return_back getItem(position).charAt(0);
        else return_back -1;*/
        //修改的
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);
        super.onItemDismiss(position);
    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }

    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        public View itemView;

        TextView diamondText;
        TextView moneyText;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            this.itemView = itemView;
            if (isItem) {
                diamondText = (TextView) itemView.findViewById(R.id.recharge_list_diamonds_num);
                moneyText = (TextView) itemView.findViewById(R.id.recharge_list_money_num);
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public GetRechargeListResponseData getItem(int position) {
        if (customHeaderView != null) {
            position--;
        }

        if (position < listItems.size()) {
            return listItems.get(position);
        } else {
            //需要修改
            return null;
        }
    }

}