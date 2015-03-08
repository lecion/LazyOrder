package com.cisoft.lazyorder.ui.express;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.express.Express;

import java.util.List;

/**
 * Created by comet on 2014/11/12.
 */
public class ExpressListAdapter extends BaseAdapter {
    private Context context;
    private List<Express> data;

    public ExpressListAdapter(Context context, List<Express> data){
        this.context = context;
        this.data = data;
    }

    public void addData(List<Express> addData){
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
    public Express getItem(int position) {
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
            holder.tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.tvContactPhone = (TextView) convertView.findViewById(R.id.tv_contact_phone);
            holder.tvContactAddress = (TextView) convertView.findViewById(R.id.tv_contact_address);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Express express = data.get(position);
        holder.tvSubmitTime.setText(express.getSubmitTime());
        holder.tvSMSCotent.setText(express.getSmsCotent());
        holder.tvExtraMsg.setText(express.getExtraMsg());
        holder.tvDeliveryMoney.setText(String.valueOf(express.getShippingFee()));
        holder.tvContactName.setText(express.getUserName());
        holder.tvContactPhone.setText(express.getUserPhone());
        holder.tvContactAddress.setText(express.getAddress());

        return convertView;
    }

    public static class ViewHolder{
        public TextView tvSubmitTime;
        public TextView tvSMSCotent;
        public TextView tvExtraMsg;
        public TextView tvDeliveryMoney;
        public TextView tvContactName;
        public TextView tvContactPhone;
        public TextView tvContactAddress;
    }
}
