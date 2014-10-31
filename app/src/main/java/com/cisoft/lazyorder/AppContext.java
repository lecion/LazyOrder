package com.cisoft.lazyorder;

import android.app.Application;

import com.cisoft.lazyorder.bean.goods.GoodsCart;

import org.kymjs.aframe.KJLoger;

/**
 * Created by comet on 2014/10/22.
 */
public class AppContext extends Application {

    /**
     * 获取购物车
     */
    public GoodsCart getGoodsCart() {
        return GoodsCart.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.create(this);
        KJLoger.IS_DEBUG = AppConfig.IS_DEBUG;
    }

}
