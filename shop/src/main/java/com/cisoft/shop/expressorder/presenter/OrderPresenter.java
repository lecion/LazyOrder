package com.cisoft.shop.expressorder.presenter;

import android.content.Context;

import com.cisoft.shop.Api;
import com.cisoft.shop.bean.ExpressOrder;
import com.cisoft.shop.expressorder.model.ExpressModel;
import com.cisoft.shop.expressorder.view.IOrderView;
import com.cisoft.shop.goods.model.INetWorkFinished;
import com.cisoft.shop.order.model.OrderModel;
import com.cisoft.shop.welcome.model.ExpmerModel;

import org.kymjs.aframe.ui.ViewInject;

import java.util.List;

/**
 * Created by Lecion on 12/4/14.
 */
public class OrderPresenter {
    IOrderView view;
    ExpressModel model;
    ExpmerModel expmerModel;

    public OrderPresenter(Context context, IOrderView view) {
        this.view = view;
        model = new ExpressModel(context);
        expmerModel = new ExpmerModel(context);
    }

    public void onLoad() {
        final int size = 5;
        view.setPage(1);
        view.showProgress();
        model.findExpressByState(Api.ORDER_STATE_CREATE, 1, size, new INetWorkFinished<ExpressOrder>() {
            @Override
            public void onSuccess(List<ExpressOrder> l) {
                if (l.size() == 0 || l.size() < size) {
                    view.setPullLoadEnable(false);
                } else {
                    view.setPullLoadEnable(true);
                }
                view.setOrderList(l);
                view.hideProgress();
                view.hideNewMsg();
            }

            @Override
            public void onFailure(String info) {
                view.showNoData();
            }
        });
    }

    public void loadMore(int page, final int size) {
        model.findExpressByState(Api.ORDER_STATE_CREATE, page, size, new INetWorkFinished<ExpressOrder>() {
            @Override
            public void onSuccess(List<ExpressOrder> l) {
                if (l.size() == 0 || l.size() < size) {
                    view.setPullLoadEnable(false);
                    ViewInject.toast("已经加载完了~");
                } else {
                    view.setOrderList(l);
                }
                view.hideMoreProgress();
                view.setOnLoadMore(false);
            }

            @Override
            public void onFailure(String info) {
                ViewInject.toast(info);
                view.hideMoreProgress();
                view.setOnLoadMore(false);
            }
        });
    }

    /**
     * 切换商店营业状态
     * @param oldState 切换之前的状态
     * @param newState 切换之后的状态
     */
    public void switchShopState(final int oldState, final int newState) {
        if (oldState == newState) {
            return;
        }
        expmerModel.updateOperateState(newState, new ExpmerModel.IUpdateOperateState() {
            @Override
            public void onSuccess(int code) {
                view.setOperatingState(newState);
            }

            @Override
            public void onFailure(String msg) {
                view.setOperatingState(oldState);
                ViewInject.toast(msg);
            }
        });
    }

    /**
     * 切换订单状态
     * @param orderId
     * @param position
     * @param state
     */
    public void switchOrderStatus(int orderId, final int position, final String state) {

        model.updateExpressStatue(orderId, state, new ExpressModel.IUpdateOrderState() {
            @Override
            public void onSuccess(int code) {
                view.setOrderStatus(position, state);
            }

            @Override
            public void onFailure(String msg) {
                ViewInject.toast(msg);
            }
        });
    }

    /**
     * 取消订单
     * @param orderId   订单id
     * @param position  取消订单在listView中的position
     */
    public void cancelOrder(int orderId, final int position) {
        model.updateOrderState(orderId, "CANCEL", new OrderModel.IUpdateOrderState() {
            @Override
            public void onSuccess(int code) {
                view.dismissView(position);
            }

            @Override
            public void onFailure(String msg) {
                ViewInject.toast(msg);
            }
        });
    }

}
