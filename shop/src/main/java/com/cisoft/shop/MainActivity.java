package com.cisoft.shop;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cisoft.myapplication.R;
import com.cisoft.shop.goods.view.GoodsFragment;
import com.cisoft.shop.order.view.OrderFragment;
import com.igexin.sdk.PushManager;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity implements GoodsFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener{

    @BindView(id = R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @BindView(id = R.id.lv_drawer)
    private ListView lvDrawer;

    @BindView(id = R.id.fl_container)
    private FrameLayout flContainer;

    private List<Map<String, ?>> drawerTitle;

    private static final String KEY_TITLE = "drawer_title";

    private static final String KEY_ICON = "drawer_icon";

    private String actionBarTitle;

    private  ActionBarDrawerToggle drawerToggle = null;

    public MainActivity() {
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);
        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    @Override
    protected void initWidget() {
        initDrawer();
        selectItem(0);
    }

    @Override
    protected void initData() {
        initDrawerTitle();
    }

    /**
     * 初始化抽屉标题数据
     */
    private void initDrawerTitle() {
        drawerTitle = new ArrayList<Map<String, ?>>();
        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put(KEY_ICON, R.drawable.ic_launcher);
        item.put(KEY_TITLE, "所有商品");
        drawerTitle.add(item);
        item = new HashMap<String, Object>();
        item.put(KEY_ICON, R.drawable.ic_launcher);
        item.put(KEY_TITLE, "订单列表");
        drawerTitle.add(item);
        item = new HashMap<String, Object>();
        item.put(KEY_ICON, R.drawable.ic_launcher);
        item.put(KEY_TITLE, "检查更新");
        drawerTitle.add(item);
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        lvDrawer.setAdapter(new SimpleAdapter(this, drawerTitle, R.layout.activity_drawer_title_cell, new String[]{KEY_ICON, KEY_TITLE}, new int[]{R.id.iv_drawer_icon, R.id.tv_drawer_title}));
        lvDrawer.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new DrawerToggle(this, drawerLayout, R.drawable.ic_launcher, R.drawable.ic_launcher);
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * 抽屉点击监听器
     */
    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                ViewInject.toast("商品列表");
                getFragmentManager().beginTransaction().replace(R.id.fl_container, GoodsFragment.newInstance("商品"), "goods").commit();
                break;
            case 1:
                ViewInject.toast("订单列表");
                getFragmentManager().beginTransaction().replace(R.id.fl_container, OrderFragment.newInstance("订单"), "order").commit();
                break;
            case 2:
                ViewInject.toast("检查更新");
                break;
        }
        actionBarTitle = (String) drawerTitle.get(position).get(KEY_TITLE);
        getActionBar().setTitle(actionBarTitle);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    private class DrawerToggle extends ActionBarDrawerToggle {

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            getActionBar().setTitle("商家版");
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getActionBar().setTitle(actionBarTitle);
            invalidateOptionsMenu();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
