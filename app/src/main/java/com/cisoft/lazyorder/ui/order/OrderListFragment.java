package com.cisoft.lazyorder.ui.order;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.bean.order.Order;
import com.cisoft.lazyorder.core.order.OrderNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.main.menu.BaseMenuItemFragment;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.EmptyView;
import com.cisoft.lazyorder.widget.RefreshListView;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends BaseMenuItemFragment
        implements AdapterView.OnItemClickListener {

    @BindView(id = R.id.rl_root_view)
    private RelativeLayout mRootView;
    @BindView(id = R.id.lv_order_list)
    private RefreshListView mLvOrderListView;
    private EmptyView mEmptyView;
    private Dialog mWaitTipDialog;

    private OrderNetwork mOrderNetwork;
    private List<Order> mOrderListData;
    private OrderListAdapter mOrderListAdapter;
    private AppContext mAppContext;

    private int mPage = 1;
    private int mPager = 10;
    private String mPhone;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_history_order, null);

        return view;
    }

    @Override
    protected void initData() {
        mOrderNetwork = new OrderNetwork(getActivity());
        mOrderListData = new ArrayList<Order>();
        mAppContext = (AppContext) getActivity().getApplication();
        if (mAppContext.isLogin()) {
            User loginUser = mAppContext.getLoginInfo();
            mPhone = loginUser.getUserPhone();
        } else {
            mPhone = mAppContext.getRecentPhone();
        }
    }


    @Override
    protected void initWidget(View parentView) {
        initialTitleBar();
        initialOrderListView();
        loadOrderListData(false);
    }

    /**
    * 初始化标题栏
    */
    private void initialTitleBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_fragment_history_order));
    }

    /**
     * 初始化订单记录的ListView
     */
    private void initialOrderListView() {
        mLvOrderListView.setOnItemClickListener(this);
        mLvOrderListView.setPullLoadEnable(false);   //刚开始时不显示"加载更多"的字样
        mLvOrderListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1; //下拉刷新时,页码重置为1
                loadOrderListData(true);
            }

            @Override
            public void onLoadMore() {
                ++mPage; //页码自增加1
                mOrderNetwork.loadOrderListData(mPhone, mPage, mPager, new OrderNetwork.OnOrderListLoadFinish() {
                    @Override
                    public void onPreStart() {
                    }

                    @Override
                    public void onSuccess(List<Order> dishOrders) {
                        mLvOrderListView.stopRefreshData();

                        if (dishOrders.size() == 0) {
                            ViewInject.toast(getText(R.string.toast_no_more_data).toString());
                            mLvOrderListView.setPullLoadEnable(false);
                        } else {
                            if (dishOrders.size() < mPager) {
                                mLvOrderListView.setPullLoadEnable(false);
                            } else {
                                mLvOrderListView.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                            }
                            mOrderListAdapter.addData(dishOrders);
                            mOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode, String errorMsg) {
                        mLvOrderListView.stopRefreshData();

                        //之前有数据显示就不显示EmptyView了,只吐司提示
                        ViewInject.toast(errorMsg);
                    }
                });
            }
        });

        mOrderListAdapter = new OrderListAdapter(getActivity(), mOrderListData);
        mLvOrderListView.setAdapter(mOrderListAdapter);
        mEmptyView = new EmptyView(getActivity(), mRootView);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                loadOrderListData(false);
            }
        });
    }

    /**
     * 异步加载快递的List数据
     */
    private void loadOrderListData(final boolean isRefresh) {
        if (StringUtils.isEmpty(mPhone)) {
            mEmptyView.showEmptyView(EmptyView.NO_DATA);
            return;
        }

        mOrderNetwork.loadOrderListData(mPhone, mPage, mPager, new OrderNetwork.OnOrderListLoadFinish() {
            @Override
            public void onPreStart() {
                if (!isRefresh) {
                    showWaitTip();
                }
            }

            @Override
            public void onSuccess(List<Order> dishOrders) {
                if (isRefresh)
                    mLvOrderListView.stopRefreshData();
                else
                    closeWaitTip();

                if (dishOrders.size() == 0) {
                    mLvOrderListView.setPullLoadEnable(false);
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                } else {
                    if (dishOrders.size() < mPager) {
                        mLvOrderListView.setPullLoadEnable(false);
                    } else {
                        mLvOrderListView.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                    }

                    mOrderListAdapter.clearAll();
                    mOrderListAdapter.addData(dishOrders);
                    mOrderListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (isRefresh)
                    mLvOrderListView.stopRefreshData();
                else
                    closeWaitTip();

                mLvOrderListView.setPullLoadEnable(false);
                ViewInject.toast(errorMsg);
                if (stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Order dishOrder = (Order) adapterView.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderDetailActivity.EXTRA_ORDER_OBJ, dishOrder);
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!getMenuOpenState()) {
            initialTitleBar();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 显示正在加载的等待提示对话框
     */
    private void showWaitTip() {
        if (mWaitTipDialog == null)
            mWaitTipDialog = DialogFactory.createWaitToastDialog(getActivity(),
                    getActivity().getString(R.string.toast_wait));
        mWaitTipDialog.show();
    }

    /**
     * 关闭正在加载的等待提示对话框
     */
    private void closeWaitTip() {
        if (mWaitTipDialog != null && mWaitTipDialog.isShowing()) {
            mWaitTipDialog.dismiss();
        }
    }
}
