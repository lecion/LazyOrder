package com.cisoft.lazyorder.ui.shop;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.ShopCategory;

public class ShopCategoryListAdapter extends BaseAdapter{

	private Context context;
    private List<ShopCategory> data;

    public ShopCategoryListAdapter(Context context, List<ShopCategory> data){
        this.context = context;
        this.data = data;
    }
	
    public void addData(List<ShopCategory> addData){
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
	public ShopCategory getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_shop_category_list_cell, null);
        }
        ((TextView)convertView.findViewById(R.id.tvShopCategoryName)).setText(data.get(position).getName());
        
        return convertView;
	}

}
