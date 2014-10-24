package com.cisoft.lazyorder.ui.goods;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.SearchView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.search.SearchActivity;
import com.cisoft.lazyorder.ui.shop.ShopActivity;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class GoodsActivity extends BaseActivity implements GoodsFragment.OnFragmentInteractionListener{

    @BindView(id = R.id.fl_container)
    private FrameLayout flContainer;
    @BindView(id = R.id.rb_pop)
    private RadioButton rbPop;
    @BindView(id = R.id.rb_price)
    private RadioButton rbPrice;


    public GoodsActivity() {
        super();
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_goods);

    }

    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        //使应用程序能够在当前应用程序向上导航
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  返回首页");

    }


    @Override
    protected void initWidget() {
        super.initWidget();
        initActionBar();
        showFragment(GoodsFragment.ORDER_POP);
        rbPop.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.rb_pop:
                showFragment(GoodsFragment.ORDER_POP);
                break;
            case R.id.rb_price:
                showFragment(GoodsFragment.ORDER_PRICE);
                break;
        }

    }


    private void showFragment(String type) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment show = getFragmentManager().findFragmentByTag(type);
        Fragment hide = getFragmentManager().findFragmentByTag(hideType(type));
        Log.d("showFragment", type + " show=>" + show + " hide=>"+hide);
        if (show == null) {
            show = GoodsFragment.newInstance(type);
            ft.add(R.id.fl_container, show, type);
        }
        if (hide == null) {
            hide = GoodsFragment.newInstance(hideType(type));
            ft.add(R.id.fl_container, hide, hideType(type));
        }
        ft.hide(hide).show(show).commit();
    }

    public String hideType(String type) {
        return GoodsFragment.ORDER_POP.equals(type) ? GoodsFragment.ORDER_PRICE : GoodsFragment.ORDER_POP;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.goods, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        KJLoger.debug("searchView: " + searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ShopActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case R.id.action_search:
                showActivity(this, SearchActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
