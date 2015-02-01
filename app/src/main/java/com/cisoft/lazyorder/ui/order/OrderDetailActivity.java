package com.cisoft.lazyorder.ui.order;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.ui.main.MainActivity;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

public class OrderDetailActivity extends KJActivity {

    /* find views */
    @BindView(id = R.id.tv_shop_name)
    private TextView tvShopName;

    @BindView(id = R.id.tv_money_all)
    private TextView tvMoneyAll;

    @BindView(id = R.id.tv_order_time)
    private TextView tvOrderTime;

    @BindView(id = R.id.tv_goods_list_container)
    private LinearLayout llGoodsListContainer;

    @BindView(id = R.id.tv_extra_message)
    private TextView tvExtraMsg;

    @BindView(id = R.id.tv_delivery_money)
    private TextView tvDeliveryMoney;

    @BindView(id = R.id.tv_order_no)
    private TextView tvOrderNo;

    @BindView(id = R.id.tv_contact_address)
    private TextView tvContactAddress;


    /* define fields */
    public static final String DISH_ORDER = "dishOrder";
    private DishOrder dishOrder;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_order_detail);
    }


    @Override
    public void initData() {
        dishOrder = (DishOrder) getIntent().getSerializableExtra(DISH_ORDER);
    }


    @Override
    public void initWidget() {
        initActionBar();
        initContent();
    }


    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  订单详情");
    }


    /**
     * 初始化数据内容
     */
    private void initContent(){
        tvShopName.setText(dishOrder.getShopName());
        tvMoneyAll.setText(String.valueOf(dishOrder.getMoneyAll()));
        tvOrderTime.setText(dishOrder.getSubmitTime());
        tvExtraMsg.setText(dishOrder.getExtraMsg());
        tvDeliveryMoney.setText(String.valueOf(dishOrder.getDeliveryMoney()));
        tvOrderNo.setText(dishOrder.getOrderNo());
        tvContactAddress.setText(dishOrder.getAddress());
        View view;
        ImageView seperator;
        for (Goods goods : dishOrder.getGoodsList()) {
            //添加分割线
            seperator = new ImageView(this);
            seperator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            seperator.setBackgroundResource(R.drawable.list_separator_line);
            llGoodsListContainer.addView(seperator);

            //加载"商品名x1 7.8"形式的视图
            view = getLayoutInflater().inflate(R.layout.activity_order_detail_goods_list_cell, llGoodsListContainer);
            ((TextView)view.findViewById(R.id.tv_goods_name)).setText(goods.getCmName());
            ((TextView)view.findViewById(R.id.tv_goods_ordered_count)).setText(String.valueOf(goods.getOrderNum()));
            ((TextView)view.findViewById(R.id.tv_same_goods_total_price)).setText(String.valueOf(goods.getOrderNum() * goods.getCmPrice()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                skipActivity(this, intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            skipActivity(this, intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
