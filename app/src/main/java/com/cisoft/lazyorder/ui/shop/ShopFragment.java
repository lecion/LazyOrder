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
import android.widget.RelativeLayout;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.Advertise;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.shop.AdvertiseNetwork;
import com.cisoft.lazyorder.core.shop.ShopCategoryNetwork;
import com.cisoft.lazyorder.core.shop.ShopNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.express.PostExpressActivity;
import com.cisoft.lazyorder.ui.goods.GoodsActivity;
import com.cisoft.lazyorder.ui.main.menu.BaseMenuItemFragment;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.AdvertiseRotator;
import com.cisoft.lazyorder.widget.EmptyView;
import com.cisoft.lazyorder.widget.ProgressingDialog;
import com.cisoft.lazyorder.widget.RefreshListView;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.DensityUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comit on 10/16/14.
 */
public class ShopFragment extends BaseMenuItemFragment implements ActionBar.OnNavigationListener,
        AdapterView.OnItemClickListener {

    @BindView(id = R.id.rl_root_view)
    private RelativeLayout mRootView;
    @BindView(id = R.id.lv_shop_list)
    private RefreshListView mLvShopListView;
    private AdvertiseRotator mImageAdRotator;
    private EmptyView mEmptyView;
    private Dialog mWaitTipDialog;

    private ProgressingDialog progressingDialog;

    private ShopCategoryNetwork mShopCategoryNetwork;
    private ShopNetwork mShopNetwork;
    private AdvertiseNetwork mAdvertiseNetwork;

    private AppContext appContext;

    private List<Shop> mShopListData;
    private List<ShopCategory> mShopCategoryListData;
    private ShopListAdapter mShopListAdapter;
    private ShopCategoryListAdapter mShopCategoryListAdapter;

    private int mPage = 1;    //页码
    private int mPager = 5;    //每页显示的Shop个数
    private int mShopCategoryId = 0;
    private int mSchoolId = 0;
    private boolean mShopCategoryLoadFinish = false;
    private boolean mAdvertiseLoadFinish = false;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_shop, null, true);

        return view;
    }

    @Override
    protected void initData() {
        appContext = (AppContext) getActivity().getApplication();
        mShopListData = new ArrayList<Shop>();
        mShopCategoryListData = new ArrayList<ShopCategory>();
        mAdvertiseNetwork = new AdvertiseNetwork(getActivity());
        mShopNetwork = new ShopNetwork(getActivity());
        mShopCategoryNetwork = new ShopCategoryNetwork(getActivity());
        mSchoolId = appContext.getSchoolId();
    }

    @Override
    protected void initWidget(View parentView) {
        initialTitleBar();
        initialAdRotator();
        initialShopListView();
        loadDataForTitleBar();
        loadDateForAdRotator();
    }

    /**
     * 初始化title bar
     */
    private void initialTitleBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getText(R.string.toast_shop_category_loading));
    }

    /**
     * 初始化title bar上的列表式导航
     */
    private void initialNavList() {
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        mShopCategoryListAdapter = new ShopCategoryListAdapter(getActivity(), mShopCategoryListData);
        actionbar.setListNavigationCallbacks(mShopCategoryListAdapter, this);
    }

    /**
     * 重新初始化title bar上的列表式导航（标题和列表式导航切换时使用）
     */
    private void reinitialNavList(){
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    /**
     * 初始化广告轮播器
     */
    private void initialAdRotator() {
        mImageAdRotator = new AdvertiseRotator(getActivity());
        mImageAdRotator.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                DensityUtils.dip2px(getActivity(), 150)));
    }

    /**
     * 初始化店家列表的ListView
     */
    private void initialShopListView() {
        mLvShopListView.addHeaderView(mImageAdRotator); //将广告控件添加到HeaderView里
        mLvShopListView.setOnItemClickListener(this);
        mLvShopListView.setPullLoadEnable(false);   //刚开始时不显示"加载更多"的字样
        mLvShopListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1; //下拉刷新时,页码重置为1

                loadShopListData(true);
            }

            @Override
            public void onLoadMore() {
                ++mPage; //页码自增加1
                mShopNetwork.loadShopDataByTypeId(mShopCategoryId, mPage, mPager, new ShopNetwork.OnShopLoadCallback() {
                    @Override
                    public void onPreStart() {}

                    @Override
                    public void onSuccess(List<Shop> shops) {
                        mLvShopListView.stopRefreshData();

                        if (shops.size() == 0) {
                            ViewInject.toast(getText(R.string.toast_no_more_data).toString());
                            mLvShopListView.setPullLoadEnable(false);
                        } else {
                            if (shops.size() < mPager) {
                                mLvShopListView.setPullLoadEnable(false);
                            } else {
                                mLvShopListView.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                            }
                            mShopListAdapter.addData(shops);
                            mShopListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode, String errorMsg) {
                        mLvShopListView.stopRefreshData();

                        //之前有数据显示就不显示EmptyView了,只吐司提示
                        ViewInject.toast(errorMsg);
                    }
                });
            }
        });

        mShopListAdapter = new ShopListAdapter(getActivity(), mShopListData);
        mLvShopListView.setAdapter(mShopListAdapter);
        mEmptyView = new EmptyView(getActivity(), mRootView);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                reloadData();
            }
        });
    }


    /**
     * 重新加载数据
     * 当未成功加载后重试时使用
     */
    private void reloadData() {

        if(!mShopCategoryLoadFinish) {
            loadDataForTitleBar();
        } else {
            loadShopListData(false);
        }

        if(!mAdvertiseLoadFinish) {
            loadDateForAdRotator();
        }
    }

    /**
     * 异步加载titlebar的数据
     */
    private void loadDataForTitleBar() {
        initialTitleBar();
        mShopCategoryNetwork.loadShopCategoryListData(mSchoolId, new ShopCategoryNetwork.OnCategoryLoadCallback() {
            @Override
            public void onPreStart() {
//                showWaitTip();
            }

            @Override
            public void onSuccess(List<ShopCategory> categories) {
                if (categories.size() == 0) {
                    getActivity().getActionBar().setTitle(getText(R.string.toast_shop_type_loading_error));
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                    mShopCategoryLoadFinish = false;
                    closeWaitTip();
                } else {
                    mShopCategoryLoadFinish = true;
                    initialNavList();
                    mShopCategoryListAdapter.clearAll();
                    mShopCategoryListAdapter.addData(categories);
                    mShopCategoryListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                closeWaitTip();

                getActivity().getActionBar().setTitle(getText(R.string.toast_shop_type_loading_error));
                mShopCategoryLoadFinish = false;
                ViewInject.toast(errorMsg);
                if (stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        });
    }

    /**
     * 异步加载轮播广告组件的数据
     */
    private void loadDateForAdRotator() {
        mAdvertiseNetwork.loadAdvertiseDate(new AdvertiseNetwork.OnAdvertiseLoadFinish() {
            @Override
            public void onSuccess(List<Advertise> advertises) {
                if (advertises.size() != 0) {
                    mImageAdRotator.setAdvertiseData(advertises);
                    mAdvertiseLoadFinish = true;
                } else {
                    mAdvertiseLoadFinish = false;
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                mAdvertiseLoadFinish = false;
            }
        });
    }

    /**
     * 异步加载店家列表的数据
     * @param isRefresh
     */
    private void loadShopListData(final boolean isRefresh) {
        mPage = 1;//初始化page
        mShopNetwork.loadShopDataByTypeId(mShopCategoryId, mPage, mPager, new ShopNetwork.OnShopLoadCallback() {
            @Override
            public void onPreStart() {
                if (!isRefresh) {
                    showWaitTip();
                }
            }

            @Override
            public void onSuccess(List<Shop> shops) {
                if (isRefresh)
                    mLvShopListView.completeRefresh(true);
//                    mLvShopListView.stopRefreshData();
                else
                    closeWaitTip();

                if (shops.size() == 0) {
                    mLvShopListView.setPullLoadEnable(false);
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                } else {
                    if (shops.size() < mPager) {
                        mLvShopListView.setPullLoadEnable(false);
                    } else {
                        mLvShopListView.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                    }

                    mShopListAdapter.clearAll();
                    mShopListAdapter.addData(shops);
                    mShopListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (isRefresh)
                    mLvShopListView.completeRefresh(false);
//                    mLvShopListView.stopRefreshData();
                else
                    closeWaitTip();

                mLvShopListView.setPullLoadEnable(false);
                ViewInject.toast(errorMsg);
                if(stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        });
    }


    /**
     * 显示正在加载的等待提示对话框
     *
     */
    private void showWaitTip() {
//        if (mWaitTipDialog == null)
//            mWaitTipDialog = DialogFactory.createWaitToastDialog(getActivity(),
//                    getActivity().getString(R.string.toast_wait));
//        mWaitTipDialog.show();
        if (progressingDialog == null)
            progressingDialog = new ProgressingDialog(getActivity(), R.string.toast_wait);
        progressingDialog.show();
    }

    /**
     * 关闭正在加载的等待提示对话框
     *
     */
    private void closeWaitTip() {
//        if (mWaitTipDialog != null && mWaitTipDialog.isShowing()) {
//            mWaitTipDialog.dismiss();
//        }
        if (progressingDialog != null && progressingDialog.isShowing()) {
            progressingDialog.dismiss();
        }
    }


    /**
     * title bar上的列表式导航的点击处理
     * PS:第一次加载默认会自动触发第一项
     */
    @Override
    public boolean onNavigationItemSelected(int position, long l) {
        mShopCategoryId = mShopCategoryListData.get(position).getId();
        mEmptyView.hideEmptyView();
//        loadShopListData(true);
        mLvShopListView.startRefresh();
        return true;
    }


    /**
     * 店家列表Item项的点击处理
     */
    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long l) {
        Shop shop = (Shop) adapterView.getItemAtPosition(position);
        if (shop != null) {
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
    }

    /**
     * 当依附的activity创建好后会回调此函数
     * 用来表明此fragment在action bar上也有菜单选项
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //销毁时停止掉轮播广告的滚动
        mImageAdRotator.stopAutoPlay();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //抽屉菜单没有展开时才显示此fragment的菜单选项
        if (!getMenuOpenState()) {
            if(mShopCategoryLoadFinish) {
                reinitialNavList();
            }
            inflater.inflate(R.menu.menu_shop, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_express:
                PostExpressActivity.startFrom(getActivity());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}