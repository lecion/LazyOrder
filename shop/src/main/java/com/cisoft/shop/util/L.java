package com.cisoft.shop.util;

import android.app.Activity;
import android.app.Fragment;

import com.cisoft.shop.MyApplication;

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
}
