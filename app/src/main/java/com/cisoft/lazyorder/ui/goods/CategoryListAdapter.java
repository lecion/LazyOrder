package com.cisoft.lazyorder.ui.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.GoodsCategory;
import java.util.List;

/**
 * Created by comet on 2015/2/26.
 */
public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsCategory> data;

    public CategoryListAdapter(Context context, List<GoodsCategory> data){
        this.context = context;
        this.data = data;
    }

    public void addData(List<GoodsCategory> addData){
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
    public GoodsCategory getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_goods_category_list_cell, null);
        }
        TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tv_category_name);
        GoodsCategory category = data.get(position);
        tvCategoryName.setText(category.getCateName());
//        KJLoger.debug("getView " + category.getCateName());
        return convertView;
    }

}
