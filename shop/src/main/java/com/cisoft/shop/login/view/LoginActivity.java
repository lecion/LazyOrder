package com.cisoft.shop.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cisoft.shop.AppConfig;
import com.cisoft.shop.MainActivity;
import com.cisoft.shop.R;
import com.cisoft.shop.login.presenter.LoginPresenter;
import com.cisoft.shop.util.DeviceUtil;
import com.cisoft.shop.util.IOUtil;
import com.cisoft.shop.widget.DialogFactory;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(id = R.id.iv_app_logo)
    private ImageView ivAppLogo;

    @BindView(id = R.id.et_phone)
    private EditText etPhone;

    @BindView(id = R.id.et_pwd)
    private EditText etPwd;

    @BindView(id = R.id.btn_login)
    private Button btnLogin;

    @BindView(id = R.id.rg_select)
    private RadioGroup rgSelect;

    @BindView(id = R.id.rb_normal)
    private RadioButton rbNormal;

    @BindView(id = R.id.rb_express)
    private RadioButton rbExpress;

    Dialog loadingDialog;

    LoginPresenter loginPresenter;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initData() {
        loginPresenter = new LoginPresenter(this, this);
    }

    @Override
    protected void initWidget() {
        btnLogin.setOnClickListener(this);
        startLoginAnimation();
        initInput();
    }

    private void initInput() {
        String phone = IOUtil.getLoginPhone(this);
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
        }
        String pwd = IOUtil.getLoginPwd(this);
        if (!TextUtils.isEmpty(pwd)) {
            etPwd.setText(pwd);
        }
        int type = IOUtil.getLoginType(this);
        if (type == AppConfig.TYPE_MERCHANT) {
            rbNormal.setChecked(true);
        } else if (type == AppConfig.TYPE_EXPMER) {
            rbExpress.setChecked(true);
        }
    }


    /**
     * 执行登陆界面动画
     */
    private void startLoginAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivAppLogo, "translationY", DeviceUtil.getScreenHeight(LoginActivity.this), -DeviceUtil.getScreenHeight(LoginActivity.this) / 20, 0);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                final ObjectAnimator animator2 = ObjectAnimator.ofFloat(ivAppLogo, "translationX", DeviceUtil.getScreenWidth(LoginActivity.this) - ivAppLogo.getWidth() * 2);
                animator2.setDuration(500);
                animator2.addListener(new AnimatorListenerAdapter() {
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
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ValueAnimator animator3 = ObjectAnimator.ofFloat(ivAppLogo, "translationX", 0);
                                animator3.setDuration(500);
                                animator3.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        ValueAnimator anim2 = ValueAnimator.ofFloat(0, 1);
                                        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                float value = (float) animation.getAnimatedValue();
                                                btnLogin.setAlpha(value);
                                                rgSelect.setAlpha(value);
                                            }
                                        });

                                        anim2.setDuration(700);
                                        anim2.start();
                                    }
                                });
                                animator3.start();
                            }
                        });
                        anim.start();
                    }
                });
                animator2.start();
            }
        });
        animator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (isMerLogin()) {
                    loginPresenter.normalLogin(etPhone.getText().toString(), etPwd.getText().toString());
                } else {
                    loginPresenter.expressLogin(etPhone.getText().toString(), etPwd.getText().toString());
                }
                break;
        }
    }


    @Override
    public void showLoginProgress() {
        if (loadingDialog == null) {
            loadingDialog = DialogFactory.createToastDialog(this, "登陆中...");
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoginProgress() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showLoginFailed(String msg) {
        ViewInject.toast(msg);
    }

    @Override
    public void showWrongInput() {
        ViewInject.toast("请输入正确的手机号和密码");
    }

    @Override
    public void skipToMainActivity() {
        skipActivity(this, MainActivity.class);
    }

    private boolean isMerLogin() {
        return rbNormal.isChecked();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (DeviceUtil.isInputMethodShow(this)) {
            ivAppLogo.setVisibility(View.GONE);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DeviceUtil.isInputMethodShow(this)) {
            ivAppLogo.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);
    }
}
