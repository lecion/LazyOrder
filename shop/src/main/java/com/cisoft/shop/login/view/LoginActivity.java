package com.cisoft.shop.login.view;

import android.animation.ObjectAnimator;
import android.widget.ImageView;

import com.cisoft.myapplication.R;
import com.cisoft.shop.util.DeviceUtil;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class LoginActivity extends BaseActivity {

    @BindView(id = R.id.iv_app_logo)
    private ImageView ivAppLogo;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initWidget() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivAppLogo, "translationY", DeviceUtil.getScreenHeight(this) / 4 * 3, 0).setDuration(1000);
        animator.start();
    }
}
