package com.cisoft.lazyorder.ui.main;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.main.menu.BaseMenuItemFragment;
import com.cisoft.lazyorder.ui.main.menu.DrawerMenuFragment;
import com.cisoft.lazyorder.ui.main.menu.DrawerMenuItem;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.ViewInject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends KJActivity implements DrawerMenuFragment.NavigationDrawerCallbacks{

    private Map<DrawerMenuItem, BaseMenuItemFragment> fragments = new HashMap();
    private DrawerMenuFragment navDrawerFragment;
    private DrawerMenuItem currMenuItem;
    private CharSequence mTitle;
    private long lastKeyTime;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initWidget() {
        navDrawerFragment = (DrawerMenuFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mTitle = getTitle();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        currMenuItem = DrawerMenuItem.values()[position];
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, obtainFragment(currMenuItem))
                .commit();
    }

    public BaseMenuItemFragment obtainFragment(DrawerMenuItem menuItem) {
        BaseMenuItemFragment fragment = fragments.get(menuItem);
        if (fragment == null) {
            try {
                fragment = (BaseMenuItemFragment) Class.forName(menuItem.getFragName()).newInstance();
                fragments.put(menuItem, fragment);
            } catch (Exception e) {
                Log.e("TAG", "fail to create menu item fragment");
            }
        }
        return fragment;
    }

    public void onSectionAttached(int position) {
        int titleId = DrawerMenuItem.values()[position].getTitleId();
        mTitle = getString(titleId);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!navDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if (currMenuItem == DrawerMenuItem.MENU_SHOP) {
                if((System.currentTimeMillis() - lastKeyTime) > 2000){
                    lastKeyTime = System.currentTimeMillis();
                    ViewInject.toast(getString(R.string.toast_exit_app));
                } else {
                    finish();
                }
            } else {
                navDrawerFragment.selectItem(DrawerMenuItem.MENU_SHOP.ordinal());
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}