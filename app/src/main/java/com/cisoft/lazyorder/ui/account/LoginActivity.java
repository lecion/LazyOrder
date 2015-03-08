package com.cisoft.lazyorder.ui.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.core.account.AccountNetwork;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class LoginActivity extends BaseActivity {

    @BindView(id = R.id.et_user_account)
    private EditText mEtUserAccount;
    @BindView(id = R.id.iv_delete_account, click = true)
    private ImageView mIvDeleteAccount;
    @BindView(id = R.id.et_user_password)
    private EditText mEtUserPwd;
    @BindView(id = R.id.iv_delete_password, click = true)
    private ImageView mIvDeletePwd;
    @BindView(id = R.id.btn_login, click = true)
    private Button mBtnLogin;
    @BindView(id = R.id.tv_forget_password, click = true)
    private TextView mTvForgetPassword;
    @BindView(id = R.id.tv_go_to_register, click = true)
    private TextView mTvGoToRegister;
    private Dialog mWaitTipDialog;
    private Dialog mSuccessTipDialog;

    private LoginStateObserver mLoginObserver;
    private AppContext mAppContext;
    private AccountNetwork mAccountNetwork;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mLoginObserver = LoginStateObserver.getInstance();
        mAccountNetwork = new AccountNetwork(this);
    }

    @Override
    public void initWidget() {
        mEtUserAccount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int visible = StringUtils.isEmpty(s.toString()) ? View.GONE : View.VISIBLE;
                mIvDeleteAccount.setVisibility(visible);
            }

            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }
        });
        mEtUserPwd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int visible = StringUtils.isEmpty(s.toString()) ? View.GONE : View.VISIBLE;
                mIvDeletePwd.setVisibility(visible);
            }
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {}
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {}
        });
        mEtUserAccount.setText(mAppContext.getRecentAccount());
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                doUserLogin();
                break;
            case R.id.iv_delete_account:
                mEtUserAccount.setText("");
                break;
            case R.id.iv_delete_password:
                mEtUserPwd.setText("");
                break;
            case R.id.tv_forget_password:
                // TODO
                break;
            case R.id.tv_go_to_register:
                skipActivity(LoginActivity.this, RegisterActivity.class);
                break;
        }
    }

    /**
     * 执行用户登录操作
     */
    private void doUserLogin() {
        final String inputUserAccount = mEtUserAccount.getText().toString();
        String inputUserPwd = mEtUserPwd.getText().toString();

        if (StringUtils.isEmpty(inputUserAccount)) {
            ViewInject.toast(getString(R.string.toast_user_account_can_not_be_empty));
            return;
        }

        if (StringUtils.isEmpty(inputUserPwd)) {
            ViewInject.toast(getString(R.string.toast_password_can_not_be_empty));
            return;
        }

        mAccountNetwork.userLogin(inputUserAccount, inputUserPwd, new AccountNetwork.OnLoginCallback() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess(User user) {
                closeWaitTip();
                showSuccessTip();
                mAppContext.saveLoginInfo(user);
                mAppContext.setRecentAccount(inputUserAccount);
                mLoginObserver.notifyStateChanged();
                // 2秒后返回上一页
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        closeSuccessTip();
                        LoginActivity.this.finish();
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
                    getString(R.string.toast_being_login));
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
     * 显示登录成功的对话框提示
     */
    private void showSuccessTip() {
        if (mSuccessTipDialog == null)
            mSuccessTipDialog = DialogFactory.createSuccessToastDialog(
                    this,
                    getString(R.string.toast_success_to_login));
        mSuccessTipDialog.show();
    }

    /**
     * 关闭登录成功的对话框提示
     */
    private void closeSuccessTip() {
        if (mSuccessTipDialog != null
                && mSuccessTipDialog.isShowing()) {
            mSuccessTipDialog.dismiss();
        }
    }

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
