package com.cisoft.lazyorder.ui.orderlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.orderlist.HistoryOrder;
import com.cisoft.lazyorder.widget.OrderNumShowView;
import com.cisoft.lazyorder.widget.WrapLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by comet on 2014/11/12.
 */
public class HistoryOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryOrder> data;

    public HistoryOrderListAdapter(Context context, List<HistoryOrder> data){
        this.context = context;
        this.data = data;
    }


    public void addData(List<HistoryOrder> addData){
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
    public HistoryOrder getItem(int position) {
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

            holder.tvShopName = (TextView) convertView.findViewById(R.id.tvShopName);
            holder.onsvGoodListContainer = (WrapLayout) convertView.findViewById(R.id.onsvGoodListContainer);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoryOrder historyOrder = data.get(position);
        holder.tvShopName.setText(historyOrder.getShopName());

        List<Map<String, String>> goodListMap =  historyOrder.getGoodList();
        OrderNumShowView orderNumShowView = null;
        for (int i = 0; i < goodListMap.size(); i++) {
            orderNumShowView = new OrderNumShowView(context, goodListMap.get(i));
            holder.onsvGoodListContainer.addView(orderNumShowView);
        }

        holder.tvAddress.setText(historyOrder.getAddress());
        holder.tvTotalPrice.setText(String.valueOf(historyOrder.getTotalPrice()));

        return convertView;

    }

    public static class ViewHolder{
        public TextView tvShopName;
        public WrapLayout onsvGoodListContainer;
        public TextView tvAddress;
        public TextView tvTotalPrice;
    }
}
