package com.cisoft.lazyorder.ui.orderlist;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.core.order.OrderService;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;
import com.cisoft.lazyorder.ui.orderdetail.OrderDetailActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.MyListView;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;

public class HistoryOrderFragment extends MenuItemContent implements AdapterView.OnItemClickListener{


    @BindView(id = R.id.lvHistoryOrderList)
    private MyListView lvHistoryOrderList;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    private OrderService orderService;
    private List<DishOrder> hisOrderList = new ArrayList<DishOrder>();
    private HistoryOrderListAdapter hisOrderListAdapter;

    public Dialog loadingTipDialog;

    private int page = 1;
    private int pager = 10;
    private String userPhone = "18883284880";

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_history_order, null);

        return view;
    }

    @Override
    protected void initData() {
        orderService = new OrderService(getActivity());
    }


    @Override
    protected void initWidget(View parentView) {
        initActionBar();
        showLoadingTip();
        initHisOrderList();
    }


    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(" 历史订单");
    }

    private void initHisOrderList(){
        hisOrderListAdapter = new HistoryOrderListAdapter(getActivity(), hisOrderList);
        lvHistoryOrderList.setAdapter(hisOrderListAdapter);
        lvHistoryOrderList.setPullLoadEnable(true);
        lvHistoryOrderList.setOnItemClickListener(this);
        lvHistoryOrderList.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                orderService.loadDishHisOrderData(userPhone, page, pager, new OrderService.OnDishHisOrderLoadFinish() {
                    @Override
                    public void onSuccess(List<DishOrder> dishOrders) {
                        lvHistoryOrderList.stopRefreshData();

                        if (dishOrders.size() == 0) {
                            showNoValueTip();
                        } else {
                            hisOrderListAdapter.clearAll();
                            hisOrderListAdapter.addData(dishOrders);
                            hisOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(orderService.getResponseStateInfo(stateCode));
                        lvHistoryOrderList.stopRefreshData();
                        showNoValueTip();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                orderService.loadDishHisOrderData(userPhone, page, pager, new OrderService.OnDishHisOrderLoadFinish() {
                    @Override
                    public void onSuccess(List<DishOrder> dishOrders) {
                        lvHistoryOrderList.stopRefreshData();

                        if (dishOrders.size() == 0) {
                            ViewInject.toast("没有更多店家数据了");
                            lvHistoryOrderList.setPullLoadEnable(false);
                        } else {
                            hisOrderListAdapter.addData(dishOrders);
                            hisOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(orderService.getResponseStateInfo(stateCode));
                        lvHistoryOrderList.stopRefreshData();
                    }
                });
            }
        });

        orderService.loadDishHisOrderData(userPhone, page, pager, new OrderService.OnDishHisOrderLoadFinish() {
            @Override
            public void onSuccess(List<DishOrder> dishOrders) {
                hideLoadingTip();

                if (dishOrders.size() == 0) {
                    showNoValueTip();
                } else {
                    hisOrderListAdapter.clearAll();
                    hisOrderListAdapter.addData(dishOrders);
                    hisOrderListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(orderService.getResponseStateInfo(stateCode));
                hideLoadingTip();
                showNoValueTip();
            }
        });
    }


    /**
     * 显示正在加载的提示
     */
    public void showLoadingTip() {
        if(loadingTipDialog == null){
            loadingTipDialog = DialogFactory.createToastDialog(getActivity(), "正在加载,请稍等");
            loadingTipDialog.setCancelable(false);
            loadingTipDialog.setCanceledOnTouchOutside(false);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        DishOrder dishOrder = (DishOrder) adapterView.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderDetailActivity.DISH_ORDER, dishOrder);
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
            initActionBar();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
