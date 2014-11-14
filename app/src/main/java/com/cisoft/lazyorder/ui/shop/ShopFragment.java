package com.cisoft.lazyorder.ui.shop;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.AdRotator;
import com.cisoft.lazyorder.widget.MyListView;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.fragment.BaseFragment;
import org.kymjs.aframe.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comit on 10/16/14.
 */
public class ShopFragment extends BaseFragment implements ActionBar.OnNavigationListener, AdapterView.OnItemClickListener{


    @BindView(id = R.id.lvShopList)
    public MyListView lvShopList;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    public ShopListViewAdapter shopListAdapter;
    public ShopCategoryListAdapter shopCategoryListAdapter;
    public AdRotator arImageAdRotator;
    private ShopService shopService;
    private ShopCategoryService shopCategoryService;
    private List<Shop> shopsData;
    private List<ShopCategory> shopCategoryData;

    public Dialog loadingTipDialog;

    private int page = 1;
    private int pager = 10;
    private int shopTypeId = 0;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_shop, null, true);
        return view;
    }

    @Override
    protected void initData() {
        shopsData = new ArrayList<Shop>();
        shopCategoryData = new ArrayList<ShopCategory>();
        shopService = new ShopService(getActivity());
        shopCategoryService = new ShopCategoryService(getActivity());
    }

    @Override
    protected void initWidget(View parentView) {
        showTitleBarMode("正在加载中...");
        showLoadingTip();
        initAdRotator();
        initShopList();
    }


    /**
     * 初始化广告轮播器
     */
    private void initAdRotator() {
        arImageAdRotator = new AdRotator(getActivity());
        arImageAdRotator.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(getActivity(), 150)));
        arImageAdRotator.setImageUrl(new String[]{
                "http://c.hiphotos.baidu.com/image/w%3D310/sign=9f7ddc689158d109c4e3afb3e158ccd0/fd039245d688d43fc5bd2ee47e1ed21b0ef43b8b.jpg",
                "http://c.hiphotos.baidu.com/image/w%3D310/sign=0bd18105de54564ee565e23883df9cde/c2cec3fdfc039245940d5c198494a4c27d1e256f.jpg",
                "http://b.hiphotos.baidu.com/image/w%3D310/sign=115ed965f8f2b211e42e834ffa806511/77c6a7efce1b9d16fc9c2577f0deb48f8c546448.jpg",
                "http://b.hiphotos.baidu.com/image/w%3D310/sign=76e7a2568735e5dd902ca3de46c6a7f5/32fa828ba61ea8d3c43a3600950a304e251f589d.jpg",
                "http://f.hiphotos.baidu.com/image/w%3D310/sign=376bbbc61d178a82ce3c79a1c603737f/a8ec8a13632762d0424ee5b5a3ec08fa513dc64c.jpg"
        });
    }


    /**
     * 初始化店家列表展示控件
     */
    private void initShopList() {
        shopListAdapter = new ShopListViewAdapter(getActivity(), shopsData);
        lvShopList.addHeaderView(arImageAdRotator);
        lvShopList.setAdapter(shopListAdapter);
        lvShopList.setOnItemClickListener(this);
        lvShopList.setPullLoadEnable(true);
        lvShopList.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                shopService.loadShopDataByTypeId(shopTypeId, page, pager, new ShopService.OnShopLoadFinish() {
                    @Override
                    public void onSuccess(List<Shop> shops) {
                        lvShopList.stopRefreshData();

                        if (shops.size() == 0) {
                            showNoValueTip();
                        } else {
                            shopListAdapter.clearAll();
                            shopListAdapter.addData(shops);
                            shopListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(shopService.getResponseStateInfo(stateCode));
                        lvShopList.stopRefreshData();
                        showNoValueTip();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                shopService.loadShopDataByTypeId(shopTypeId, ++page, pager, new ShopService.OnShopLoadFinish() {
                    @Override
                    public void onSuccess(List<Shop> shops) {
                        lvShopList.stopRefreshData();

                        if (shops.size() == 0) {
                            ViewInject.toast("没有更多店家数据了");
                            lvShopList.setPullLoadEnable(false);
                        } else {
                            shopListAdapter.addData(shops);
                            shopListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(shopService.getResponseStateInfo(stateCode));
                        lvShopList.stopRefreshData();
                    }
                });
            }
        });
    }



    /**
     * 显示正在加载的提示
     */
    public void showLoadingTip() {
        if(loadingTipDialog == null){
            loadingTipDialog = DialogFactory.createToastDialog(getActivity(), "正在加载,请稍等");
        }
        if(!loadingTipDialog.isShowing()){
            loadingTipDialog.show();
        }

        llShowNoValueTip.setVisibility(View.GONE);
    }

    /**
     * 隐藏正在加载的提示
     */
    public void hideLoadingTip() {
        llShowNoValueTip.setVisibility(View.GONE);
        if(loadingTipDialog != null && loadingTipDialog.isShowing())
            loadingTipDialog.dismiss();
    }

    /**
     * 显示出没有数据的提示
     */
    public void showNoValueTip() {
        llShowNoValueTip.setVisibility(View.VISIBLE);
        if(loadingTipDialog !=null && loadingTipDialog.isShowing())
            loadingTipDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shop, menu);
    }


    /**
     * ActionBar Item的点击回调
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(getActivity(), WelcomeActivity.class));
                break;
        }

        return true;
    }


    /**
     * 初始化标题栏式的ActionBar
     */
    public void showTitleBarMode(String title) {
        getActivity().getActionBar().setTitle(title);
        shopCategoryService.loadAllShopCategoryData(new ShopCategoryService.OnCategoryLoadFinish() {
            @Override
            public void onSuccess(List<ShopCategory> categories) {
                if(categories.size() == 0){
                    getActivity().getActionBar().setTitle("加载分类失败");
                    hideLoadingTip();
                }else{
                    showNavListMode();
                    shopCategoryListAdapter.addData(categories);
                    shopCategoryListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode) {
                getActivity().getActionBar().setTitle("加载分类失败");
                hideLoadingTip();
                ViewInject.toast(shopCategoryService.getResponseStateInfo(stateCode));
            }
        });
    }

    /**
     * 初始化导航列表式的actionBar
     */
    public void showNavListMode(){
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        shopCategoryListAdapter = new ShopCategoryListAdapter(getActivity(), shopCategoryData);
        actionbar.setListNavigationCallbacks(shopCategoryListAdapter, this);
    }


    /**
     * ActionBar的下拉列表导航的点击回调
     */
    @Override
    public boolean onNavigationItemSelected(int position, long l) {
        page = 1;//初始化page
        shopTypeId = shopCategoryData.get(position).getId();
        if(shopTypeId != 0) {
            showLoadingTip();
            shopService.loadShopDataByTypeId(shopTypeId, page, pager, new ShopService.OnShopLoadFinish() {
                @Override
                public void onSuccess(List<Shop> shops) {
                    hideLoadingTip();

                    if (shops.size() == 0) {
                        showNoValueTip();
                    } else {
                        shopListAdapter.clearAll();
                        shopListAdapter.addData(shops);
                        shopListAdapter.refresh();
                    }
                }

                @Override
                public void onFailure(int stateCode) {
                    hideLoadingTip();
                    showNoValueTip();
                    ViewInject.toast(shopCategoryService.getResponseStateInfo(stateCode));
                }
            });
        } else {
            showNoValueTip();
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
        bundle.putString(ApiConstants.KEY_MER_FACE_PIC, shop.getFaceImgUrl());
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(getActivity(), GoodsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arImageAdRotator.stopAutoPlay();
    }

}
