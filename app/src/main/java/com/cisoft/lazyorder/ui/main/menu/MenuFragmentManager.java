package com.cisoft.lazyorder.ui.main.menu;

import android.content.Context;
import com.cisoft.lazyorder.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2015/2/26.
 */
public class MenuFragmentManager {

    private static MenuFragmentManager instance;
    public List<MenuItem> mMenuItems;
    private Context mContext;

    private MenuFragmentManager(Context context) {
        mContext = context;
        initMenuItems();
    }

    public static MenuFragmentManager getInstance(Context context) {
        if (instance == null) {
            instance = new MenuFragmentManager(context);
        }

        return instance;
    }

    private void initMenuItems() {
        mMenuItems = new ArrayList<MenuItem>();
        MenuItem menuItem;

        menuItem = new MenuItem();
        menuItem.position = 0;
        menuItem.iconResId = R.drawable.selector_drawer_icon_order_goods;
        menuItem.title = "我要订餐";
        menuItem.fragment = createMenuItemFragment("com.cisoft.lazyorder.ui.shop.ShopFragment");
        mMenuItems.add(menuItem.position, menuItem);

        menuItem = new MenuItem();
        menuItem.position = 1;
        menuItem.iconResId = R.drawable.selector_drawer_icon_order_list;
        menuItem.title = "订单中心";
        menuItem.fragment = createMenuItemFragment("com.cisoft.lazyorder.ui.order.OrderListFragment");
        mMenuItems.add(menuItem.position, menuItem);

        menuItem = new MenuItem();
        menuItem.position = 2;
        menuItem.iconResId = R.drawable.selector_drawer_icon_order_list;
        menuItem.title = "代领快递";
        menuItem.fragment = createMenuItemFragment("com.cisoft.lazyorder.ui.express.ExpressListFragment");
        mMenuItems.add(menuItem.position, menuItem);
    }


    public MenuItem get(int position) {
        return mMenuItems.get(position);
    }

    private BaseMenuItemFragment createMenuItemFragment(String className) {
        BaseMenuItemFragment menuItemFragment = null;
        try {
            menuItemFragment = (BaseMenuItemFragment) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return menuItemFragment;
    }


    public class MenuItem {
        public int position;
        public int iconResId;
        public String title;
        public BaseMenuItemFragment fragment;
    }
}
