package com.cisoft.lazyorder.ui.shop;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.WelcomeActivity;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.shop.ShopCategoryService;
import com.cisoft.lazyorder.core.shop.ShopService;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.bitmap.KJBitmapConfig;
import org.kymjs.aframe.bitmap.utils.BitmapCreate;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.ui.widget.KJListView;
import org.kymjs.aframe.ui.widget.KJRefreshListener;
import org.kymjs.aframe.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comit on 10/16/14.
 */
public class ShopActivity extends BaseActivity implements ActionBar.OnNavigationListener, AdapterView.OnItemClickListener{

    @BindView(id = R.id.lvShopList)
    public KJListView lvShopList;

    @BindView(id = R.id.llLoadingGoodsListTip)
    private LinearLayout llLoadingShopListTip;
    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    public ShopListViewAdapter shopListAdapter;
    public ShopCategoryListAdapter shopCategoryListAdapter;

    private KJBitmap kjBitmap;
    private ShopService shopService;
    private ShopCategoryService shopCategoryService;
    private List<Shop> shopsData;
    private List<ShopCategory> shopCategoryData;

    private int page = 1;
    private int pager = 10;
    //加载店家的头像时默认显示的加载图的宽（单位：dp）
    private int loadingImgWidthForDp = 100;
    private int loadingImgHeightForDp = 100;
    //是否正在刷新的标志位（用于网络获取数据时区分是刷新调度的还是没有缓存时调度的）
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
        initBitmap();
        initShopListAdapter();

        loadingTipShow();
        shopService.loadAllShopDataFromCache(page, pager);
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

    /**
     * 初始化带loadingBitmap的bitmap对象
     */
    private void initBitmap() {
        KJBitmapConfig bitmapConfig = new KJBitmapConfig();
        bitmapConfig.loadingBitmap = BitmapCreate.bitmapFromResource(getResources(), R.drawable.ic_launcher,
                DensityUtils.dip2px(this, loadingImgWidthForDp), DensityUtils.dip2px(this, loadingImgHeightForDp));
        kjBitmap = KJBitmap.create(bitmapConfig);
    }



    private void initShopListAdapter() {
        shopListAdapter = new ShopListViewAdapter(this, shopsData, kjBitmap);
        lvShopList.setAdapter(shopListAdapter);
        lvShopList.getHeadView().setBackgroundResource(R.drawable.ic_launcher);
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
    }



    /**
     * 显示正在加载的提示,并隐藏店家列表
     */
    public void loadingTipShow() {
        llLoadingShopListTip.setVisibility(View.VISIBLE);
        lvShopList.setVisibility(View.GONE);
        llShowNoValueTip.setVisibility(View.GONE);
    }

    /**
     * 隐藏正在加载的提示,并显示店家列表
     */
    public void loadingTipHide() {
        llLoadingShopListTip.setVisibility(View.GONE);
        llShowNoValueTip.setVisibility(View.GONE);
        lvShopList.setVisibility(View.VISIBLE);
    }

    /**
     * 显示出没有数据的提示
     */
    public void showNoValueTip() {
        llLoadingShopListTip.setVisibility(View.GONE);
        lvShopList.setVisibility(View.GONE);
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
        showActivity(this, WelcomeActivity.class, bundle);
    }

    public void setListViewHeightBasedOnChildren() {

        int totalHeight = 0;
        for (int i = 0; i < shopListAdapter.getCount(); i++) {
            View listItem = shopListAdapter.getView(i, null, lvShopList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvShopList.getLayoutParams();
        params.height = totalHeight + (lvShopList.getDividerHeight() * (shopListAdapter.getCount() - 1));
        lvShopList.setLayoutParams(params);
    }
}
