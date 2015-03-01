package com.cisoft.lazyorder.ui.main.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

import java.util.List;

/**
 * Created by comet on 2014/10/16.
 */
public class DrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MenuFragmentManager.MenuItem> mData;

    public DrawerListAdapter(Context context, List<MenuFragmentManager.MenuItem> data){
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MenuFragmentManager.MenuItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_drawer_menu_list_cell, null);
            holder = new ViewHolder();
            holder.tvMenuTitle = (TextView) convertView.findViewById(R.id.menu_title);
            holder.ivMenuIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuFragmentManager.MenuItem menuItem = mData.get(position);
        holder.tvMenuTitle.setText(menuItem.title);
        holder.ivMenuIcon.setImageResource(menuItem.iconResId);

        return convertView;

    }

    public static class ViewHolder{
        public TextView tvMenuTitle;
        public ImageView ivMenuIcon;
    }
}
