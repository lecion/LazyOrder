package com.cisoft.lazyorder.ui.express;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.express.SmsInfo;

import java.util.List;

/**
 * Created by comet on 2014/11/12.
 */
public class ChoiceSmsListAdapter extends BaseAdapter {
    private Context context;
    private List<SmsInfo> data;

    public ChoiceSmsListAdapter(Context context, List<SmsInfo> data){
        this.context = context;
        this.data = data;
    }

    public void addData(List<SmsInfo> addData){
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
    public SmsInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_choice_sms_list_cell, null);
            holder = new ViewHolder();

            holder.tvSmsAddress = (TextView) convertView.findViewById(R.id.tv_sms_address);
            holder.tvSmsPerson = (TextView) convertView.findViewById(R.id.tv_sms_person);
            holder.tvSmsBody = (TextView) convertView.findViewById(R.id.tv_sms_body);
            holder.tvSmsDate = (TextView) convertView.findViewById(R.id.tv_sms_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SmsInfo smsInfo = data.get(position);
        holder.tvSmsAddress.setText(smsInfo.getAddress());
        holder.tvSmsPerson.setText(smsInfo.getPerson());
        holder.tvSmsBody.setText(smsInfo.getSmsbody());
        holder.tvSmsDate.setText(smsInfo.getDate());

        return convertView;
    }

    public static class ViewHolder{
        public TextView tvSmsAddress;
        public TextView tvSmsPerson;
        public TextView tvSmsBody;
        public TextView tvSmsDate;
    }
}
