package com.cisoft.lazyorder.ui.search;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.cisoft.lazyorder.widget.OrderNumView;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/17/14.
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
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

    @BindView(id = R.id.rl_goods_cart)
    private RelativeLayout rlGoodsCart;

    private Dialog clearCartDialog;

    private int shopId;

    private String shopName;

    private String shopAddress;

    private SearchResultAdapter mAdapter;

    private List<Goods> goodsList;

    private int mLastVisiblePosition = -1;

    private LinearLayout mLastVisibleView;

    private boolean isExpand = false;

    private KJBitmap kjb;


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
        shopAddress = data.getString(ApiConstants.KEY_MER_ADDRESS);
        goodsList = new ArrayList<Goods>();
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
        mAdapter = new SearchResultAdapter();
        lvSearchResult.setAdapter(mAdapter);
        lvSearchResult.setOnItemClickListener(this);
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
            rlGoodsCart.setVisibility(View.GONE);
            /** /
            ivCartLogo.setImageResource(R.drawable.cart_logo_normal);
            tvOrderedCount.setText(goodsCart.getTotalCount() + "份");
            tvOrderedPrice.setText("￥" + goodsCart.getTotalPrice());
            tvOrderedCount.setVisibility(View.INVISIBLE);
            tvOrderedPrice.setVisibility(View.INVISIBLE);
            btnGoSettle.setText("未选择");
            /**/
        } else {
            rlGoodsCart.setVisibility(View.VISIBLE);
            ivCartLogo.setImageResource(R.drawable.cart_logo_selected);
            tvOrderedCount.setText(goodsCart.getTotalCount() + "份");
            tvOrderedPrice.setText("￥" + goodsCart.getTotalPrice());
            //tvOrderedCount.setVisibility(View.VISIBLE);
            //tvOrderedPrice.setVisibility(View.VISIBLE);
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
            Log.d("onAddToCart", goodsCart.isSameShop(shopId) + "");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onItemClick", "position  " + position);
        if (position > goodsList.size()) {
            return;
        }
        //mListener.onGoodsItemClick(goodsData.get(position));
        LinearLayout currentLL = (LinearLayout) view.findViewById(R.id.ll_expand);
        //第一次点击
        if (mLastVisibleView == null) {
            mLastVisibleView = currentLL;
            currentLL.setVisibility(View.VISIBLE);
            isExpand = true;
        } else {
            //不是第一次点击
            //上次点击与本次不一样，则隐藏上次视图，显示本次视图
            if (mLastVisiblePosition != position) {
                currentLL.setVisibility(View.VISIBLE);
                mLastVisibleView.setVisibility(View.GONE);
                mLastVisibleView = currentLL;
                isExpand = true;
//                    Log.d("mLastVisiblePosition", mLastVisiblePosition+"");
            } else {
                //展开则隐藏
                if (isExpand) {
                    currentLL.setVisibility(View.GONE);
                } else {
                    currentLL.setVisibility(View.VISIBLE);
                }
                isExpand = !isExpand;
                //Log.d("mLastVisiblePosition", mLastVisiblePosition+"");
            }
        }
        mLastVisiblePosition = position;
    }

    /**
     * 搜索结果适配器
     */
    class SearchResultAdapter extends BaseAdapter {
        public SearchResultAdapter() {
            kjb = KJBitmap.create();
        }

        @Override
        public int getCount() {
            return goodsList.size();
        }

        @Override
        public Object getItem(int position) {
            Goods g = goodsList.get(position);
            g.setShopId(shopId);
            g.setShopName(shopName);
            return g;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//          Log.d("getView", "mLastVisiblePosition => " + mLastVisiblePosition + "  position=> " + position);
            final Goods item = (Goods) getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.fragment_goods_list_cell, parent, false);
                holder.tvGoodsTitle = (TextView) convertView.findViewById(R.id.tv_goods_title);
                holder.tvGoodsAddress = (TextView) convertView.findViewById(R.id.tv_address);
                holder.tvGoodsType = (TextView) convertView.findViewById(R.id.tv_goods_type);
                holder.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
                holder.btnGoodsPrice = (Button) convertView.findViewById(R.id.btn_goods_price);
                holder.ivGoodsThumb = (ImageView) convertView.findViewById(R.id.iv_goods_thumb);
                holder.llExpand = (LinearLayout) convertView.findViewById(R.id.ll_expand);
                holder.orderNumView = (OrderNumView) convertView.findViewById(R.id.order_num_view);
                holder.btnAddToCart = (Button) convertView.findViewById(R.id.btn_add_to_cart);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            kjb.display(holder.ivGoodsThumb, item.getCmPicture(), holder.ivGoodsThumb.getWidth(), holder.ivGoodsThumb.getHeight());
            holder.tvGoodsTitle.setText(item.getCmName());
            holder.tvGoodsAddress.setText(shopAddress);
            holder.btnGoodsPrice.setText(String.valueOf(item.getCmPrice()));

            final View animView = holder.btnGoodsPrice;
            holder.btnGoodsPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    startAnimation(v, position);
                }

            });
            holder.tvGoodsCount.setText(String.valueOf(item.getSalesNum()));
            holder.tvGoodsType.setText(item.getCatName());
                /*以下是展开view*/
            holder.llExpand.setVisibility(View.GONE);
            holder.btnAddToCart.setOnClickListener(new AddToCartListener(holder.orderNumView, item, animView));
            if (mLastVisiblePosition == position + 1 && isExpand) {
                holder.llExpand.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        /**
         * 点击价格标签时的动画
         *
         * @param v
         * @param position
         * @return
         */
        private void startAnimation(final View v, int position) {
            final Goods g = (Goods) getItem(position);
            //重新设置价格，防止重用出现问题
            ((Button)v).setText(g.getCmPrice() + "");
            final View animView = createAnimView(v);
            final ViewGroup animLayout = createAnimLayer();
            animLayout.addView(animView);
            int[] startLoc = getAnimStartLocation(v);
            //Log.d("start ", startLoc[0] + " " +startLoc[1]);
            setAnimViewLoc(startLoc, animView);
            int[] endLoc = getAnimEndLocation();
            int[] offset = getAnimOffset(startLoc, endLoc);
            final Animation animation = buildAnimation(offset);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    v.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAddToCart(g);
                    ((ViewGroup)animLayout.getParent()).removeView(animLayout);
                    v.setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //animView.setAnimation(animation);
            animView.startAnimation(animation);
        }

        /**
         * 点击加入购物车按钮时的动画
         *
         * @param v
         * @param g
         */
        private void startAnimation(final View v, final Goods g, final OrderNumView orderNumView, final View disableView) {
            //重新设置价格，防止重用出现问题
            ((Button)v).setText(g.getCmPrice() + "");
            final View animView = createAnimView(v);
            final ViewGroup animLayout = createAnimLayer();
            animLayout.addView(animView);
            int[] startLoc = getAnimStartLocation(v);
            setAnimViewLoc(startLoc, animView);
            int[] endLoc = getAnimEndLocation();
            int[] offset = getAnimOffset(startLoc, endLoc);
            Animation animation = buildAnimation(offset);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    disableView.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAddToCart(g, orderNumView.getNum());
                    //Did 展开view的复用问题：商品数量选择控件被复用=>暂时先这样解决
                    orderNumView.setNum(1);
                    ((ViewGroup)animLayout.getParent()).removeView(animLayout);
                    disableView.setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animView.setAnimation(animation);
        }

        /**
         * 创建动画层
         * @return
         */
        private ViewGroup createAnimLayer() {
            final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            LinearLayout ll = new LinearLayout(SearchActivity.this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            ll.setBackgroundResource(android.R.color.transparent);
            decorView.addView(ll);
            return ll;
        }

        /**
         * 创建动画对象
         * @param v
         * @return
         */
        private View createAnimView(View v) {
            v.buildDrawingCache();
            Bitmap animBitmap = v.getDrawingCache();
            ImageView ivAnim = new ImageView(SearchActivity.this);
            ivAnim.setImageBitmap(animBitmap);
            return ivAnim;
        }

        /**
         * 获得动画开始位置，即价格视图所在位置
         * @param v
         * @return
         */
        private int[] getAnimStartLocation(View v) {
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            return location;
        }

        /**
         * 获得动画结束位置，即购物车所在位置
         * @return
         */
        private int[] getAnimEndLocation() {
            return getCartLocation();
        }

        /**
         * 设置动画起始位置
         * @param startLoc
         * @param animView
         */
        private void setAnimViewLoc(int[] startLoc, View animView) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = startLoc[0];
            lp.topMargin = startLoc[1];
            animView.setLayoutParams(lp);
        }

        /**
         * 获得动画x，y坐标的偏移量
         * @param startLoc
         * @param endLoc
         * @return
         */
        private int[] getAnimOffset(int[] startLoc, int[] endLoc) {
            int[] offset = new int[2];
            offset[0] = endLoc[0] - startLoc[0];
            offset[1] = endLoc[1] - startLoc[1];
            return offset;
        }

        /**
         * 根据偏移量创建动画
         * @param offset
         * @return
         */
        private Animation buildAnimation(int[] offset) {
            AnimationSet as = new AnimationSet(false);
            TranslateAnimation translateX = new TranslateAnimation(0, offset[0], 0, 0);
            TranslateAnimation translateY = new TranslateAnimation(0, 0, 0, offset[1]);
            translateY.setInterpolator(new AccelerateInterpolator());
            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0);
            scaleAnimation.setInterpolator(new AccelerateInterpolator());
            as.addAnimation(scaleAnimation);
            as.addAnimation(translateX);
            as.addAnimation(translateY);
            as.setDuration(300);
            return as;
        }

        /**
         * 添加到购物车按钮被点击时的监听器
         */
        class AddToCartListener implements View.OnClickListener {
            OrderNumView orderNumView;
            Goods goods;
            View animView;
            public AddToCartListener(OrderNumView orderNumView, Goods item, View animView) {
                this.orderNumView = orderNumView;
                this.goods = item;
                this.animView = animView;
            }

            @Override
            public void onClick(View v) {
                startAnimation(animView, goods, orderNumView, v);
            }
        }

        /**
         * 设置data
         * @param l
         */
        public void setData(List<Goods> l) {
            goodsList.clear();
            goodsList.addAll(l);
            notifyDataSetChanged();
        }

        public void clearData() {
            goodsList.clear();
            notifyDataSetChanged();
        }

        private class ViewHolder {
            TextView tvGoodsTitle;
            TextView tvGoodsAddress;
            TextView tvGoodsType;
            TextView tvGoodsCount;
            Button btnGoodsPrice;
            ImageView ivGoodsThumb;
            /*以下是展开的view的控件*/
            LinearLayout llExpand;
            ListView lvGoodsComment;
            OrderNumView orderNumView;
            Button btnAddToCart;

        }
    }

}
