package com.cisoft.shop.goods.view;

import com.cisoft.shop.bean.Goods;
import com.cisoft.shop.bean.GoodsCategory;

import java.util.List;

/**
 * Created by Lecion on 12/4/14.
 */
public interface IGoodsView {
    /**
     * 营业状态被选择
     *
     * @param state 0关 1开
     */
    void setOperatingState(int state);

    /**
     * 设置商店列表
     *
     * @param goodsList
     */
    void setGoodsList(List<Goods> goodsList);

    void showProgress();

    void showNoData();

    void hideProgress();

    void setOnLoadMore(boolean flag);

    void setPage(int i);

    void setCategoryList(List<GoodsCategory> l);

    void setGoodsCategory(int type);

    void setGoodsStatus(int position, int code);
}
