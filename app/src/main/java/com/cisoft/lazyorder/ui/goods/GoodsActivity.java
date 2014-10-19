package com.cisoft.lazyorder.ui.goods;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.search.SearchActivity;
import com.cisoft.lazyorder.ui.shop.ShopActivity;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class GoodsActivity extends BaseActivity {

    @BindView(id=R.id.lv_goods)
    private ListView lvGoods;

    public GoodsActivity() {
        super();
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_goods);
        initActionBar();
    }

    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        //使应用程序能够在当前应用程序向上导航
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  返回首页");

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.goods, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        KJLoger.debug("searchView: " + searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ShopActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case R.id.action_search:
                showActivity(this, SearchActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
