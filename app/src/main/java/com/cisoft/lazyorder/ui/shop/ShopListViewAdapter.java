package com.cisoft.lazyorder.ui.shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.Shop;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.utils.DensityUtils;
import java.util.List;

/**
 * Created by comet on 2014/10/16.
 */
public class ShopListViewAdapter extends BaseAdapter {

    private Context context;
    private KJBitmap kjb;
    private List<Shop> data;
    private Bitmap loadingBitmap;

    public ShopListViewAdapter(Context context, List<Shop> data){
        this.context = context;
        this.data = data;
        this.kjb = KJBitmap.create();
        this.loadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
    }


    public void addData(List<Shop> addData){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_shop_list_cell, null);
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
        kjb.display(holder.ivShopFaceImg, shop.getFaceImgUrl(), loadingBitmap,
                DensityUtils.dip2px(context, 100), DensityUtils.dip2px(context, 100));
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
