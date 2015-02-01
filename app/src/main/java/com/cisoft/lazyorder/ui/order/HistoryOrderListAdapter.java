package com.cisoft.lazyorder.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.widget.OrderNumShowView;
import com.cisoft.lazyorder.widget.WrapLayout;

import java.util.List;

/**
 * Created by comet on 2014/11/12.
 */
public class HistoryOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<DishOrder> data;

    public HistoryOrderListAdapter(Context context, List<DishOrder> data){
        this.context = context;
        this.data = data;
    }


    public void addData(List<DishOrder> addData){
        data.addAll(addData);
    }

    public void clearAll(){
        data.clear();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DishOrder getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_history_order_list_cell, null);
            holder = new ViewHolder();

            holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.onsvGoodsListContainer = (WrapLayout) convertView.findViewById(R.id.onsvGoodListContainer);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DishOrder dishOrder = data.get(position);
        holder.tvShopName.setText(dishOrder.getShopName());
        holder.onsvGoodsListContainer.removeAllViews();
        List<Goods> goodsList = dishOrder.getGoodsList();
        OrderNumShowView orderNumShowView = null;
        for (int i = 0; i < goodsList.size(); i++) {
            orderNumShowView = new OrderNumShowView(context, goodsList.get(i).getCmName(), goodsList.get(i).getOrderNum());
            holder.onsvGoodsListContainer.addView(orderNumShowView);
        }

        holder.tvAddress.setText(dishOrder.getAddress());
        holder.tvTotalPrice.setText(String.valueOf(dishOrder.getMoneyAll()));

        return convertView;

    }

    public static class ViewHolder{
        public TextView tvShopName;
        public WrapLayout onsvGoodsListContainer;
        public TextView tvAddress;
        public TextView tvTotalPrice;
    }
}
