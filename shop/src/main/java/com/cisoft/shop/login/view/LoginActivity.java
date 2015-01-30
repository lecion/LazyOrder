package com.cisoft.shop.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.cisoft.myapplication.R;
import com.cisoft.shop.util.DeviceUtil;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class LoginActivity extends BaseActivity {

    @BindView(id = R.id.iv_app_logo)
    private ImageView ivAppLogo;

    @BindView(id = R.id.et_phone)
    private EditText etPhone;

    @BindView(id = R.id.et_pwd)
    private EditText etPwd;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initWidget() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivAppLogo, "translationY", DeviceUtil.getScreenHeight(LoginActivity.this), DeviceUtil.getScreenHeight(LoginActivity.this) / 5, DeviceUtil.getScreenHeight(LoginActivity.this) / 3, DeviceUtil.getScreenHeight(LoginActivity.this) / 5);
//        animator.setStartDelay(200);
        animator.setDuration(2500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        etPhone.setAlpha(value);
                        etPwd.setAlpha(value);
                    }
                });
                anim.setDuration(700);
                anim.start();
            }
        });
        animator.start();
    }
}
