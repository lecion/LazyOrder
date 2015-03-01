package com.cisoft.lazyorder.ui.account;

import android.app.Dialog;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.core.account.AccountNetwork;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class RegisterTwoActivity extends BaseActivity {

    @BindView(id = R.id.et_password)
    private EditText mEtInputPassword;
    @BindView(id = R.id.et_password_confirm)
    private EditText mEtInputSurePassword;
    @BindView(id = R.id.btn_sure_and_register, click = true)
    private Button mBtnSureAndRegister;
    private Dialog mWaitTipDialog;
    private Dialog mSuccessTipDialog;

    private AppContext mAppContext;
    private LoginStateObserver mLoginObserver;
    private AccountNetwork mAccountNetwork;
    private String mUserPhone;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_register_two);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mLoginObserver = LoginStateObserver.getInstance();
        mAccountNetwork = new AccountNetwork(this);
        mUserPhone = getIntent().getStringExtra(ApiConstants.KEY_ACCOUNT_USER_PHONE);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure_and_register:
                sureAndRegister();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void sureAndRegister() {
        String inputPassword = mEtInputPassword.getText().toString();
        String inputSurePassword = mEtInputSurePassword.getText().toString();
        if (StringUtils.isEmpty(inputPassword)) {
            ViewInject.toast(getString(R.string.toast_password_can_not_be_empty));
            return;
        }
        if(StringUtils.isEmpty(inputSurePassword)){
            ViewInject.toast(getString(R.string.toast_sure_password_can_not_be_empty));
            return;
        }
        if (!inputPassword.equals(inputSurePassword)) {
            ViewInject.toast(getString(R.string.toast_twice_password_not_same));
            return;
        }
        mAccountNetwork.userRegister(mUserPhone, inputPassword, new AccountNetwork.OnRegisterCallback() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess(User user) {
                closeWaitTip();
                showSuccessTip();
                mAppContext.saveLoginInfo(user);
                mAppContext.setRecentAccount(mUserPhone);
                mLoginObserver.notifyStateChanged();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        closeSuccessTip();
                        RegisterTwoActivity.this.finish();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                closeWaitTip();
                ViewInject.toast(errorMsg);
            }
        });
    }

    /**
     * 显示等待的提示对话框
     */
    private void showWaitTip() {
        if (mWaitTipDialog == null)
            mWaitTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.toast_being_register));
        mWaitTipDialog.show();
    }

    /**
     * 关闭等待的提示对话框
     */
    private void closeWaitTip() {
        if (mWaitTipDialog != null && mWaitTipDialog.isShowing()) {
            mWaitTipDialog.dismiss();
        }
    }

    /**
     * 显示注册成功的对话框提示
     */
    private void showSuccessTip() {
        if (mSuccessTipDialog == null)
            mSuccessTipDialog = DialogFactory.createSuccessToastDialog(
                    this,
                    getString(R.string.toast_success_to_register));
        mSuccessTipDialog.show();
    }

    /**
     * 关闭注册成功的对话框提示
     */
    private void closeSuccessTip() {
        if (mSuccessTipDialog != null
                && mSuccessTipDialog.isShowing()) {
            mSuccessTipDialog.dismiss();
        }
    }

}
