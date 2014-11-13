package com.cisoft.lazyorder.ui.main;

import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.cisoft.lazyorder.R;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity{

    private NavDrawerFragment navDrawerFragment;

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
        navDrawerFragment = (NavDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fmNavDrawerFragment);

        navDrawerFragment.setUp(
                R.id.fmNavDrawerFragment,
                (DrawerLayout) findViewById(R.id.dlNavDrawerLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
