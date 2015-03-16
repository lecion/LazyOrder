package com.cisoft.shop.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.cisoft.shop.AppConfig;
import com.cisoft.shop.MainActivity;
import com.cisoft.shop.R;
import com.cisoft.shop.login.presenter.LoginPresenter;
import com.cisoft.shop.util.DeviceUtil;
import com.cisoft.shop.util.IOUtil;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(id = R.id.rl_container)
    private RelativeLayout rlContainer;

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

    @BindView(id = R.id.pb_loading)
    ProgressBar pbLoading;

    LoginPresenter loginPresenter;

    private boolean isLogining;


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
        rgSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_normal) {
                    rbNormal.setTextColor(getResources().getColor(R.color.material_font_color));
                    rbExpress.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                } else {
                    rbNormal.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                    rbExpress.setTextColor(getResources().getColor(R.color.material_font_color));
                }
            }
        });
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
            rbNormal.setTextColor(getResources().getColor(R.color.material_font_color));
        } else if (type == AppConfig.TYPE_EXPMER) {
            rbExpress.setChecked(true);
            rbExpress.setTextColor(getResources().getColor(R.color.material_font_color));
        }

    }


    /**
     * 执行登陆界面动画
     */
    private void startLoginAnimation() {
        int screenHeight = DeviceUtil.getScreenHeight(this);
        int screenTop = DeviceUtil.dp2px(this, 108);

        ObjectAnimator transLogoY = ObjectAnimator.ofFloat(ivAppLogo, "translationY", - screenTop, screenTop * 2, 0);
        transLogoY.setDuration(1500);
        transLogoY.setInterpolator(new BounceInterpolator());

        ObjectAnimator scaleLogoX = ObjectAnimator.ofFloat(ivAppLogo, View.SCALE_X, 1, 3, 1);
        ObjectAnimator scaleLogoY = ObjectAnimator.ofFloat(ivAppLogo, View.SCALE_Y, 1, 3, 1);
        scaleLogoX.setDuration(700);
        scaleLogoY.setDuration(700);

        ObjectAnimator rotateLogo = ObjectAnimator.ofFloat(ivAppLogo, View.ROTATION, -20, 20, -40, 40, -20, 20, 0);
        rotateLogo.setInterpolator(new BounceInterpolator());
        rotateLogo.setDuration(700);

        AnimatorSet scale = new AnimatorSet();
        scale.playTogether(scaleLogoX, scaleLogoY, rotateLogo);

        ObjectAnimator alphaPhone = ObjectAnimator.ofFloat(etPhone, View.ALPHA, 0.0f, 1.0f);
        alphaPhone.setDuration(300);

        ObjectAnimator alphaPwd = ObjectAnimator.ofFloat(etPwd, View.ALPHA, 0.0f, 1.0f);
        alphaPwd.setDuration(300);

        ObjectAnimator alphaRadio = ObjectAnimator.ofFloat(rgSelect, View.ALPHA, 0.0f, 1.0f);
        alphaRadio.setDuration(300);

        ObjectAnimator alphaLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 0.0f, 1.0f);
        alphaLogin.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(transLogoY).before(alphaPhone);
        animatorSet.play(alphaPwd).after(alphaPhone);
        animatorSet.play(alphaRadio).after(alphaPwd);
        animatorSet.play(alphaLogin).after(alphaRadio);
        animatorSet.play(scale).after(alphaLogin);
        animatorSet.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (!isLogining) {
                    if (isMerLogin()) {
                        loginPresenter.normalLogin(etPhone.getText().toString(), etPwd.getText().toString());
                    } else {
                        loginPresenter.expressLogin(etPhone.getText().toString(), etPwd.getText().toString());
                    }
                }
                break;
        }
    }


    @Override
    public void showLoginProgress() {
//        if (loadingDialog == null) {
//            loadingDialog = DialogFactory.createToastDialog(this, "登陆中...");
//        }
//        if (!loadingDialog.isShowing()) {
//            loadingDialog.show();
//        }
        isLogining = true;
        ivAppLogo.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginProgress() {
//        if (loadingDialog != null) {
//            loadingDialog.dismiss();
//        }
        pbLoading.setVisibility(View.GONE);
        ivAppLogo.setImageResource(R.drawable.baffled);
        ivAppLogo.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginFailed(String msg) {
//        ViewInject.toast(msg);
        ivAppLogo.setImageResource(R.drawable.crying);
        ObjectAnimator scaleLogoX = ObjectAnimator.ofFloat(ivAppLogo, View.SCALE_X, 0.5f, 1.5f, 1);
        ObjectAnimator scaleLogoY = ObjectAnimator.ofFloat(ivAppLogo, View.SCALE_Y, 0.5f, 1.5f, 1);
        scaleLogoX.setDuration(700);
        scaleLogoY.setDuration(700);

        ObjectAnimator rotateLogo = ObjectAnimator.ofFloat(ivAppLogo, View.ROTATION, -20, 20, -40, 40, -20, 20, 0);
        rotateLogo.setInterpolator(new BounceInterpolator());
        rotateLogo.setDuration(700);

        AnimatorSet shakeAndRotate = new AnimatorSet();
        shakeAndRotate.playTogether(scaleLogoX, scaleLogoY, rotateLogo);
        shakeAndRotate.start();
        isLogining = false;
    }

    @Override
    public void showWrongInput(int... ids) {
        for (int id : ids) {
            switch (id) {
                case R.id.et_phone:
                    animWrongView(etPhone);
                    break;
                case R.id.et_pwd:
                    animWrongView(etPwd);
                    break;
            }
        }
    }

    /**
     * 生成动画
     * @param v
     */
    public void animWrongView(View v) {
//        ViewInject.toast("请输入正确的手机号和密码");
        ObjectAnimator scaleLogoX = ObjectAnimator.ofFloat(v, View.SCALE_X, 0.5f, 1.1f, 1);
        ObjectAnimator scaleLogoY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 0.5f, 1.1f, 1);
        scaleLogoX.setDuration(700);
        scaleLogoY.setDuration(700);

        ObjectAnimator rotateLogo = ObjectAnimator.ofFloat(v, View.ROTATION, -10, 10, -20, 20, -10, 10, 0);
        rotateLogo.setInterpolator(new BounceInterpolator());
        rotateLogo.setDuration(700);

        AnimatorSet shakeAndRotate = new AnimatorSet();
        shakeAndRotate.playTogether(scaleLogoX, scaleLogoY, rotateLogo);
        shakeAndRotate.start();
    }

    @Override
    public void skipToMainActivity() {
        successLoginAnim();
    }

    private void successLoginAnim() {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(rlContainer, View.ROTATION, 360 * 3);
        rotation.setDuration(1500);
        rotation.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rlContainer, View.SCALE_X, 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rlContainer, View.SCALE_Y, 1f, 0f);
        scaleX.setDuration(1500);
        scaleY.setDuration(1500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotation, scaleX, scaleY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                skipActivity(LoginActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
        animatorSet.start();
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
