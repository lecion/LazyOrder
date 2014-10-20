package com.cisoft.lazyorder.ui.shop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.Shop;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/10/16.
 */
public class ShopListViewAdapter extends BaseAdapter {

    private Context context;
    private KJBitmap kjb;
    private List<Shop> data;

    public ShopListViewAdapter(Context context, List<Shop> data, KJBitmap kjb){
        this.context = context;
        this.kjb = kjb;
        this.data = data;
    }


    public void addData(List<Shop> addData){
        data.addAll(addData);
    }

    public void clearAll(){
        data.clear();
    }

    public void refresh(){
        notifyDataSetChanged();
        notifyDataSetInvalidated();
        ((ShopActivity)context).setListViewHeightBasedOnChildren();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Shop getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_shop_list_cell, null);
            holder = new ViewHolder();
            holder.tvShopName = (TextView) convertView.findViewById(R.id.tvShopName);
            holder.tvOpenTime = (TextView) convertView.findViewById(R.id.tvOpenTime);
            holder.tvMonthSales = (TextView) convertView.findViewById(R.id.tvMonthSales);
            holder.ivShopFaceImg = (ImageView) convertView.findViewById(R.id.ivShopFaceImg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Shop shop = data.get(position);
        holder.tvShopName.setText(shop.getName());
        holder.tvOpenTime.setText(shop.getOpenTime() + "-" + shop.getCloseTime());
        holder.tvMonthSales.setText(String.valueOf(shop.getMonthSales()));
        kjb.display(holder.ivShopFaceImg, shop.getFaceImgUrl());
        if (shop.getOpenState() == 0) {
            convertView.setBackgroundResource(R.drawable.shop_bg_open);
        } else {
            convertView.setBackgroundResource(R.drawable.shop_bg_close);
        }
        convertView.setPadding(0, 0, 0, 0);

        return convertView;
    }

    public static class ViewHolder{
        public TextView tvShopName;
        public TextView tvOpenTime;
        public TextView tvMonthSales;
        public ImageView ivShopFaceImg;
    }
}
