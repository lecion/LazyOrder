package com.cisoft.lazyorder.ui.express;

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
import android.widget.RelativeLayout;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.ExpressOrder;
import com.cisoft.lazyorder.core.order.OrderNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.EmptyView;
import com.cisoft.lazyorder.widget.RefreshListView;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ExpressOrderListFragment extends MenuItemContent {

    @BindView(id = R.id.rl_root_view)
    private RelativeLayout mRootView;
    @BindView(id = R.id.lv_express_order_list)
    private RefreshListView mLvExpressListView;
    private EmptyView mEmptyView;
    private Dialog mWaitTipDialog;

    private OrderNetwork mOrderNetwork;
    private List<ExpressOrder> mExpressListData;
    private ExpressOrderListAdapter expressOrderListAdapter;

    private int mPage = 1;
    private int mPager = 10;
    private String userPhone = "18883284880";

    public static final int REQ_CODE_POST_EXPRESS = 200;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_express_list, null);

        return view;
    }


    @Override
    protected void initData() {
        mOrderNetwork = new OrderNetwork(getActivity());
        mExpressListData = new ArrayList<ExpressOrder>();
    }


    @Override
    protected void initWidget(View parentView) {
        initialTitleBar();
        initialExpressListView();
        loadExpressListData(false);
    }


    /**
     * 初始化标题栏
     */
    private void initialTitleBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("  历史快递");
    }


    private void initialExpressListView() {
        mLvExpressListView.setPullLoadEnable(false);   //刚开始时不显示"加载更多"的字样
        mLvExpressListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1; //下拉刷新时,页码重置为1
                loadExpressListData(true);
            }

            @Override
            public void onLoadMore() {
                ++mPage; //页码自增加1
                mOrderNetwork.loadExpressHisOrderData(userPhone, mPage, mPager, new OrderNetwork.OnExpressHisOrderLoadFinish() {
                    @Override
                    public void onPreStart() {
                    }

                    @Override
                    public void onSuccess(List<ExpressOrder> expressOrders) {
                        mLvExpressListView.stopRefreshData();

                        if (expressOrders.size() == 0) {
                            ViewInject.toast(getText(R.string.express_data_no_more_title).toString());
                            mLvExpressListView.setPullLoadEnable(false);
                        } else {
                            if (expressOrders.size() < mPager) {
                                mLvExpressListView.setPullLoadEnable(false);
                            } else {
                                mLvExpressListView.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                            }
                            expressOrderListAdapter.addData(expressOrders);
                            expressOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        mLvExpressListView.stopRefreshData();

                        //之前有数据显示就不显示EmptyView了,只吐司提示
                        ViewInject.toast(mOrderNetwork.getResponseStateInfo(stateCode));
                    }
                });
            }
        });

        expressOrderListAdapter = new ExpressOrderListAdapter(getActivity(), mExpressListData);
        mLvExpressListView.setAdapter(expressOrderListAdapter);
        mEmptyView = new EmptyView(getActivity(), mRootView);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                loadExpressListData(false);
            }
        });
    }

    /**
     * 异步加载快递的List数据
     */
    private void loadExpressListData(final boolean isRefresh) {
        mOrderNetwork.loadExpressHisOrderData(userPhone, mPage, mPager, new OrderNetwork.OnExpressHisOrderLoadFinish() {
            @Override
            public void onPreStart() {
                if (!isRefresh) {
                    showWaitTip();
                }
            }

            @Override
            public void onSuccess(List<ExpressOrder> expressOrders) {
                if (isRefresh)
                    mLvExpressListView.stopRefreshData();
                else
                    closeWaitTip();

                if (expressOrders.size() == 0) {
                    mLvExpressListView.setPullLoadEnable(false);
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                } else {
                    if (expressOrders.size() < mPager) {
                        mLvExpressListView.setPullLoadEnable(false);
                    } else {
                        mLvExpressListView.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                    }

                    expressOrderListAdapter.clearAll();
                    expressOrderListAdapter.addData(expressOrders);
                    expressOrderListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode) {
                if (isRefresh)
                    mLvExpressListView.stopRefreshData();
                else
                    closeWaitTip();

                mLvExpressListView.setPullLoadEnable(false);
                ViewInject.toast(mOrderNetwork
                        .getResponseStateInfo(stateCode));
                if (stateCode == ApiConstants.RESPONSE_STATE_NOT_NET || stateCode == ApiConstants.RESPONSE_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!getMenuOpenState()) {
            initialTitleBar();
            inflater.inflate(R.menu.express_list, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_express:
                startActivityForResult(new Intent(getActivity(), PostExpressActivity.class), REQ_CODE_POST_EXPRESS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_POST_EXPRESS:
                if (resultCode == 1) {
                    ExpressOrder expressOrder = (ExpressOrder) data.getSerializableExtra(PostExpressActivity.POSTED_EXPRESS);
                    mExpressListData.add(0, expressOrder);
                    expressOrderListAdapter.refresh();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 显示正在加载的等待提示对话框
     */
    private void showWaitTip() {
        if (mWaitTipDialog == null)
            mWaitTipDialog = DialogFactory.createWaitToastDialog(getActivity(),
                    getActivity().getString(R.string.wait));
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
