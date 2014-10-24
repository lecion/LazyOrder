package com.cisoft.lazyorder;

import android.widget.ImageView;

import com.cisoft.lazyorder.ui.goods.GoodsActivity;

import org.kymjs.aframe.ui.activity.BaseSplash;


public class WelcomeActivity extends BaseSplash {


    @Override
    protected void setRootBackground(ImageView view) {
        view.setBackgroundResource(R.drawable.ic_launcher);

    }

    @Override
    protected void redirectTo() {
        super.redirectTo();
        skipActivity(this, GoodsActivity.class);
//        skipActivity(this, ShopActivity.class);
    }


}
