package com.cisoft.lazyorder.ui.sureinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.sureinfo.Build;
import com.cisoft.lazyorder.bean.sureinfo.BuildChoiceCounter;

import java.util.List;

/**
 * Created by comet on 2014/11/4.
 */
public class BuildListAdapter extends BaseAdapter{
    private Context context;
    private List<Build> data;
    private BuildChoiceCounter buildChoiceCounter;

    public BuildListAdapter(Context context, List<Build> data, BuildChoiceCounter buildChoiceCounter){
        this.context = context;
        this.data = data;
        this.buildChoiceCounter = buildChoiceCounter;
    }


    public void addData(List<Build> addData){
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
    public Build getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_sure_info_build_list_cell, null);
            holder = new ViewHolder();
            holder.ctvBuildName = (CheckedTextView) convertView.findViewById(R.id.ctvBuildName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ctvBuildName.setText(data.get(position).getName());
        if(position == buildChoiceCounter.getBuildChoicePos()){
            holder.ctvBuildName.setChecked(true);
        }else{
            holder.ctvBuildName.setChecked(false);
        }

        return convertView;
    }

    public static class ViewHolder{
        public CheckedTextView ctvBuildName;
    }
}
