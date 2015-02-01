package com.cisoft.lazyorder.ui.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.main.menu.DrawerMenuHeaderView;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;
import com.cisoft.lazyorder.ui.main.menu.MenuItemView;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.FrameFragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by comet on 2014/11/23.
 */
public class DrawMenuFragment extends FrameFragment {

    @BindView(id = R.id.drawer_header, click = true)
    private DrawerMenuHeaderView drawerMenuHeader;

    @BindView(id = R.id.menu_container)
    private LinearLayout menuContainer;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mNavDrawerLayout;
    private View mDrawerMenuView;

    /* 用来存放已经创建了的菜单项对应的内
    * 容(fragment),解决重复初始化
    */
    private Map<String, MenuItemContent> mMenuItems = new HashMap<String, MenuItemContent>();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_drawer_menu, null, true);

        return view;
    }


    @Override
    protected void initWidget(View parentView) {
        for (int i = 0; i < menuContainer.getChildCount(); i++) {
            menuContainer.getChildAt(i).setOnClickListener(this);
        }

        //默认显示第一个菜单项的内容
        if (menuContainer.getChildCount() > 0)
            showMenuItemContent((MenuItemView) menuContainer.getChildAt(0));
        else
            throw new RuntimeException("please add menu item...");
    }


    @Override
    protected void widgetClick(View v) {
        mNavDrawerLayout.closeDrawer(mDrawerMenuView);

        showMenuItemContent((MenuItemView) v);
    }

    /**
     * 切换显示菜单项对应的内容(fragment)
     *
     * @param menuItemView
     */
    private void showMenuItemContent(MenuItemView menuItemView) {
        String contentClass = menuItemView.getContentClass();
        MenuItemContent menuItemContent = obtainMenuItemContent(contentClass);

        /* 先隐藏已经创建了的fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Iterator iterator = mMenuItems.entrySet().iterator();
        MenuItemContent tempContent = null;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            tempContent = (MenuItemContent) entry.getValue();
            fragmentTransaction.hide(tempContent);
        }

        /* 显示指定的fragment */
        fragmentTransaction.show(menuItemContent);
        fragmentTransaction.commit();
    }

    /**
     * 获取菜单项对应的内容(fragment)
     *
     * @param contentClass
     */
    private MenuItemContent obtainMenuItemContent(String contentClass) {
        MenuItemContent menuItemContent = null;

        /* 若该菜单对应的内容在集合中已存在,则直接取出 */
        if (mMenuItems.containsKey(contentClass)) {
            menuItemContent = mMenuItems.get(contentClass);

        /* 不存在则新建并添加进集合里 */
        } else {
            try {
                menuItemContent = (MenuItemContent) Class.forName(contentClass).newInstance();
                addMenuItemContent(contentClass, menuItemContent);
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return menuItemContent;
    }


    /**
     * 添加新的菜单项对应的内容(fragment)
     *
     * @param contentClass
     * @param menuItemContent
     */
    private void addMenuItemContent(String contentClass, MenuItemContent menuItemContent) {
        mMenuItems.put(contentClass, menuItemContent);
        getFragmentManager().beginTransaction().add(R.id.drawer_menu_item_content, menuItemContent).commit();
    }


    /**
     * 绑定上抽屉式导航菜单
     *
     * @param fragmentId
     * @param drawerMenuLayout
     */
    public void setUp(int fragmentId, DrawerLayout drawerMenuLayout) {
        mDrawerMenuView = getActivity().findViewById(fragmentId);
        mNavDrawerLayout = drawerMenuLayout;

        mNavDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mNavDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                invalidateOptionsMenu();
            }
        };
        mNavDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mNavDrawerLayout.setDrawerListener(mDrawerToggle);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 导航抽屉是否打开
     *
     * @return
     */
    public boolean isNavDrawerOpen() {
        return mNavDrawerLayout != null && mNavDrawerLayout.isDrawerOpen(mDrawerMenuView);
    }

    /**
     * 重置actionbar上的菜单项
     */
    private void invalidateOptionsMenu() {
        Iterator iterator = mMenuItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MenuItemContent tempFragment = (MenuItemContent) entry.getValue();
            tempFragment.setMenuOpenState(isNavDrawerOpen());
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置了此方法，系统才会去调用onCreateOptionsMenu方法
        setHasOptionsMenu(true);
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (isNavDrawerOpen()) {
            inflater.inflate(R.menu.drawer_menu, menu);
            restoreActionBar();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //使icon图标能够切换抽屉菜单的显示
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
