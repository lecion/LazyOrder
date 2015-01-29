package com.cisoft.shop.goods.model;

import com.cisoft.shop.bean.Goods;

/**
 * Created by Lecion on 12/4/14.
 */
public interface IGoodsModel {
    void loadGoodsList(int page, int size, int type, String sortType, INetWorkFinished<Goods> finishedListener);
    void loadGoodsListByType(int page, int size, int type, String sortType, INetWorkFinished<Goods> finishedListener);
    void updateComState(int state, GoodsModel.IUpdateGoodsState iUpdateGoodsState);
}
