package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Lecion on 11/4/14.
 */
public class ParentListView extends MyListView {
    public ParentListView(Context context) {
        super(context);
    }

    public ParentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
