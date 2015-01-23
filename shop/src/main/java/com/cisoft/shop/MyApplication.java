package com.cisoft.shop;

import android.app.Application;

import com.cisoft.shop.bean.Shop;

/**
 * Created by Lecion on 12/4/14.
 */
public class MyApplication extends Application {

    public Shop getShop() {
        return new Shop(1, "叮当超市", "11：00", "14:00", 777, "http://lecion.qiniudn.com/qjrs.jpeg", "第二份半价", "堕落街A区");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
