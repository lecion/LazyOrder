package com.cisoft.shop;

import android.widget.ArrayAdapter;

import com.cisoft.myapplication.R;
import com.cisoft.shop.widget.RefreshSwipeDeleteListView;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

/**
 * Created by Lecion on 2/22/15.
 */
public class TestActivity extends BaseActivity {
    String[] data = {"a", "B", "c", "d", "e", "f"};
    @BindView(id = R.id.rdlv)
    RefreshSwipeDeleteListView listView;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
    }
}
