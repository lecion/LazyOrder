package com.cisoft.lazyorder.ui.goods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.cisoft.lazyorder.R;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class GoodsActivity extends BaseActivity {

    @BindView(id=R.id.lv_goods)
    private ListView lvGoods;



    @Override
    public void setRootView() {
        setContentView(R.layout.activity_goods);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        lvGoods.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 18;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return LayoutInflater.from(GoodsActivity.this).inflate(R.layout.goods_list_cell, null);
            }
        });
    }
}
