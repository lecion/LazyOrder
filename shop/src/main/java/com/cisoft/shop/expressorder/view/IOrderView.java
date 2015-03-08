package com.cisoft.shop.expressorder.view;

import com.cisoft.shop.bean.ExpressOrder;

import java.util.List;

/**
 * Created by Lecion on 12/4/14.
 */
public interface IOrderView {
    /**
     * 营业状态被选择
     *
     * @param state 0关 1开
     */
    void setOperatingState(int state);

    /**
     * 设置商店列表
     *
     * @param orderList
     */
    void setOrderList(List<ExpressOrder> orderList);

    void showProgress();

    void showNoData();

    void hideProgress();

    void setOnLoadMore(boolean flag);

    void setPage(int i);

    void setOrderStatus(int position, String state);

    void setPullLoadEnable(boolean flag);

    void showMoreProgress();

    void hideMoreProgress();
}
