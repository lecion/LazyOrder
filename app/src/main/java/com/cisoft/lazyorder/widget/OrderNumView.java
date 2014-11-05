package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.cisoft.lazyorder.R;

/**
 *
 * 一个订单数量的自定义控件
 *
 * Created by comet on 2014/10/25.
 */
public class OrderNumView extends FrameLayout{

    private Context context;
    private ImageButton ibAddNum;
    private ImageButton ibSubNum;
    private TextView tvInputNum;
    private int num = 1;
    private OnClickListener btnClickListener;
    private OnOrderNumChangeListener changeListener;


    public OrderNumView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.order_num_layout, this);

        control();
    }


    public OrderNumView(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
        LayoutInflater.from(context).inflate(R.layout.order_num_layout, this);

        control();
    }


    public OrderNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.order_num_layout, this);

        control();
    }


    private void control() {
        initWidget();
    }


    private void initWidget() {
        btnClickListener = new ButtonOnClickListener();
        ibAddNum = (ImageButton) findViewById(R.id.ibAddNum);
        ibAddNum.setEnabled(true);
        ibAddNum.setOnClickListener(btnClickListener);
        ibSubNum = (ImageButton) findViewById(R.id.ibSubNum);
        ibSubNum.setEnabled(true);
        ibSubNum.setOnClickListener(btnClickListener);

        tvInputNum = (TextView) findViewById(R.id.tvInputNum);
        tvInputNum.setText(String.valueOf(num));
    }

    private class ButtonOnClickListener implements OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ibAddNum:
                    num++;
                    if(num >= 2) {
                        ibSubNum.setEnabled(true);
                    }
                    tvInputNum.setText(String.valueOf(num));
                    break;
                case R.id.ibSubNum:
                    if (num >= 2) {
                        num--;
                    }
                    if (num == 1) {
                        ibSubNum.setEnabled(false);
                    }
                    tvInputNum.setText(String.valueOf(num));
                    break;
            }

            if(changeListener != null) {
                changeListener.onChange(OrderNumView.this, num);
            }
        }
    }

    public void setNum(int num) {
        this.num = num;
        tvInputNum.setText(String.valueOf(num));
    }

    public int getNum() {
        return num;
    }


    /**
     * 供外界调用，当num改变时回调
     */
    public interface OnOrderNumChangeListener{
        public void onChange(View view, int num);
    }

    /**
     * 设置订单量改变的监听器
     * @param changeListener
     */
    public void setOnOrderNumChangeListener(OnOrderNumChangeListener changeListener){
        this.changeListener = changeListener;
    }
}
