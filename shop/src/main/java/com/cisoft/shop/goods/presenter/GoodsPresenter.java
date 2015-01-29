package com.cisoft.shop.goods.presenter;

import android.content.Context;

import com.cisoft.shop.bean.Goods;
import com.cisoft.shop.bean.GoodsCategory;
import com.cisoft.shop.goods.model.CategoryModel;
import com.cisoft.shop.goods.model.GoodsModel;
import com.cisoft.shop.goods.model.IGoodsModel;
import com.cisoft.shop.goods.model.INetWorkFinished;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.goods.view.IGoodsView;

import org.kymjs.aframe.ui.ViewInject;

import java.util.List;

/**
 * Created by Lecion on 12/4/14.
 */
public class GoodsPresenter{
    IGoodsView view;
    IGoodsModel model;
    CategoryModel categoryModel;
    ShopModel shopModel;

    public GoodsPresenter(Context context, IGoodsView view) {
        this.view = view;
        model = new GoodsModel(context);
        categoryModel = new CategoryModel(context);
        shopModel = new ShopModel(context);
    }

    public void onLoad(int type, String sortType) {
        view.setPage(1);
        view.showProgress();
        categoryModel.loadCateogryByShopId(new INetWorkFinished<GoodsCategory>() {
            @Override
            public void onSuccess(List<GoodsCategory> l) {
                view.setCategoryList(l);
            }

            @Override
            public void onFailure(String info) {
                ViewInject.toast(info);
            }
        });

        model.loadGoodsListByType(1, 5, type, sortType, new INetWorkFinished<Goods>() {
            @Override
            public void onSuccess(List<Goods> l) {
                view.hideProgress();
                view.setGoodsList(l);
            }

            @Override
            public void onFailure(String info) {
                view.hideProgress();
                view.showNoData();
            }
        });
    }

    public void loadMore(int page, final int size, int type, String sortType) {
        model.loadGoodsListByType(page, size, type, sortType, new INetWorkFinished<Goods>() {
            @Override
            public void onSuccess(List<Goods> l) {
                if (l.size() == 0 || l.size() < size) {
                    ViewInject.toast("已经加载完了~");
                } else {
                    view.setGoodsList(l);
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

    public void switchGoodsType(int type) {
        view.setGoodsCategory(type);
        view.setDefaultSort();
        this.onLoad(type, IGoodsView.SORT_SALES);
    }

    public void switchGoodsStatus(final int position, final int state) {
        final int result = state == 1 ? 0 : 1;
        model.updateComState(result, new GoodsModel.IUpdateGoodsState(){

            @Override
            public void onSuccess(int code) {
                view.setGoodsStatus(position, result);
            }

            @Override
            public void onFailure(String msg) {
                //TODO 临时处理
                ViewInject.toast(msg);
            }
        });
    }

    public void switchSortType(int type, String sortType) {
        view.setSortType(sortType);
        this.onLoad(type, sortType);
    }
}
