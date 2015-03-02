package com.cisoft.shop.order.view;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cisoft.shop.R;
import com.cisoft.shop.bean.Order;
import com.cisoft.shop.bean.OrderGoods;
import com.cisoft.shop.util.DeviceUtil;

import java.util.List;

/**
 * Created by Lecion on 2/28/15.
 */
public class OrderDetailDialog extends DialogFragment {
    private ListView lvOrderGoods;
    private TextView tvOrderNumber;
    private TextView tvDistribution;
    private TextView tvPrice;
    private TextView tvContent;
    private TextView tvUserName;
    private TextView tvUserPhone;
    private TextView tvAddress;


    private Order order;

    public OrderDetailDialog(Order order) {
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_fragment_order_detail, container, false);
        initWidget(v);
        initData();
        return v;
    }

    private void initData() {
        lvOrderGoods.setAdapter(new OrderGoodsAdapter(order.getGoodsList()));
        tvOrderNumber.setText("No." + order.getOrderNumber());
        tvDistribution.setText("￥" + order.getDistributionPrice());
        tvPrice.setText("￥" + order.getOrderPrice());
        tvContent.setText(order.getContent());
        tvUserName.setText(order.getUserName());
        tvUserPhone.setText(order.getUserPhone());
        tvAddress.setText(order.getAddress());
    }

    private void initWidget(View parent) {
        lvOrderGoods = (ListView) parent.findViewById(R.id.lv_order_goods);
        tvOrderNumber = (TextView) parent.findViewById(R.id.tv_order_number);
        tvDistribution = (TextView) parent.findViewById(R.id.tv_distribution_price_show);
        tvPrice = (TextView) parent.findViewById(R.id.tv_order_price_show);
        tvContent = (TextView) parent.findViewById(R.id.tv_content_show);
        tvUserName = (TextView) parent.findViewById(R.id.tv_user_name_show);
        tvUserPhone = (TextView) parent.findViewById(R.id.tv_user_phone_show);
        tvAddress = (TextView) parent.findViewById(R.id.tv_address_show);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(DeviceUtil.getScreenWidth(getActivity()) / 10 * 9, window.getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private class OrderGoodsAdapter extends BaseAdapter {
        private List<OrderGoods> orderGoodsList;

        private OrderGoodsAdapter(List<OrderGoods> orderGoodsList) {
            this.orderGoodsList = orderGoodsList;
        }

        @Override
        public int getCount() {
            return orderGoodsList.size();
        }

        @Override
        public Object getItem(int position) {
            return orderGoodsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return orderGoodsList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_order_detial_cell, parent, false);
                holder.tvOrderGoodsName = (TextView) convertView.findViewById(R.id.tv_order_goods_name);
                holder.tvOrderGoodsPrice = (TextView) convertView.findViewById(R.id.tv_order_goods_price);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            OrderGoods goods = (OrderGoods) getItem(position);
            holder.tvOrderGoodsName.setText(goods.getComName() + "×" + goods.getComNum());
            holder.tvOrderGoodsPrice.setText("￥" + goods.getPrice());
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView tvOrderGoodsName;
        TextView tvOrderGoodsPrice;
    }
}
