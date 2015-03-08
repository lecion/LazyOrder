package com.cisoft.shop;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cisoft.shop.expressorder.view.ExpressOrderFragment;
import com.cisoft.shop.goods.view.GoodsFragment;
import com.cisoft.shop.login.view.LoginActivity;
import com.cisoft.shop.order.view.OrderFragment;
import com.cisoft.shop.util.IOUtil;
import com.cisoft.shop.util.L;
import com.cisoft.shop.widget.DialogFactory;
import com.igexin.sdk.PushConsts;
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

    @BindView(id = R.id.btn_new_msg)
    private Button btnNewMsg;

    private List<Map<String, ?>> drawerTitle;

    private static final String KEY_TITLE = "drawer_title";

    private static final String KEY_ICON = "drawer_icon";

    private String actionBarTitle;

    private  ActionBarDrawerToggle drawerToggle = null;

    private MyReceiver receiver;

    private int loginType;

    public MainActivity() {
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.cisoft.receivemsg");
        receiver = new MyReceiver();
        getApplicationContext().registerReceiver(receiver, filter);
        PushManager.getInstance().initialize(this.getApplicationContext());
        String clientId = PushManager.getInstance().getClientid(this);
//        Log.d("MainActivity", clientId);
        bindAlias();
        loginType = IOUtil.getLoginType(this);
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
        item.put(KEY_TITLE, "查看订单");
        drawerTitle.add(item);

        if (loginType == AppConfig.TYPE_MERCHANT) {
            item = new HashMap<String, Object>();
            item.put(KEY_ICON, R.drawable.ic_launcher);
            item.put(KEY_TITLE, "查看商品");
            drawerTitle.add(item);
        }

        item = new HashMap<String, Object>();
        item.put(KEY_ICON, R.drawable.ic_launcher);
        item.put(KEY_TITLE, "已完成订单");
        drawerTitle.add(item);

        item = new HashMap<String, Object>();
        item.put(KEY_ICON, R.drawable.ic_launcher);
        item.put(KEY_TITLE, "统计");
        drawerTitle.add(item);

        item = new HashMap<String, Object>();
        item.put(KEY_ICON, R.drawable.ic_launcher);
        item.put(KEY_TITLE, "注销");
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
        if (loginType == AppConfig.TYPE_MERCHANT) {
            switch (position) {
                case 0:
                    ViewInject.toast("查看订单");
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, OrderFragment.newInstance("订单"), "order").commit();
                    break;
                case 1:
                    ViewInject.toast("查看商品");
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, GoodsFragment.newInstance("商品"), "goods").commit();
                    break;
                case 2:
                    ViewInject.toast("已完成订单");
                    break;
                case 3:
                    ViewInject.toast("统计");
                    break;
                case 4:
                    doLogout();
                    break;
            }

        } else if (loginType == AppConfig.TYPE_EXPMER) {
            switch (position) {
                case 0:
                    ViewInject.toast("查看订单");
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, ExpressOrderFragment.newInstance("订单"), "order").commit();
                    break;
                case 1:
                    ViewInject.toast("已完成订单");
                    break;
                case 2:
                    ViewInject.toast("统计");
                    break;
                case 3:
                    doLogout();
                    break;
            }
        }
        actionBarTitle = (String) drawerTitle.get(position).get(KEY_TITLE);
        getActionBar().setTitle(actionBarTitle);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    /**
     * 执行注销
     */
    private void doLogout() {
        IOUtil.clearLoginInfo(this);
        DialogFactory.createConfirmDialog(this, "", "一定要离开我吗", "抹泪离去", "再逗逗我", new DialogFactory.IConfirm() {
            @Override
            public void onYes() {
                skipActivity(MainActivity.this, LoginActivity.class);
            }
        }).show();
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

    /**
     * 将ShopId与别名相互绑定
     * 别名格式：shop_***  ***为shop的id号
     */
    private void bindAlias() {
        if (loginType == AppConfig.TYPE_MERCHANT) {
            PushManager.getInstance().bindAlias(this, "shop_" + L.getShop(this).getId());
        } else if (loginType == AppConfig.TYPE_EXPMER) {
            PushManager.getInstance().bindAlias(this, "express_" + L.getExpmer(this).getId());
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        public static final int GET_MSG = 1;

        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            Log.d("onReceive", "MainActivity");
            switch (data.getInt(PushConsts.CMD_ACTION)) {
                case PushConsts.GET_MSG_DATA:
                    byte[] payload = data.getByteArray("payload");
                    if (payload != null) {
                        String rs = new String(payload);
                        Log.d("GET_MESSAGE", rs);
                        Message msg = handler.obtainMessage();
                        msg.what = GET_MSG;
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", rs);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    break;
                case PushConsts.GET_CLIENTID:
                    String clientId = data.getString("clientid");
                    Log.d("GET_CLIENTID", clientId);
                    break;
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyReceiver.GET_MSG:
                    newOrders(msg.getData());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void newOrders(Bundle data) {
        //TODO 获得新订单更新UI
        Log.d("DEBUGDEBUG", data.getString("msg"));
        String msg = data.getString("msg");
        if (btnNewMsg.getVisibility() == View.VISIBLE) {
            Log.d("newOrders", "visible");
            //已经有新消息提示
            btnNewMsg.setText(msg);
        } else {
            //还没有新消息提示
            btnNewMsg.setVisibility(View.VISIBLE);
            btnNewMsg.setText(msg);
            Log.d("newOrders", "invisible");
        }
    }

}
