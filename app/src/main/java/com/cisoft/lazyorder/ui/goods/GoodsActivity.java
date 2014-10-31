package com.cisoft.lazyorder.ui.goods;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.cisoft.lazyorder.core.goods.CategoryService;
import com.cisoft.lazyorder.core.goods.GoodsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.search.SearchActivity;
import com.cisoft.lazyorder.ui.shop.ShopActivity;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends BaseActivity implements GoodsFragment.OnFragmentInteractionListener{

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

    private ListView lvSwitchCategory;

    private List<GoodsCategory> categoryList;

    private BaseAdapter categoryAdapter;

    private PopupWindow popupWindow;

    public static final String KEY_SHOP_ID = "shop_id";

    private int shopId = 1;

    private CategoryService categoryService;

    private GoodsService goodsService;






    public GoodsActivity() {
        super();
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_goods);
        //shopId = getIntent().getExtras().getInt(KEY_SHOP_ID);
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
        goodsService = new GoodsService(this, ApiConstants.MODULE_COMMODITY);
        categoryList = new ArrayList<GoodsCategory>();
        categoryService.loadCateogryByShopIdFromNet(shopId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initActionBar();
        initPopupWindow();
        showFragment(GoodsFragment.ORDER_POP);
        rbPop.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
        btnType.setOnClickListener(this);
    }

    private void initPopupWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_popup_switch_type, null);
        initCategoryList(popView);
        popupWindow = new PopupWindow(popView, DensityUtils.dip2px(this, 110), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_switch_goods_category));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

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
                ViewInject.toast(position+"");
                ((GoodsFragment)getFragmentManager().findFragmentByTag("pop")).init(1);
                ((GoodsFragment)getFragmentManager().findFragmentByTag("price")).init(1);
                //showFragment("pop");
                rbPop.toggle();
                goodsService.loadGoodsDataByTypeFromNet(shopId, getCategoryIdByPosition(position), 1, GoodsFragment.size, GoodsFragment.ORDER_POP);
                popupWindow.dismiss();
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
                showFragment(GoodsFragment.ORDER_POP);
                break;
            case R.id.rb_price:
                showFragment(GoodsFragment.ORDER_PRICE);
                break;
            case R.id.btn_type:
                popupWindow.showAsDropDown(v, DensityUtils.dip2px(this, -60), DensityUtils.dip2px(this, -10));
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

    /**
     * 商品列表被点击时回调
     * @param goods
     */
    @Override
    public void onGoodsItemClick(Goods goods) {
        //TODO 完成商品订单弹窗
        OrderDialogFragment.newInstance(goods).show(getFragmentManager(), goods.getId()+"");
    }

    /**
     * 加入购物车的回调
     * @param goods
     */
    @Override
    public void onAddToCart(Goods goods) {
        AppContext app = (AppContext) getApplication();
        GoodsCart goodsCart = app.getGoodsCart();
        goodsCart.addGoods(goods);
        tvOrderedCount.setText(goodsCart.getTotalCount() + "");
        tvOrderedPrice.setText("￥" + goodsCart.getTotalPrice());
    }

    public int getShopId() {
        return shopId;
    }

    public void setCateogryData(List<GoodsCategory> goodsCategoryList) {
        this.categoryList = goodsCategoryList;
        categoryAdapter.notifyDataSetChanged();
    }

}
