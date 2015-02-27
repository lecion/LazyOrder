package com.cisoft.shop.order.model;

import com.cisoft.shop.bean.Order;
import com.cisoft.shop.goods.model.INetWorkFinished;

/**
 * Created by Lecion on 12/4/14.
 */
public interface IOrderModel {
    void loadOrderList(int page, int size, INetWorkFinished<Order> finishedListener);

    void findOrdersByMerId(String orderState, int page, int size, INetWorkFinished<Order> finishedListener);

    void updateOrderState(int orderId, String state, OrderModel.IUpdateOrderState finishedListener);
}
