package com.cisoft.lazyorder.ui.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.GoodsXCount;
import com.cisoft.lazyorder.bean.order.Order;
import com.cisoft.lazyorder.ui.BaseActivity;

import org.kymjs.kjframe.ui.BindView;

public class OrderDetailActivity extends BaseActivity {

    @BindView(id = R.id.tv_shop_name)
    private TextView mTvShopName;
    @BindView(id = R.id.tv_settled_price)
    private TextView mTvMoneyAll;
    @BindView(id = R.id.tv_submit_time)
    private TextView mTvSubmitTime;
    @BindView(id = R.id.tv_goods_list_container)
    private LinearLayout mLlGoodsListContainer;
    @BindView(id = R.id.tv_extra_message)
    private TextView mTvExtraMsg;
    @BindView(id = R.id.tv_delivery_money)
    private TextView mTvDeliveryMoney;
    @BindView(id = R.id.tv_order_number)
    private TextView mTvOrderNumber;
    @BindView(id = R.id.tv_contact_address)
    private TextView mTvContactAddress;

    public static final String EXTRA_ORDER_OBJ = "extraOrderObj";
    private Order mExtraOrderObj;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_order_detail);
    }


    @Override
    public void initData() {
        mExtraOrderObj = (Order) getIntent().getSerializableExtra(EXTRA_ORDER_OBJ);
    }


    @Override
    public void initWidget() {
        initContent();
    }


    /**
     * 初始化数据内容
     */
    private void initContent(){
        mTvShopName.setText(mExtraOrderObj.getShopName());
        mTvMoneyAll.setText(String.valueOf(mExtraOrderObj.getSettledPrice()));
        mTvSubmitTime.setText(mExtraOrderObj.getOrderTime());
        mTvExtraMsg.setText(mExtraOrderObj.getExtraMsg());
        mTvDeliveryMoney.setText(String.valueOf(mExtraOrderObj.getShippingFee()));
        mTvOrderNumber.setText(mExtraOrderObj.getOrderNum());
        mTvContactAddress.setText(mExtraOrderObj.getAddress());
        View view;
        ImageView seperator;
        for (GoodsXCount goodsXCount : mExtraOrderObj.getGoodsList()) {
            //添加分割线
            seperator = new ImageView(this);
            seperator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            seperator.setBackgroundResource(R.drawable.list_separator_line);
            mLlGoodsListContainer.addView(seperator);

            //加载"商品名x1 7.8"形式的视图
            view = getLayoutInflater().inflate(R.layout.activity_order_detail_goods_list_cell, mLlGoodsListContainer);
            ((TextView)view.findViewById(R.id.tv_goods_name)).setText(goodsXCount.getGoodsName());
            ((TextView)view.findViewById(R.id.tv_goods_ordered_count)).setText(String.valueOf(goodsXCount.getCount()));
            ((TextView)view.findViewById(R.id.tv_same_goods_total_price)).setText(String.valueOf(goodsXCount.getCount() * goodsXCount.getCount()));
        }
    }


    public static void startFrom(Activity activity, Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ORDER_OBJ, order);
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}
