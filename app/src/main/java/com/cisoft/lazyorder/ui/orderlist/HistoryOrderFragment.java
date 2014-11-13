package com.cisoft.lazyorder.ui.orderlist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.orderlist.HistoryOrder;
import com.cisoft.lazyorder.core.orderlist.HistoryOrderService;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.MyListView;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderFragment extends BaseFragment {


    @BindView(id = R.id.lvHistoryOrderList)
    private MyListView lvHistoryOrderList;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    private HistoryOrderService hisOrderService;
    private List<HistoryOrder> hisOrderList = new ArrayList<HistoryOrder>();
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
        hisOrderService = new HistoryOrderService(getActivity());
    }


    @Override
    protected void initWidget(View parentView) {
        showLoadingTip();
        initHisOrderList();
    }


    private void initHisOrderList(){
        hisOrderListAdapter = new HistoryOrderListAdapter(getActivity(), hisOrderList);
        lvHistoryOrderList.setAdapter(hisOrderListAdapter);
        lvHistoryOrderList.setPullLoadEnable(true);
        lvHistoryOrderList.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                hisOrderService.loadHistoryOrderData(userPhone, page, pager, new HistoryOrderService.OnHistoryOrderLoadFinish() {
                    @Override
                    public void onSuccess(List<HistoryOrder> historyOrders) {
                        lvHistoryOrderList.stopRefreshData();

                        if (historyOrders.size() == 0) {
                            showNoValueTip();
                        } else {
                            hisOrderListAdapter.clearAll();
                            hisOrderListAdapter.addData(historyOrders);
                            hisOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(hisOrderService.getResponseStateInfo(stateCode));
                        lvHistoryOrderList.stopRefreshData();
                        showNoValueTip();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                hisOrderService.loadHistoryOrderData(userPhone, page, pager, new HistoryOrderService.OnHistoryOrderLoadFinish() {
                    @Override
                    public void onSuccess(List<HistoryOrder> historyOrders) {
                        lvHistoryOrderList.stopRefreshData();

                        if (historyOrders.size() == 0) {
                            ViewInject.toast("没有更多店家数据了");
                            lvHistoryOrderList.setPullLoadEnable(false);
                        } else {
                            hisOrderListAdapter.addData(historyOrders);
                            hisOrderListAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode) {
                        ViewInject.toast(hisOrderService.getResponseStateInfo(stateCode));
                        lvHistoryOrderList.stopRefreshData();
                    }
                });
            }
        });

        hisOrderService.loadHistoryOrderData(userPhone, page, pager, new HistoryOrderService.OnHistoryOrderLoadFinish() {
            @Override
            public void onSuccess(List<HistoryOrder> historyOrders) {
                hideLoadingTip();

                if (historyOrders.size() == 0) {
                    showNoValueTip();
                } else {
                    hisOrderListAdapter.clearAll();
                    hisOrderListAdapter.addData(historyOrders);
                    hisOrderListAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(hisOrderService.getResponseStateInfo(stateCode));
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
}
