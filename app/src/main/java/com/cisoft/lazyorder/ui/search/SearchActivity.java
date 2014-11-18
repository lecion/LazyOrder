package com.cisoft.lazyorder.ui.search;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.core.goods.INetWorkFinished;
import com.cisoft.lazyorder.core.search.SearchService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.sureorder.SureOrderActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Lecion on 10/17/14.
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    @BindView(id = R.id.lv_search_results)
    private ListView lvSearchResult;

    @BindView(id = R.id.search_view)
    private SearchView searchView;

    @BindView(id = R.id.tv_ordered_count)
    private TextView tvOrderedCount;

    @BindView(id = R.id.tv_ordered_price)
    private TextView tvOrderedPrice;

    @BindView(id = R.id.btn_go_settle)
    private Button btnGoSettle;

    @BindView(id = R.id.iv_cart_logo)
    private ImageView ivCartLogo;

    private Dialog clearCartDialog;

    private int shopId;

    private String shopName;

    private String shopAdrress;

    private SearchResultAdapter mAdapter;

    /**
     * 没有数据的提示
     */
    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    /**
     * 加载数据的提示
     */
    private Dialog loadingTipDialog;

    private SearchService searchService;

    public SearchActivity() {
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_search);
        initActionBar();
    }

    @Override
    protected void initData() {
        Bundle data = getIntent().getExtras();
        shopId = data.getInt(ApiConstants.KEY_MER_ID, 0);
        shopName = data.getString(ApiConstants.KEY_MER_NAME);
        shopAdrress = data.getString(ApiConstants.KEY_MER_ADDRESS);
        searchService = new SearchService(this);
    }

    @Override
    protected void initWidget() {
        initSearchView();
        initResultListView();
        updateCartView();
        btnGoSettle.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_settle:
                AppContext app = (AppContext) getApplication();
                GoodsCart goodsCart = app.getGoodsCart();
                if (goodsCart.getTotalCount() > 0) {
                    skipActivity(this, SureOrderActivity.class);
                } else {
                    Animation shakeAnimation = new TranslateAnimation(0, -20, 0, 0);
                    shakeAnimation.setDuration(350);
                    shakeAnimation.setInterpolator(new CycleInterpolator(9));
                    ivCartLogo.startAnimation(shakeAnimation);
                }
        }

    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        //使应用程序能够在当前应用程序向上导航
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(LayoutInflater.from(this).inflate(R.layout.searchview_actionbar, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
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
        ((ImageView) searchPlate.getChildAt(0)).setImageResource(R.drawable.ic_search);
        //设置输入框的颜色和提示字体的颜色
        int id = getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        //设置关闭按钮的图标
        int closeBtnId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeBtn = (ImageView) searchView.findViewById(closeBtnId);
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
     * 初始化结果列表
     */
    private void initResultListView() {
        mAdapter = new SearchResultAdapter(this, shopId, shopName, shopAdrress);
        lvSearchResult.setAdapter(mAdapter);
    }

    /**
     * 设置SearchView背景
     *
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
            View submitView = (View) submitAredField.get(searchView);
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
        showLoadingTip();
        searchService.queryGoodsList(shopId, query, new INetWorkFinished<Goods>() {
            @Override
            public void onSuccess(List<Goods> l) {
                mAdapter.setData(l);
                hideLoadingTip();
            }

            @Override
            public void onFailure(String info) {
                mAdapter.clearData();
                showNoValueTip();
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * 显示加载数据的提示
     */
    public void showLoadingTip() {
        if (loadingTipDialog == null) {
            loadingTipDialog = DialogFactory.createToastDialog(this, "正在努力的找啊找啊找。。。");
        }
        if (!loadingTipDialog.isShowing()) {
            loadingTipDialog.show();
        }
        llShowNoValueTip.setVisibility(View.GONE);
    }

    /**
     * 隐藏加载数据的提示
     */
    public void hideLoadingTip() {
        llShowNoValueTip.setVisibility(View.GONE);
        if (loadingTipDialog != null && loadingTipDialog.isShowing()) {
            loadingTipDialog.dismiss();
        }
    }

    /**
     * 显示出没有数据的提示
     */
    public void showNoValueTip() {
        llShowNoValueTip.setVisibility(View.VISIBLE);
        if(loadingTipDialog !=null && loadingTipDialog.isShowing())
            loadingTipDialog.dismiss();
    }



    /**
     * 更新购物车
     */
    private void updateCartView() {
        AppContext app = (AppContext) getApplication();
        GoodsCart goodsCart = app.getGoodsCart();
        if (goodsCart.getTotalCount() == 0) {
            ivCartLogo.setImageResource(R.drawable.cart_logo_normal);
            tvOrderedCount.setText(goodsCart.getTotalCount() + "份");
            tvOrderedPrice.setText("￥" + goodsCart.getTotalPrice());
            tvOrderedCount.setVisibility(View.INVISIBLE);
            tvOrderedPrice.setVisibility(View.INVISIBLE);
            btnGoSettle.setText("未选择");
        } else {
            ivCartLogo.setImageResource(R.drawable.cart_logo_selected);
            tvOrderedCount.setText(goodsCart.getTotalCount() + "份");
            tvOrderedPrice.setText("￥" + goodsCart.getTotalPrice());
            tvOrderedCount.setVisibility(View.VISIBLE);
            tvOrderedPrice.setVisibility(View.VISIBLE);
            btnGoSettle.setText("去结算");
        }
    }

    /**
     * 加入购物车的回调
     * @param goods
     */
    public void onAddToCart(final Goods goods) {
        AppContext app = (AppContext) getApplication();
        final GoodsCart goodsCart = app.getGoodsCart();
        if (!goodsCart.isSameShop(shopId)) {
            if (clearCartDialog == null) {
                clearCartDialog = DialogFactory.createConfirmDialog(this, "发现不属于我家的宝贝，是否清空它们？", new DialogFactory.IConfirm() {
                    @Override
                    public void onYes() {
                        goodsCart.clear();
                        goodsCart.addGoods(goods);
                        updateCartView();
                    }
                });
            }
            clearCartDialog.show();
        } else {
            goodsCart.addGoods(goods);
            updateCartView();
        }
    }

    /**
     * 加入购物车回调
     * @param goods
     * @param count
     */
    public void onAddToCart(final Goods goods, int count) {
        AppContext app = (AppContext) getApplication();
        final GoodsCart goodsCart = app.getGoodsCart();
        if (!goodsCart.isSameShop(shopId)) {
            if (clearCartDialog == null) {
                clearCartDialog = DialogFactory.createConfirmDialog(this, "发现不属于我家的宝贝，是否清空它们？", new DialogFactory.IConfirm() {
                    @Override
                    public void onYes() {
                        goodsCart.clear();
                        goodsCart.addGoods(goods);
                        updateCartView();
                    }
                });
            }
            clearCartDialog.show();
        } else {
            goodsCart.addGoods(goods, count);
            updateCartView();
        }


    }

    public int[] getCartLocation() {
        int[] location = new int[2];
        ivCartLogo.getLocationOnScreen(location);
        location[0] = location[0] + ivCartLogo.getWidth() / 2;
        location[1] = location[1] + ivCartLogo.getHeight() / 2;
        return location;
    }
}
