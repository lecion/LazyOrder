package com.cisoft.lazyorder.ui.sureorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.widget.OrderNumView;

import org.kymjs.aframe.ui.ViewInject;

import java.util.List;

/**
 * Created by comet on 2014/11/5.
 */
public class OrderListAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private List<Goods> data;

    public OrderListAdapter(Context context, List<Goods> data) {
        this.context = context;
        this.data = data;
    }


    public void addData(List<Goods> addData) {
        data.addAll(addData);
    }

    public void clear() {
        data.clear();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Goods getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_sure_order_list_cell, null);
            viewHolder = new ViewHolder();
            viewHolder.tvGoodName = (TextView) convertView.findViewById(R.id.tvGoodName);
            viewHolder.onvGoodOrderNum = (OrderNumView) convertView.findViewById(R.id.onvGoodOrderNum);
            viewHolder.tvGoodsTotalPrice = (TextView) convertView.findViewById(R.id.tvGoodsTotalPrice);
            viewHolder.ibDeleteOrder = (ImageButton) convertView.findViewById(R.id.ibDeleteOrder);
            viewHolder.ibDeleteOrder.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Goods good = data.get(position);
        viewHolder.goodId = good.getId();
        viewHolder.tvGoodName.setText(good.getCmName());
        viewHolder.onvGoodOrderNum.setNum(good.getOrderNum());
        viewHolder.tvGoodsTotalPrice.setText(String.valueOf(good.getOrderNum() * good.getCmPrice()));

        return convertView;
    }

    @Override
    public void onClick(View view) {
        View parentView = (View) view.getParent();
        ViewHolder viewHolder = (ViewHolder) parentView.getTag();
        int goodId = viewHolder.goodId;
        GoodsCart.getInstance().delGoods(goodId);
        this.data = GoodsCart.getInstance().getAllGoods();
        this.refresh();
    }


    public class ViewHolder{
        public int goodId;
        public TextView tvGoodName;
        public OrderNumView onvGoodOrderNum;
        public TextView tvGoodsTotalPrice;
        public ImageButton ibDeleteOrder;
    }
}
