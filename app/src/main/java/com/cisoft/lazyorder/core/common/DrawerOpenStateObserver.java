package com.cisoft.lazyorder.core.common;

import java.util.ArrayList;
import java.util.List;

/*
    * 抽屉菜单打开状态的观察者
    *
    */
public class DrawerOpenStateObserver {

    private List<I_DrawerOpenStateObserver> observers;
    private static DrawerOpenStateObserver instance;

    private DrawerOpenStateObserver(){
        observers = new ArrayList<I_DrawerOpenStateObserver>();
    }

    public static DrawerOpenStateObserver getInstance(){
        if (instance == null) {
            instance = new DrawerOpenStateObserver();
        }

        return instance;
    }

    public void attach(I_DrawerOpenStateObserver observer) {
        if(observer != null) {
            observers.add(observer);
        }
    }

    public void detach(I_DrawerOpenStateObserver observer) {
        if(observer != null) {
            observers.remove(observer);
        }
    }


    public void notifyStateChanged(boolean isOpenState) {
        for (I_DrawerOpenStateObserver observer : observers) {
            if (observer != null) {
                observer.onStateChange(isOpenState);
            }
        }
    }
}

