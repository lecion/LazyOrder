package com.cisoft.lazyorder.ui.cart;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.settle.SettleActivity;
import com.cisoft.lazyorder.util.Utility;
import com.cisoft.lazyorder.widget.CompatListView;

import org.kymjs.kjframe.ui.BindView;

import java.util.List;

public class CartActivity extends BaseActivity {

    @BindView(id = R.id.lvOrderList)
    private CompatListView lvOrderList;

    @BindView(id = R.id.tv_settled_price)
    private TextView tvMoneyAll;

    @BindView(id = R.id.rlShowGoodsCart)
    private RelativeLayout rlShowGoodsCart;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    @BindView(id = R.id.btnSureOrder, click = true)
    private Button btnSureOrder;

    private List<Goods> orderListData;
    private CartListAdapter cartListAdapter;


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
        initAdapter();
        setMoneyAll();
    }

    private void initAdapter() {
        cartListAdapter = new CartListAdapter(this, orderListData);
        lvOrderList.setAdapter(cartListAdapter);
        resetListViewHeight();
    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btnSureOrder:
                showActivity(this, SettleActivity.class);
                break;
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
