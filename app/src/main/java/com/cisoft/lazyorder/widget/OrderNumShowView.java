package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.finals.ApiConstants;

import java.util.Map;

/**
 *
 * 一个订单数量的自定义控件
 *
 * Created by comet on 2014/10/25.
 */
public class OrderNumShowView extends FrameLayout{

    private Context context;
    private TextView tvGoodName;
    private TextView tvGoodCount;

    private String goodName = "";
    private int goodCount = 1;


    public OrderNumShowView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.order_num_show_layout, this);

        control();
    }

    public OrderNumShowView(Context context, Map<String, String> value) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.order_num_show_layout, this);

        control();
        setNameAndCountWithMap(value);
    }


    public OrderNumShowView(Context context, String goodName, int goodCount) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.order_num_show_layout, this);

        control();
        setGoodName(goodName);
        setGoodCount(goodCount);
    }


    private void control() {
        initWidget();
    }


    private void initWidget() {
        tvGoodName = (TextView) findViewById(R.id.tvONSVGoodName);
        tvGoodCount = (TextView) findViewById(R.id.tvGoodCount);
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
        tvGoodName.setText(this.goodName);
    }


    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
        tvGoodCount.setText(this.goodCount);
    }

    public void setNameAndCountWithMap(Map<String, String> value){
        goodName = value.get(ApiConstants.KEY_HIS_ORDER_GOOD_NAME);
        goodCount = Integer.parseInt(value.get(ApiConstants.KEY_HIS_ORDER_GOOD_COUNT));

        tvGoodName.setText(goodName);
        tvGoodCount.setText(String.valueOf(goodCount));
    }

}
