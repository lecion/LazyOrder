package com.cisoft.shop.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.cisoft.shop.MyApplication;
import com.cisoft.shop.bean.Shop;

/**
 * Created by Lecion on 2/20/15.
 */
public class L {
    public static MyApplication app(Activity aty) {
        return (MyApplication) aty.getApplication();
    }

    public static MyApplication app(Fragment fragment) {
        return (MyApplication) fragment.getActivity().getApplication();
    }

    public static MyApplication app(Context ctx) {
        return app((Activity)ctx);
    }

    public static Shop getShop(Activity aty) {
        return app(aty).getShop();
    }

    public static Shop getShop(Fragment fragment) {
        return app(fragment).getShop();
    }

    public static Shop getShop(Context ctx) {
        return app(ctx).getShop();
    }
}
