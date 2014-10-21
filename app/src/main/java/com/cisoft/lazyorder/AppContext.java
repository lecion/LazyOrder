package com.cisoft.lazyorder;

import android.app.Application;

import org.kymjs.aframe.CrashHandler;
import org.kymjs.aframe.KJLoger;

/**
 * Created by comet on 2014/10/22.
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.create(this);
        KJLoger.IS_DEBUG = AppConfig.IS_DEBUG;
    }
}
