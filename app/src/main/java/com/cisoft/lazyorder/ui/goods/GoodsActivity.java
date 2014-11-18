package com.cisoft.lazyorder.ui.goods;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
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
import android.widget.BaseAdapter;
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
import com.cisoft.lazyorder.core.goods.CategoryService;
import com.cisoft.lazyorder.core.goods.INetWorkFinished;
import com.cisoft.lazyorder.core.goods.ISwitchType;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.search.SearchActivity;
import com.cisoft.lazyorder.ui.sureorder.SureOrderActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends BaseActivity implements GoodsFragment.OnFragmentInteractionListener{

    @BindView(id = R.id.iv_shop_logo)
    private ImageView ivShopLogo;

    @BindView(id = R.id.tv_shop_name)
    private TextView tvShopName;

    @BindView(id = R.id.tv_shop_promotion_info)
    private TextView tvShopPromotionInfo;

    @BindView(id = R.id.fl_container)
    private FrameLayout flContainer;

    @BindView(id = R.id.rb_pop)
    private RadioButton rbPop;

    @BindView(id = R.id.rb_price)
    private RadioButton rbPrice;

    @BindView(id = R.id.btn_type)
    private Button btnType;

    @BindView(id = R.id.tv_ordered_count)
    private TextView tvOrderedCount;

    @BindView(id = R.id.tv_ordered_price)
    private TextView tvOrderedPrice;

    @BindView(id = R.id.btn_go_settle)
    private Button btnGoSettle;

    @BindView(id = R.id.iv_cart_logo)
    private ImageView ivCartLogo;

    private ListView lvSwitchCategory;

    private List<GoodsCategory> categoryList;

    private BaseAdapter categoryAdapter;

    private PopupWindow popupWindow;

    public static final String KEY_SHOP_ID = ApiConstants.KEY_MER_ID;

    //private int shopId = 1;

    private CategoryService categoryService;

    private Shop shop;

    private Dialog clearCartDialog;

    /**
     * 切换商品观察者
     */
    private List<ISwitchType> switchTypeObservers;

    public GoodsActivity() {
        super();
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_goods);
        shop = new Shop();
        Bundle bundle = getIntent().getExtras();
        shop.setAddress(bundle.getString(ApiConstants.KEY_MER_ADDRESS));
        shop.setId(bundle.getInt(KEY_SHOP_ID, 1));
        shop.setPromotionInfo(bundle.getString(ApiConstants.KEY_MER_PROMOTION_INFO));
        shop.setName(bundle.getString(ApiConstants.KEY_MER_NAME));
        shop.setFaceImgUrl(bundle.getString(ApiConstants.KEY_MER_FACE_PIC));
        //shopId = getIntent().getExtras().getInt(KEY_SHOP_ID, 1);
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
    protected void initData() {
        categoryService = new CategoryService(this, ApiConstants.MODULE_COM_CATEGORY);
        categoryList = new ArrayList<GoodsCategory>();
        //初始化商品类别
        categoryService.loadCateogryByShopId(shop.getId(), new INetWorkFinished<GoodsCategory>() {
            @Override
            public void onSuccess(List<GoodsCategory> l) {
                setCateogryData(l);
            }

            @Override
            public void onFailure(String info) {
                ViewInject.toast(info);
            }
        });
        Log.d("shoplogo", shop.getFaceImgUrl());
        KJBitmap.create().display(ivShopLogo, shop.getFaceImgUrl(), BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        tvShopName.setText(shop.getName());
        tvShopPromotionInfo.setText(shop.getPromotionInfo());
        updateCartView();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initActionBar();
        initPopupWindow();
        showFragment(GoodsFragment.ORDER_SALES_NUM);
        rbPop.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
        btnType.setOnClickListener(this);
        btnGoSettle.setOnClickListener(this);
    }

    private void initPopupWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_popup_switch_type, null);
        initCategoryList(popView);
        popupWindow = new PopupWindow(popView, DensityUtils.dip2px(this, 110), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_switch_goods_category));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

    /**
     * 初始化分类列表
     * @param popView
     */
    private void initCategoryList(View popView) {
        categoryAdapter = new BaseAdapter(){
            @Override
            public int getCount() {
                return categoryList.size();
            }

            @Override
            public Object getItem(int position) {
                return categoryList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return categoryList.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(GoodsActivity.this).inflate(R.layout.activity_goods_category_list_cell, parent, false);
                }
                TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tv_category_name);
                GoodsCategory category = (GoodsCategory)getItem(position);
                tvCategoryName.setText(category.getCateName());
                KJLoger.debug("getView " + category.getCateName());
                return convertView;
            }
        };
        lvSwitchCategory = (ListView) popView.findViewById(R.id.lv_switch_type);
        lvSwitchCategory.setAdapter(categoryAdapter);
        lvSwitchCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                rbPop.toggle();
                showFragment(GoodsFragment.ORDER_SALES_NUM);
                int type = getCategoryIdByPosition(position);

                if (switchTypeObservers != null) {
                    for (ISwitchType observer : switchTypeObservers) {
                        observer.onSwitch(type);
                    }
                }

//                ViewInject.toast(position + "");
//                ((GoodsFragment) getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_SALES_NUM)).init(1);
//                ((GoodsFragment) getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_PRICE)).init(1);
            }

            private int getCategoryIdByPosition(int position) {
                return categoryList.get(position).getId();
            }
        });
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
                    skipActivity(this, SureOrderActivity.class);
                } else {
                    Animation shakeAnimation = new TranslateAnimation(0, -20, 0, 0);
                    shakeAnimation.setDuration(350);
                    shakeAnimation.setInterpolator(new CycleInterpolator(9));
                    ivCartLogo.startAnimation(shakeAnimation);
                }
        }

    }

    private void showFragment(String order) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment show = getFragmentManager().findFragmentByTag(order);
        Fragment hide = getFragmentManager().findFragmentByTag(hideOrder(order));
        Log.d("showFragment", order + " show=>" + show + " hide=>"+hide);
        if (show == null) {
            //0表示全部商品
            show = GoodsFragment.newInstance(order, 0);
            ft.add(R.id.fl_container, show, order);
        }
        if (hide == null) {
            hide = GoodsFragment.newInstance(hideOrder(order), 0);
            ft.add(R.id.fl_container, hide, hideOrder(order));
        }
        ft.hide(hide).show(show).commit();
    }

    /**
     * 根据排序方式获得要隐藏的fragment
     * @param order
     * @return
     */
    public String hideOrder(String order) {
        return GoodsFragment.ORDER_SALES_NUM.equals(order) ? GoodsFragment.ORDER_PRICE : GoodsFragment.ORDER_SALES_NUM;
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
                /*Intent i = new Intent(this, ShopActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
                finish();
                return true;
            case R.id.action_search:
                Bundle data = new Bundle();
                data.putInt(ApiConstants.KEY_MER_ID, shop.getId());
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

    public void setCateogryData(List<GoodsCategory> goodsCategoryList) {
        this.categoryList = goodsCategoryList;
        categoryAdapter.notifyDataSetChanged();
    }

}
