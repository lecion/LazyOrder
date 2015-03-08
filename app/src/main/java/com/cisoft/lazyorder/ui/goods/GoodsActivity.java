package com.cisoft.lazyorder.ui.goods;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.bean.goods.GoodsCategory;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.core.goods.CategoryNetwork;
import com.cisoft.lazyorder.core.goods.ISwitchType;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.cart.CartActivity;
import com.cisoft.lazyorder.ui.search.SearchActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.util.Utility;
import com.cisoft.lazyorder.widget.EmptyView;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.widget.RoundImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2015/2/26.
 */
public class GoodsActivity extends BaseActivity implements GoodsFragment.OnFragmentInteractionListener{

    @BindView(id = R.id.iv_shop_logo)
    private RoundImageView ivShopLogo;
    @BindView(id = R.id.tv_shop_name)
    private TextView tvShopName;
    @BindView(id = R.id.tv_shop_promotion_info)
    private TextView tvShopPromotionInfo;
    @BindView(id = R.id.rb_pop, click = true)
    private RadioButton rbPop;
    @BindView(id = R.id.rb_price, click = true)
    private RadioButton rbPrice;
    @BindView(id = R.id.btn_type, click = true)
    private Button btnType;
    @BindView(id = R.id.fl_container)
    private FrameLayout flContainer;
    @BindView(id = R.id.iv_cart_logo)
    private ImageView ivCartLogo;
    @BindView(id = R.id.tv_ordered_count)
    private TextView tvOrderedCount;
    @BindView(id = R.id.tv_ordered_price)
    private TextView tvOrderedPrice;
    @BindView(id = R.id.btn_go_settle, click = true)
    private Button btnGoSettle;
    private PopupWindow popupWindow;
    private ListView lvSwitchCategory;
    private EmptyView mEmptyView;
    private Dialog mWaitTipDialog;
    private Dialog clearCartDialog;

    private CategoryListAdapter categoryAdapter;
    private List<GoodsCategory> categoryList;
    private List<ISwitchType> switchTypeObservers;
    private CategoryNetwork categoryNetwork;
    private Shop shop;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_goods);
    }

    @Override
    public void initData() {
        shop = new Shop();
        Bundle bundle = getIntent().getExtras();
        shop.setAddress(bundle.getString(ApiConstants.KEY_MER_ADDRESS));
        shop.setId(bundle.getInt(ApiConstants.KEY_MER_ID, 1));
        shop.setPromotionInfo(bundle.getString(ApiConstants.KEY_MER_PROMOTION_INFO));
        shop.setName(bundle.getString(ApiConstants.KEY_MER_NAME));
        shop.setFaceImgUrl(bundle.getString(ApiConstants.KEY_MER_FACE_PIC));

        categoryNetwork = new CategoryNetwork(this);
        categoryList = new ArrayList<GoodsCategory>();
    }

    @Override
    public void initWidget() {
        Utility.getKjBitmapInstance().display(ivShopLogo, shop.getFaceImgUrl());
        tvShopName.setText(shop.getName());
        tvShopPromotionInfo.setText(shop.getPromotionInfo());
        mEmptyView = new EmptyView(this, flContainer);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                loadCategoryListData();
            }
        });
        initPopupWindow();
        loadCategoryListData();
        showFragment(GoodsFragment.ORDER_SALES_NUM);
    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.rb_pop:
                showFragment(GoodsFragment.ORDER_SALES_NUM);
                break;
            case R.id.rb_price:
                showFragment(GoodsFragment.ORDER_PRICE);
                break;
            case R.id.btn_type:
                popupWindow.showAsDropDown(v, DensityUtils.dip2px(this, -60), DensityUtils.dip2px(this, -10));
                break;
            case R.id.btn_go_settle:
                AppContext app = (AppContext) getApplication();
                GoodsCart goodsCart = app.getGoodsCart();
                if (goodsCart.getTotalCount() > 0) {
                    showActivity(this, CartActivity.class);
                } else {
                    Animation shakeAnimation = new TranslateAnimation(0, -20, 0, 0);
                    shakeAnimation.setDuration(350);
                    shakeAnimation.setInterpolator(new CycleInterpolator(9));
                    ivCartLogo.startAnimation(shakeAnimation);
                }
        }
    }

    private void initPopupWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_popup_switch_type, null);
        popupWindow = new PopupWindow(popView, DensityUtils.dip2px(this, 110), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_switch_goods_category));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        categoryAdapter = new CategoryListAdapter(this, categoryList);
        lvSwitchCategory = (ListView) popView.findViewById(R.id.lv_switch_type);
        lvSwitchCategory.setAdapter(categoryAdapter);
        lvSwitchCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                rbPop.toggle();
                showFragment(GoodsFragment.ORDER_SALES_NUM);
                int type = categoryList.get(position).getId();

                if (switchTypeObservers != null) {
                    for (ISwitchType observer : switchTypeObservers) {
                        observer.onSwitch(type);
                    }
                }
            }

        });
    }

    private void loadCategoryListData() {
        categoryNetwork.loadCateogryByShopId(shop.getId(), new CategoryNetwork.OnCategoryLoadCallback() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess(List<GoodsCategory> categories) {
                if (categories.size() == 0) {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                    closeWaitTip();
                } else {
                    categoryAdapter.clearAll();
                    categoryAdapter.addData(categories);
                    categoryAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                closeWaitTip();

                ViewInject.toast(errorMsg);
                if (stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        });
    }


    private void showFragment(String order) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment show = getFragmentManager().findFragmentByTag(order);
        Fragment hide = getFragmentManager().findFragmentByTag(toggleOrder(order));
        Log.d("showFragment", order + " show=>" + show + " hide=>" + hide);
        if (show == null) {
            //0表示全部商品
            show = GoodsFragment.newInstance(order, 0);
            ft.add(R.id.fl_container, show, order);
        }
        if (hide == null) {
            hide = GoodsFragment.newInstance(toggleOrder(order), 0);
            ft.add(R.id.fl_container, hide, toggleOrder(order));
        }
        ft.hide(hide).show(show).commit();
    }


    /**
     * 根据排序方式获得要隐藏的fragment
     * @param order
     * @return
     */
    public String toggleOrder(String order) {
        return GoodsFragment.ORDER_SALES_NUM.equals(order) ? GoodsFragment.ORDER_PRICE : GoodsFragment.ORDER_SALES_NUM;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goods, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        KJLoger.debug("searchView: " + searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Bundle data = new Bundle();
                data.putInt(ApiConstants.KEY_MER_ID, shop.getId());
                data.putString(ApiConstants.KEY_MER_NAME, shop.getName());
                data.putString(ApiConstants.KEY_MER_ADDRESS, shop.getAddress());
                showActivity(this, SearchActivity.class, data);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 添加观察者
     * @param observer
     */
    public void addSwitchTypeObserver(ISwitchType observer) {
        if (this.switchTypeObservers == null) {
            switchTypeObservers = new ArrayList<ISwitchType>();
        }
        switchTypeObservers.add(observer);
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
    @Override
    public void onAddToCart(final Goods goods) {
        AppContext app = (AppContext) getApplication();
        final GoodsCart goodsCart = app.getGoodsCart();
        if (!goodsCart.isSameShop(shop.getId())) {
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
    @Override
    public void onAddToCart(final Goods goods, int count) {
        AppContext app = (AppContext) getApplication();
        final GoodsCart goodsCart = app.getGoodsCart();
        if (!goodsCart.isSameShop(shop.getId())) {
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


    @Override
    public int[] getCartLocation() {
        int[] location = new int[2];
        ivCartLogo.getLocationOnScreen(location);
        location[0] = location[0] + ivCartLogo.getWidth() / 2;
        location[1] = location[1] + ivCartLogo.getHeight() / 2;
        return location;
    }

    public int getShopId() {
        return shop.getId();
    }

    public String getShopName() {
        return shop.getName();
    }

    public String getShopAddress() {
        return shop.getAddress();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCartView();
    }

    /**
     * 显示正在加载的等待提示对话框
     *
     */
    public void showWaitTip() {
        if (mWaitTipDialog == null)
            mWaitTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.toast_wait));
        mWaitTipDialog.show();
    }

    /**
     * 关闭正在加载的等待提示对话框
     *
     */
    public void closeWaitTip() {
        if (mWaitTipDialog != null && mWaitTipDialog.isShowing()) {
            mWaitTipDialog.dismiss();
        }
    }
}
