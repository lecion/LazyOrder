package com.cisoft.lazyorder.ui.shop;

import android.view.View;
import android.widget.Button;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.goods.GoodsActivity;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

/**
 * Created by Lecion on 10/15/14.
 */
public class ShopActivity extends BaseActivity{
    @BindView(id = R.id.btn_go_goods, click = true)
    private Button btnGoShop;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_shop);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_goods:
                showActivity(this, GoodsActivity.class);
                break;
        }
    }
}
