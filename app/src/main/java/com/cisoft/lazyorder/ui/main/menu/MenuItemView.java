package com.cisoft.lazyorder.ui.main.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cisoft.lazyorder.R;

/**
 * Created by comet on 2014/11/22.
 */

public abstract class MenuItemView extends LinearLayout{

    private String mContentClass;

    public MenuItemView(Context context) {
        this(context, null, 0);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.drawer_menu_item_bg);
        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.drawMenuItemAttrs);
        mContentClass = localTypedArray.getString(R.styleable.drawMenuItemAttrs_content_class);
        localTypedArray.recycle();
    }

    public String getContentClass() {
        return mContentClass;
    }

    public void setContentClass(String contentClass) {
        mContentClass = contentClass;
    }
}