package com.cisoft.lazyorder.ui.shop;

import java.util.ArrayList;
import java.util.List;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.bitmap.KJBitmapConfig;
import org.kymjs.aframe.bitmap.utils.BitmapCreate;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJRefreshListener;
import org.kymjs.aframe.utils.DensityUtils;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.WelcomeActivity;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.shop.ShopCategoryService;
import com.cisoft.lazyorder.core.shop.ShopService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsActivity;
import com.cisoft.lazyorder.widget.AdRotator;

/**
 * Created by comit on 10/16/14.
 */
public class ShopActivity extends BaseActivity implements ActionBar.OnNavigationListener, AdapterView.OnItemClickListener{

    @BindView(id = R.id.lvShopList)
    public KJListView lvShopList;

    @BindView(id = R.id.llLoadingShopListTip)
    private LinearLayout llLoadingShopListTip;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    public ShopListViewAdapter shopListAdapter;
    public ShopCategoryListAdapter shopCategoryListAdapter;
    public AdRotator arImageAdRotator;
    private ShopService shopService;
    private ShopCategoryService shopCategoryService;
    private List<Shop> shopsData;
    private List<ShopCategory> shopCategoryData;

    private int page = 1;
    private int pager = 10;

    //是否正在刷新的标志位
    //（用于网络获取数据时区分是刷新调度的还是没有缓存时调度的）
    public boolean lvRefreshing = false;

    public ShopActivity() {
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_shop);
    }


    @Override
    protected void initData() {
        shopsData = new ArrayList<Shop>();
        shopCategoryData = new ArrayList<ShopCategory>();
        shopService = new ShopService(this);
        shopCategoryService = new ShopCategoryService(this);
    }


    @Override
    protected void initWidget() {
        initActionBar();
        loadingTipShow();
        initShopList();
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        ActionBar actionbar = getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        shopCategoryService.loadAllShopCategoryData();
        shopCategoryListAdapter = new ShopCategoryListAdapter(this, shopCategoryData);
        actionbar.setListNavigationCallbacks(shopCategoryListAdapter, this);
    }

    private void initShopList() {
        //自定义广告控件初始化
        arImageAdRotator = new AdRotator(this);
        arImageAdRotator.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 150)));
        arImageAdRotator.setImageUrl(new String[]{
            "http://c.hiphotos.baidu.com/image/w%3D310/sign=9f7ddc689158d109c4e3afb3e158ccd0/fd039245d688d43fc5bd2ee47e1ed21b0ef43b8b.jpg",
            "http://c.hiphotos.baidu.com/image/w%3D310/sign=0bd18105de54564ee565e23883df9cde/c2cec3fdfc039245940d5c198494a4c27d1e256f.jpg",
            "http://b.hiphotos.baidu.com/image/w%3D310/sign=115ed965f8f2b211e42e834ffa806511/77c6a7efce1b9d16fc9c2577f0deb48f8c546448.jpg",
            "http://b.hiphotos.baidu.com/image/w%3D310/sign=76e7a2568735e5dd902ca3de46c6a7f5/32fa828ba61ea8d3c43a3600950a304e251f589d.jpg",
            "http://f.hiphotos.baidu.com/image/w%3D310/sign=376bbbc61d178a82ce3c79a1c603737f/a8ec8a13632762d0424ee5b5a3ec08fa513dc64c.jpg"
        });

        //ListView初始化
        shopListAdapter = new ShopListViewAdapter(this, shopsData);
        lvShopList.addHeaderView(arImageAdRotator);
        lvShopList.setAdapter(shopListAdapter);
        lvShopList.setOnItemClickListener(this);
        lvShopList.setPullLoadEnable(true);
        lvShopList.setOnRefreshListener(new KJRefreshListener() {
            @Override
            public void onRefresh() {
                lvRefreshing = true;
                shopService.loadAllShopDataFromNet(1, pager);
            }

            @Override
            public void onLoadMore() {
                lvRefreshing = true;
                shopService.loadAllShopDataFromNet(++page, pager);
            }
        });
        shopService.loadAllShopDataFromCache(page, pager);
    }



    /**
     * 显示正在加载的提示,并隐藏店家列表
     */
    public void loadingTipShow() {
        llLoadingShopListTip.setVisibility(View.VISIBLE);
        llShowNoValueTip.setVisibility(View.GONE);
    }

    /**
     * 隐藏正在加载的提示,并显示店家列表
     */
    public void loadingTipHide() {
        llLoadingShopListTip.setVisibility(View.GONE);
        llShowNoValueTip.setVisibility(View.GONE);
    }

    /**
     * 显示出没有数据的提示
     */
    public void showNoValueTip() {
        llLoadingShopListTip.setVisibility(View.GONE);
        llShowNoValueTip.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop, menu);

        return true;
    }


    /**
     * ActionBar Item的点击回调
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                showActivity(this, WelcomeActivity.class);
                break;
            case R.id.menu_serach:
                showActivity(this, WelcomeActivity.class);
                break;
        }

        return true;
    }

    /**
     * ActionBar的下拉列表导航的点击回调
     */
    @Override
    public boolean onNavigationItemSelected(int position, long l) {
        page = 1;//初始化page

        loadingTipShow();
        if (position == 0) {//加载全部的店家列表
            shopService.loadAllShopDataFromCache(page, pager);
        } else {
            int shopTypeId = shopCategoryData.get(position-1).getId();
            shopService.loadShopDataFromNetByTypeId(shopTypeId, page, pager);
        }

        return true;
    }

    /**
     * 店家列表Item项的点击回调
     */
    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long l) {
        Shop shop = (Shop) adapterView.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putInt(ApiConstants.KEY_MER_ID, shop.getId());
        bundle.putString(ApiConstants.KEY_MER_ADDRESS, shop.getAddress());
        bundle.putString(ApiConstants.KEY_MER_PROMOTION_INFO, shop.getPromotionInfo());
        bundle.putString(ApiConstants.KEY_MER_NAME, shop.getName());
        showActivity(this, GoodsActivity.class, bundle);
    }

}
