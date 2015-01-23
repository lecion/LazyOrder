package com.cisoft.lazyorder.ui.main;

import android.app.ActionBar;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import com.cisoft.lazyorder.R;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity{

    private DrawMenuFragment navDrawerFragment;

    public MainActivity(){
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initWidget() {
        initNavDrawer();
    }

    /**
     * 初始化导航抽屉
     */
    private void initNavDrawer(){
        navDrawerFragment = (DrawMenuFragment)
                getFragmentManager().findFragmentById(R.id.drawer_menu_fragment);

        navDrawerFragment.setUp(
                R.id.drawer_menu_fragment,
                (DrawerLayout) findViewById(R.id.drawe_menu_layout));
    }

    /**
     * 重置ActionBar
     */
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(navDrawerFragment.isNavDrawerOpen()) {
            restoreActionBar();
        }

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}