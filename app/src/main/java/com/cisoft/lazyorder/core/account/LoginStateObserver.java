package com.cisoft.lazyorder.core.account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/12/7.
 */
public class LoginStateObserver {

    private List<I_LoginStateObserver> observers;
    private static LoginStateObserver instance;

    private LoginStateObserver(){
        observers = new ArrayList<I_LoginStateObserver>();
    }

    public static LoginStateObserver getInstance(){
        if (instance == null) {
            instance = new LoginStateObserver();
        }

        return instance;
    }

    public void attach(I_LoginStateObserver observer) {
        if(observer != null) {
            observers.add(observer);
        }
    }

    public void detach(I_LoginStateObserver observer) {
        if(observer != null) {
            observers.remove(observer);
        }
    }


    public void notifyStateChanged() {
        for (I_LoginStateObserver observer : observers) {
            if (observer != null) {
                observer.onLoginSateChange();
            }
        }
    }
}

