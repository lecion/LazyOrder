package com.cisoft.lazyorder.ui.main.menu;

import com.cisoft.lazyorder.R;

/**
 * Created by comet on 2015/4/17.
 */
public enum DrawerMenuItem {

    MENU_SHOP(R.drawable.selector_drawer_icon_order_goods, R.string.menu_shop, "com.cisoft.lazyorder.ui.shop.ShopFragment"),
    MENU_ORDER_LIST(R.drawable.selector_drawer_icon_order_list, R.string.menu_order_list, "com.cisoft.lazyorder.ui.order.OrderListFragment"),
    MENU_EXPRESS(R.drawable.selector_drawer_icon_order_goods, R.string.menu_express, "com.cisoft.lazyorder.ui.express.ExpressListFragment");

    private int titleId;
    private String fragName;
    private int iconId;

    private DrawerMenuItem (int iconId, int titleId, String fragName) {
        this.iconId = iconId;
        this.titleId = titleId;
        this.fragName = fragName;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getFragName() {
        return fragName;
    }

    public void setFragName(String fragName) {
        this.fragName = fragName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
