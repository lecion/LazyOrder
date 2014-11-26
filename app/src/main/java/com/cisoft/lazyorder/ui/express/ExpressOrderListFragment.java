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
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.bean.order.ExpressOrder;
import com.cisoft.lazyorder.core.order.OrderService;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;
import com.cisoft.lazyorder.ui.orderdetail.OrderDetailActivity;
import com.cisoft.lazyorder.ui.orderlist.HistoryOrderListAdapter;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.MyListView;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ExpressOrderListFragment extends MenuItemContent implements AdapterView.OnItemClickListener{

    @BindView(id = R.id.lv_express_order_list)
    private MyListView lvExpressOrderList;

    @BindView(id = R.id.ll_show_no_value_tip)
    private LinearLayout llShowNoValueTip;

    private OrderService orderService;
    private List<ExpressOrder> expressOrderList = new ArrayList<ExpressOrder>();
    private ExpressOrderListAdapter expressOrderListAdapter;

    public Dialog loadingTipDialog;

    private int page = 1;
    private int pager = 10;
    private String userPhone = "18883284880";

    public static final String EXPRESS_ORDER = "expressOrder";
    public static final int REQUEST_CODE_POST_EXPRESS = 200;
    public static final int RESULT_CODE_POST_EXPRESS_SUCCESS = 300;
    public static final int RESULT_CODE_POST_EXPRESS_FAILED = 400;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_express_order_list, null);

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
        initExpressOrderList();
    }


    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("  历史快递");
    }


    private void initExpressOrderList(){
        expressOrderListAdapter = new ExpressOrderListAdapter(getActivity(), expressOrderList);
        lvExpressOrderList.setAdapter(expressOrderListAdapter);
        lvExpressOrderList.setPullLoadEnable(true);
        lvExpressOrderList.setOnItemClickListener(this);
        lvExpressOrderList.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                orderService.loadExpressHisOrderData(userPhone, page, pager, new OrderService.OnExpressHisOrderLoadFinish() {
                    @Override
                    public void onSuccess(List<ExpressOrder> expressOrders) {
                        lvExpressOrderList.stopRefreshData();

                        if (expressOrders.size() == 0) {
                            showNoValueTip();
                        } else {
                            expressOrderListAdapter.clearAll();
                            expressOrderListAdapter.addData(expressOrders);
                            expressOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(orderService.getResponseStateInfo(stateCode));
                        lvExpressOrderList.stopRefreshData();
                        showNoValueTip();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                orderService.loadExpressHisOrderData(userPhone, page, pager, new OrderService.OnExpressHisOrderLoadFinish() {
                    @Override
                    public void onSuccess(List<ExpressOrder> expressOrders) {
                        lvExpressOrderList.stopRefreshData();

                        if (expressOrders.size() == 0) {
                            ViewInject.toast("没有更多快递数据了");
                            lvExpressOrderList.setPullLoadEnable(false);
                        } else {
                            expressOrderListAdapter.addData(expressOrders);
                            expressOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(orderService.getResponseStateInfo(stateCode));
                        lvExpressOrderList.stopRefreshData();
                    }
                });
            }
        });

        orderService.loadExpressHisOrderData(userPhone, page, pager, new OrderService.OnExpressHisOrderLoadFinish() {
            @Override
            public void onSuccess(List<ExpressOrder> expressOrders) {
                hideLoadingTip();

                if (expressOrders.size() == 0) {
                    showNoValueTip();
                } else {
                    expressOrderListAdapter.clearAll();
                    expressOrderListAdapter.addData(expressOrders);
                    expressOrderListAdapter.refresh();
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
        ExpressOrder expressOrder = (ExpressOrder) adapterView.getItemAtPosition(position);
        Bundle bundle = new Bundle();
//        bundle.putSerializable(OrderDetailActivity.DISH_ORDER, expressOrder);
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
            inflater.inflate(R.menu.express_order_list, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_express:
                startActivityForResult(new Intent(getActivity(), PostExpressActivity.class), REQUEST_CODE_POST_EXPRESS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_POST_EXPRESS:
                if (resultCode == RESULT_CODE_POST_EXPRESS_SUCCESS) {
                    ExpressOrder expressOrder = (ExpressOrder) data.getSerializableExtra(EXPRESS_ORDER);
                    expressOrderList.add(0, expressOrder);
                    expressOrderListAdapter.refresh();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
