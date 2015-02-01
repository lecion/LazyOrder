package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 用于解决根据ListView item数量计算ListView高度时,item高度不一致的问题
 * Created by comet on 2014/12/9.
 */
public class CompatListView extends ListView {

    public CompatListView(Context context) {
        super(context);
    }

    public CompatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatListView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
