package com.cisoft.lazyorder.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by comet on 2014/11/22.
 */
public class EmptyViewManager {


    /**
     * 展示空视图
     * @param linearLayout
     * @param emptyView
     */
    public static void showEmptyView(ViewGroup linearLayout, View emptyView) {
        if (emptyView == null)
            return;

        if (emptyView.getParent() != null)
            ((ViewGroup)emptyView.getParent()).removeView(emptyView);

        Object layoutParamsObject = null;
        if (emptyView.getLayoutParams() != null) {
            layoutParamsObject = emptyView.getLayoutParams();
            linearLayout.removeAllViews();
            linearLayout.addView(emptyView, (ViewGroup.LayoutParams) layoutParamsObject);
        } else {
            linearLayout.addView(emptyView);
        }
    }
}
