package com.cisoft.shop.order.presenter;

import android.content.Context;
import android.util.Log;

import com.cisoft.shop.bean.Order;
import com.cisoft.shop.goods.model.INetWorkFinished;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.order.model.IOrderModel;
import com.cisoft.shop.order.model.OrderModel;
import com.cisoft.shop.order.view.IOrderView;

import org.kymjs.aframe.ui.ViewInject;

import java.util.List;

/**
 * Created by Lecion on 12/4/14.
 */
public class OrderPresenter {
    IOrderView view;
    IOrderModel model;
    ShopModel shopModel;

    public OrderPresenter(Context context, IOrderView view) {
        this.view = view;
        model = new OrderModel(context);
        shopModel = new ShopModel(context);
    }

    public void onLoad(int type) {
        view.setPage(1);
        view.showProgress();
        model.findOrdersByOrderState("CREATE", 1, 5, new INetWorkFinished<Order>() {
            @Override
            public void onSuccess(List<Order> l) {
                view.setOrderList(l);
                view.hideProgress();
            }

            @Override
            public void onFailure(String info) {
                Log.d("findOrdersByOrderState", "failed");
                view.showNoData();
            }
        });
//        model.loadOrderList(1, 5, new INetWorkFinished<Order>() {
//            @Override
//            public void onSuccess(List<Order> l) {
//                view.hideProgress();
//                view.setOrderList(l);
//            }
//
//            @Override
//            public void onFailure(String info) {
//                view.hideProgress();
//                view.showNoData();
//            }
//        });
    }

    public void loadMore(int page, final int size) {
        model.loadOrderList(page, size, new INetWorkFinished<Order>() {
            @Override
            public void onSuccess(List<Order> l) {
                if (l.size() == 0 || l.size() < size) {
                    ViewInject.toast("已经加载完了~");
                } else {
                    view.setOrderList(l);
                }
                view.setOnLoadMore(false);
            }

            @Override
            public void onFailure(String info) {
                ViewInject.toast(info);
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
        shopModel.updateOperateState(newState, new ShopModel.IUpdateOperateState() {
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


//    public void switchGoodsStatus(final int position, final int state) {
//        final int result = state == 1 ? 0 : 1;
//        model.updateComState(result, new GoodsModel.IUpdateGoodsState(){
//
//            @Override
//            public void onSuccess(int code) {
//                view.setGoodsStatus(position, result);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                ViewInject.toast(msg);
//                //TODO 测试状态改变，记得删
//                view.setGoodsStatus(position, result);
//            }
//        });
//    }
}
