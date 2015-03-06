package com.cisoft.shop;

import android.app.Application;

import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.util.IOUtil;

import org.kymjs.aframe.utils.PreferenceHelper;

/**
 * Created by Lecion on 12/4/14.
 */
public class MyApplication extends Application {
    Shop shop = null;

    public Shop getShop() {
        if (shop == null) {
            String str = PreferenceHelper.readString(this, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_OBJ, null);
            shop = IOUtil.decode(str);
        }
        return shop;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
