package com.cisoft.lazyorder.ui.main.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

/**
 * Created by comet on 2014/11/22.
 */
public class DrawerMenuItemView extends MenuItemView {

    protected ImageView ivMenuIcon;
    protected TextView tvMenuTitle;
    protected TextView tvMenuMessage;

    public DrawerMenuItemView(Context context) {
        this(context, null, 0);
    }

    public DrawerMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerMenuItemView(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);

        LayoutInflater.from(context).inflate(R.layout.drawer_menu_item, this, true);

        ivMenuIcon = ((ImageView)findViewById(R.id.menu_icon));
        tvMenuTitle = ((TextView)findViewById(R.id.menu_title));
        tvMenuMessage = ((TextView)findViewById(R.id.menu_message));

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.drawMenuItemAttrs);
        Drawable menuIcon = localTypedArray.getDrawable(R.styleable.drawMenuItemAttrs_menu_icon);
        String menuName = localTypedArray.getString(R.styleable.drawMenuItemAttrs_menu_name);
        localTypedArray.recycle();
        ivMenuIcon.setImageDrawable(menuIcon);
        tvMenuTitle.setText(menuName);
    }
}
