package com.cisoft.lazyorder.ui.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.util.Utility;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.DensityUtils;
import java.util.List;

/**
 * Created by comet on 2014/10/16.
 */
public class ShopListAdapter extends BaseAdapter {

    private Context mContext;
    private KJBitmap mKjBitmap;
    private List<Shop> mData;

    public ShopListAdapter(Context context, List<Shop> data){
        mContext = context;
        mData = data;
        mKjBitmap = Utility.getKjBitmapInstance();
    }


    public void addData(List<Shop> addData){
        mData.addAll(addData);
    }

    public void clearAll(){
        mData.clear();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Shop getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_shop_list_cell, null);
            holder = new ViewHolder();
            holder.ivOpenStateBg = (ImageView) convertView.findViewById(R.id.iv_open_state_bg);
            holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tvOpenTime = (TextView) convertView.findViewById(R.id.tv_open_time);
            holder.tvMonthSales = (TextView) convertView.findViewById(R.id.tv_month_sales);
            holder.ivShopFaceImg = (ImageView) convertView.findViewById(R.id.iv_shop_face_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Shop shop = mData.get(position);
        if (shop.getOpenState() == 0) {
            holder.ivOpenStateBg.setImageResource(R.drawable.shop_close_state);
        } else {
            holder.ivOpenStateBg.setImageResource(R.drawable.shop_open_state);
        }
        holder.tvShopName.setText(shop.getName());
        holder.tvOpenTime.setText(shop.getOpenTime() + "-" + shop.getCloseTime());
        holder.tvMonthSales.setText(String.valueOf(shop.getMonthSales()));
        mKjBitmap.display(holder.ivShopFaceImg, shop.getFaceImgUrl(),
                R.drawable.icon_loading,
                DensityUtils.dip2px(mContext, 100), DensityUtils.dip2px(mContext, 100));

        return convertView;

    }

    public static class ViewHolder{
        public ImageView ivOpenStateBg;
        public TextView tvShopName;
        public TextView tvOpenTime;
        public TextView tvMonthSales;
        public ImageView ivShopFaceImg;
    }
}
