package com.cisoft.lazyorder.ui.search;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.core.goods.INetWorkFinished;
import com.cisoft.lazyorder.core.search.SearchService;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/17/14.
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    @BindView(id = R.id.lv_search_results)
    private ListView lvSearchResult;

    @BindView(id = R.id.search_view)
    private SearchView searchView;

    private int shopId;

    /**
     * 是否是搜索商店的，否则是商品搜索页面
     */
    private boolean isShop;

    /**
     * 存放商品搜索结果
     */
    private List<Goods> goodsList;

    /**
     * 存放商店搜索结果
     */
    private List<Shop> shopList;

    private SearchService searchService;

    public SearchActivity() {
        setHiddenActionBar(false);
    }
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initData() {
        Bundle data = getIntent().getExtras();
        shopId = data.getInt(ApiConstants.KEY_MER_ID, 0);
        isShop = shopId == 0;
        if (isShop) {
            shopList = new ArrayList<Shop>();
        } else {
            goodsList = new ArrayList<Goods>();
        }
        searchService = new SearchService();
    }

    @Override
    protected void initWidget() {
        initActionBar();
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        //使应用程序能够在当前应用程序向上导航
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(LayoutInflater.from(this).inflate(R.layout.searchview_actionbar, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        initSearchView();
    }

    /**
     * 初始化SearchView
     */
    private void initSearchView() {
        //设置自动展开
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        //设置提交图标
        int searchPlateId = getResources().getIdentifier("android:id/submit_area", null, null);
        LinearLayout searchPlate = (LinearLayout) searchView.findViewById(searchPlateId);
        ((ImageView)searchPlate.getChildAt(0)).setImageResource(R.drawable.ic_search);
        //设置输入框的颜色和提示字体的颜色
        int id = getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        //设置关闭按钮的图标
        int closeBtnId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeBtn = (ImageView)searchView.findViewById(closeBtnId);
        closeBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

//
//        int searchButtonId = getResources().getIdentifier("android:id/search_button", null, null);
//        ImageView searchButton = (ImageView) searchView.findViewById(searchButtonId);
//        searchButton.setBackgroundResource(R.drawable.ic_launcher);
//        searchButton.setImageResource(R.drawable.ic_search);
        //下面四行清除放大镜
        int magIconId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) searchView.findViewById(magIconId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        searchView.setIconifiedByDefault(false);

        setSearchViewBackground(searchView);

        searchView.setOnQueryTextListener(this);
    }

    /**
     * 设置SearchView背景
     * @param searchView
     */
    public void setSearchViewBackground(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.parseColor("#e60012"));

            Field submitAredField = argClass.getDeclaredField("mSubmitArea");
            submitAredField.setAccessible(true);
            View submitView = (View)submitAredField.get(searchView);
            submitView.setBackgroundColor(Color.parseColor("#e60012"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
