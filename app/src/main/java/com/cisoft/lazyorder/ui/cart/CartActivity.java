package com.cisoft.lazyorder.ui.cart;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.ui.sureinfo.SureInfoActivity;
import com.cisoft.lazyorder.util.Utility;
import com.cisoft.lazyorder.widget.CompatListView;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import java.util.List;

public class CartActivity extends KJActivity {

    @BindView(id = R.id.lvOrderList)
    private CompatListView lvOrderList;

    @BindView(id = R.id.tv_money_all)
    private TextView tvMoneyAll;

    @BindView(id = R.id.rlShowGoodsCart)
    private RelativeLayout rlShowGoodsCart;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    @BindView(id = R.id.btnSureOrder, click = true)
    private Button btnSureOrder;

    private List<Goods> orderListData;
    private OrderListAdapter orderListAdapter;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_cart);
    }

    @Override
    public void initData() {
        orderListData = GoodsCart.getInstance().getAllGoods();
    }

    @Override
    public void initWidget() {
        initActionBar();
        initAdapter();
        setMoneyAll();
    }

    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  确认订单");
    }

    private void initAdapter() {
        orderListAdapter = new OrderListAdapter(this, orderListData);
        lvOrderList.setAdapter(orderListAdapter);
        resetListViewHeight();
    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btnSureOrder:
                showActivity(this, SureInfoActivity.class);
                break;
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

    public void showNoValueTip(){
        llShowNoValueTip.setVisibility(View.VISIBLE);
        rlShowGoodsCart.setVisibility(View.GONE);
    }

    public void hideNoValueTip(){
        llShowNoValueTip.setVisibility(View.GONE);
        rlShowGoodsCart.setVisibility(View.VISIBLE);
    }


    public void resetListViewHeight() {
        Utility.setListViewHeightBasedOnChildren(lvOrderList);
    }

    public void setMoneyAll() {
        tvMoneyAll.setText(String.valueOf(GoodsCart.getInstance().getTotalPrice() + 1));
    }
}
