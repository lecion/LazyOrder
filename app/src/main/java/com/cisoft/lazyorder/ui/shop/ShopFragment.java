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
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.shop.ShopCategoryService;
import com.cisoft.lazyorder.core.shop.ShopService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsActivity;
import com.cisoft.lazyorder.ui.login.LoginActivity;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.AdRotator;
import com.cisoft.lazyorder.widget.MyListView;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.utils.DensityUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comit on 10/16/14.
 */
public class ShopFragment extends MenuItemContent implements ActionBar.OnNavigationListener, AdapterView.OnItemClickListener{

    @BindView(id = R.id.ll_content_area)
    private LinearLayout llContentArea;

    @BindView(id = R.id.ll_tip_area)
    private LinearLayout llTipArea;

    @BindView(id = R.id.lvShopList)
    private MyListView lvShopList;

    private ShopListAdapter shopListAdapter;
    private ShopCategoryListAdapter shopCategoryListAdapter;
    private AdRotator arImageAdRotator;
    private ShopService shopService;
    private ShopCategoryService shopCategoryService;
    private List<Shop> shopsData;
    private List<ShopCategory> shopCategoryData;

    private Dialog loadingTipDialog;
    private boolean isCategoryLoadFinish = false;

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
        shopListAdapter = new ShopListAdapter(getActivity(), shopsData);
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
                            showNoShopTip();
                        } else {
                            shopListAdapter.clearAll();
                            shopListAdapter.addData(shops);
                            shopListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        lvShopList.stopRefreshData();
                        ViewInject.toast(shopService.getResponseStateInfo(stateCode));
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
     * 初始化标题栏式的ActionBar
     */
    public void showTitleBarMode(String title) {
        showLoadingTip();

        getActivity().getActionBar().setTitle(title);

        shopCategoryService.loadAllShopCategoryData(new ShopCategoryService.OnCategoryLoadFinish() {
            @Override
            public void onSuccess(List<ShopCategory> categories) {
                if(categories.size() == 0){
                    getActivity().getActionBar().setTitle("加载分类失败");
                    isCategoryLoadFinish = false;
                    closeLoadingTip();
                }else{
                    isCategoryLoadFinish = true;
                    showNavListMode();
                    shopCategoryListAdapter.clearAll();
                    shopCategoryListAdapter.addData(categories);
                    shopCategoryListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode) {
                closeLoadingTip();
                getActivity().getActionBar().setTitle("加载分类失败");
                isCategoryLoadFinish = false;

                switch (stateCode) {
                    case ApiConstants.RESPONSE_STATE_NOT_NET:
                        showNoNetTip();
                        break;
                    case ApiConstants.RESPONSE_STATE_NET_POOR:
                        showNetPoorTip();
                        break;
                    case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                        showNoShopTip();
                        break;
                }
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
     * 重新加载列表式导航
     */
    private void reloadNavList(){
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }


    /**
     * 重新加载数据
     */
    private void reloadData(){
        llTipArea.setVisibility(View.GONE);
        llContentArea.setVisibility(View.VISIBLE);

        if (!isCategoryLoadFinish) {
            showTitleBarMode("正在加载分类...");
        } else {
            showLoadingTip();

            shopService.loadShopDataByTypeId(shopTypeId, page, pager, new ShopService.OnShopLoadFinish() {
                @Override
                public void onSuccess(List<Shop> shops) {
                    closeLoadingTip();

                    if (shops.size() == 0) {
                        showNoShopTip();
                    } else {
                        shopListAdapter.clearAll();
                        shopListAdapter.addData(shops);
                        shopListAdapter.refresh();
                    }
                }

                @Override
                public void onFailure(int stateCode) {
                    closeLoadingTip();

                    if(shopsData.size() > 0) {
                        ViewInject.toast(shopService.getResponseStateInfo(stateCode));
                    } else {
                        switch (stateCode) {
                            case ApiConstants.RESPONSE_STATE_NOT_NET:
                                showNoNetTip();
                                break;
                            case ApiConstants.RESPONSE_STATE_NET_POOR:
                                showNetPoorTip();
                                break;
                            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                                showNoShopTip();
                                break;
                        }
                    }
                }
            });
        }
    }



    /**
     * 显示正在加载的提示框
     */
    public void showLoadingTip() {
        if(loadingTipDialog == null){
            loadingTipDialog = DialogFactory.createToastDialog(getActivity(), "正在加载,请稍等");
            loadingTipDialog.setCanceledOnTouchOutside(false);
        }
        if(!loadingTipDialog.isShowing()){
            loadingTipDialog.show();
        }
    }

    /**
     * 关闭正在加载的提示框
     */
    public void closeLoadingTip() {
        if(loadingTipDialog != null && loadingTipDialog.isShowing())
            loadingTipDialog.dismiss();
    }


    /**
     * 显示出没有数据的提示视图
     */
    public void showNoShopTip() {
        llContentArea.setVisibility(View.GONE);
        llTipArea.setVisibility(View.VISIBLE);

        View noShopView = LayoutInflater.from(getActivity()).inflate(R.layout.error_no_shop, null);
        noShopView.findViewById(R.id.btn_error_notice_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadData();
            }
        });
        llTipArea.removeAllViews();
        llTipArea.addView(noShopView);
    }


    /**
     * 显示出网络状态差的提示视图
     */
    public void showNetPoorTip() {
        llContentArea.setVisibility(View.GONE);
        llTipArea.setVisibility(View.VISIBLE);

        View netPoorView = LayoutInflater.from(getActivity()).inflate(R.layout.error_net_poor, null);
        netPoorView.findViewById(R.id.btn_error_notice_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadData();
            }
        });
        llTipArea.removeAllViews();
        llTipArea.addView(netPoorView);
    }


    /**
     * 显示出没有网络的提示视图
     */
    public void showNoNetTip() {
        llContentArea.setVisibility(View.GONE);
        llTipArea.setVisibility(View.VISIBLE);

        View noNetTip = LayoutInflater.from(getActivity()).inflate(R.layout.error_no_net, null);
        noNetTip.findViewById(R.id.btn_error_notice_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadData();
            }
        });
        llTipArea.removeAllViews();
        llTipArea.addView(noNetTip);
    }


    /**
     * ActionBar的下拉列表导航的点击回调
     */
    @Override
    public boolean onNavigationItemSelected(int position, long l) {

        page = 1;//初始化page
        shopTypeId = shopCategoryData.get(position).getId();

        showLoadingTip();

        if(shopTypeId != 0) {
            shopService.loadShopDataByTypeId(shopTypeId, page, pager, new ShopService.OnShopLoadFinish() {
                @Override
                public void onSuccess(List<Shop> shops) {
                    closeLoadingTip();

                    if (shops.size() == 0) {
                        showNoShopTip();
                    } else {
                        shopListAdapter.clearAll();
                        shopListAdapter.addData(shops);
                        shopListAdapter.refresh();
                    }
                }

                @Override
                public void onFailure(int stateCode) {
                    closeLoadingTip();

                    if(shopsData.size() > 0) {
                        ViewInject.toast(shopService.getResponseStateInfo(stateCode));
                    } else {
                        switch (stateCode) {
                            case ApiConstants.RESPONSE_STATE_NOT_NET:
                                showNoNetTip();
                                break;
                            case ApiConstants.RESPONSE_STATE_NET_POOR:
                                showNetPoorTip();
                                break;
                            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                                showNoShopTip();
                                break;
                        }
                    }
                }
            });
        } else {
            showNoShopTip();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arImageAdRotator.stopAutoPlay();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!getMenuOpenState()) {
            inflater.inflate(R.menu.shop, menu);
            reloadNavList();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
