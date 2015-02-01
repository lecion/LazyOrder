package com.cisoft.lazyorder.ui.express;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.ExpressOrder;

import java.util.List;

/**
 * Created by comet on 2014/11/12.
 */
public class ExpressOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<ExpressOrder> data;

    public ExpressOrderListAdapter(Context context, List<ExpressOrder> data){
        this.context = context;
        this.data = data;
    }

    public void addData(List<ExpressOrder> addData){
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
    public ExpressOrder getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_express_list_cell, null);
            holder = new ViewHolder();

            holder.tvSubmitTime = (TextView) convertView.findViewById(R.id.tv_submit_time);
            holder.tvSMSCotent = (TextView) convertView.findViewById(R.id.tv_sms_content);
            holder.tvExtraMsg = (TextView) convertView.findViewById(R.id.tv_extra_message);
            holder.tvDeliveryMoney = (TextView) convertView.findViewById(R.id.tv_delivery_money);
            holder.tvContactAddress = (TextView) convertView.findViewById(R.id.tv_contact_address);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpressOrder expressOrder = data.get(position);
        holder.tvSubmitTime.setText(expressOrder.getSubmitTime());
        holder.tvSMSCotent.setText(expressOrder.getSmsCotent());
        holder.tvExtraMsg.setText(expressOrder.getExtraMsg());
        holder.tvDeliveryMoney.setText(String.valueOf(expressOrder.getDeliveryMoney()));
        holder.tvContactAddress.setText(expressOrder.getAddress());

        return convertView;
    }

    public static class ViewHolder{
        public TextView tvSubmitTime;
        public TextView tvSMSCotent;
        public TextView tvExtraMsg;
        public TextView tvDeliveryMoney;
        public TextView tvContactAddress;
    }
}
