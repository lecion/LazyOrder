package com.cisoft.lazyorder.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.GoodsXCount;
import com.cisoft.lazyorder.bean.order.Order;
import com.cisoft.lazyorder.widget.OrderNumShowView;
import com.cisoft.lazyorder.widget.WrapLayout;

import java.util.List;

/**
 * Created by comet on 2014/11/12.
 */
public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private List<Order> data;

    public OrderListAdapter(Context context, List<Order> data){
        this.context = context;
        this.data = data;
    }


    public void addData(List<Order> addData){
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
    public Order getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_order_list_cell, null);
            holder = new ViewHolder();

            holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.onsvGoodsListContainer = (WrapLayout) convertView.findViewById(R.id.onsvGoodListContainer);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvOrderPrice = (TextView) convertView.findViewById(R.id.tv_order_price);
            holder.tvSettledPrice = (TextView) convertView.findViewById(R.id.tv_settled_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = data.get(position);
        holder.tvShopName.setText(order.getShopName());
        holder.onsvGoodsListContainer.removeAllViews();
        List<GoodsXCount> goodsList = order.getGoodsList();
        OrderNumShowView orderNumShowView = null;
        for (int i = 0; i < goodsList.size(); i++) {
            orderNumShowView = new OrderNumShowView(context, goodsList.get(i).getGoodsName(), goodsList.get(i).getCount());
            holder.onsvGoodsListContainer.addView(orderNumShowView);
        }

        holder.tvName.setText(order.getName());
        holder.tvPhone.setText(order.getPhone());
        holder.tvAddress.setText(order.getAddress());
        holder.tvOrderPrice.setText(String.valueOf(order.getOrderPrice()));
        holder.tvSettledPrice.setText(String.valueOf(order.getSettledPrice()));

        return convertView;

    }

    public static class ViewHolder{
        public TextView tvShopName;
        public WrapLayout onsvGoodsListContainer;
        public TextView tvName;
        public TextView tvPhone;
        public TextView tvAddress;
        public TextView tvOrderPrice;
        public TextView tvSettledPrice;
    }
}
