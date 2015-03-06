package com.cisoft.lazyorder.ui.main;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.main.menu.DrawerMenuFragment;
import com.cisoft.lazyorder.ui.main.menu.MenuFragmentManager;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.ViewInject;

public class MainActivity extends KJActivity implements DrawerMenuFragment.NavigationDrawerCallbacks{

    private DrawerMenuFragment navDrawerFragment;
    private MenuFragmentManager.MenuItem currMenuItem;
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
        currMenuItem = MenuFragmentManager.getInstance(this).get(position);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, currMenuItem.fragment)
                .commit();
    }

    public void onSectionAttached(int position) {
        mTitle = MenuFragmentManager.getInstance(this)
                .get(position).title;
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
            if (currMenuItem.position == 0) {
                if((System.currentTimeMillis() - lastKeyTime) > 2000){
                    lastKeyTime = System.currentTimeMillis();
                    ViewInject.toast("再按一次退出程序");
                }else{
                    finish();
                }
            } else {
                navDrawerFragment.selectItem(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}