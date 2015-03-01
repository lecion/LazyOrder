package com.cisoft.lazyorder.ui.account;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.core.account.AccountNetwork;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by comet on 2015/2/24.
 */
public class EditPasswordActivity extends BaseActivity {

    @BindView(id = R.id.et_current_password)
    private EditText mEtCurrentPwd;
    @BindView(id = R.id.et_new_password)
    private EditText mEtNewPwd;
    @BindView(id = R.id.et_confirm_new_password)
    private EditText mEtNewPwdConfirm;
    @BindView(id = R.id.btn_submit, click = true)
    private Button mBtnSubmit;
    private Dialog mWaitTipDialog;

    private AppContext mAppContext;
    private AccountNetwork mAccountNetwork;
    private User loginUser;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_edit_password);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mAccountNetwork = new AccountNetwork(this);
        loginUser = mAppContext.getLoginInfo();
    }

    @Override
    public void initWidget() {
        setTitle(R.string.title_activity_edit_password);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                doUpdatePassword();
                break;
        }
    }

    private void doUpdatePassword() {
        String currentPwd = mEtCurrentPwd.getText().toString();
        final String newPwd = mEtNewPwd.getText().toString();
        String newPwdConfirm = mEtNewPwdConfirm.getText().toString();

        if (StringUtils.isEmpty(currentPwd)) {
            ViewInject.toast(getString(R.string.toast_current_password_can_not_be_empty));
            return;
        }
        if (StringUtils.isEmpty(newPwd)) {
            ViewInject.toast(getString(R.string.toast_new_password_can_not_be_empty));
            return;
        }
        if (StringUtils.isEmpty(newPwdConfirm)) {
            ViewInject.toast(getString(R.string.toast_new_password_confirm_can_not_be_empty));
            return;
        }
        if (!newPwd.equals(newPwdConfirm)) {
            ViewInject.toast(getString(R.string.toast_twice_password_not_same));
            return;
        }
        if (!currentPwd.equals(loginUser.getUserPwd())) {
            ViewInject.toast(getString(R.string.toast_old_password_error));
            return;
        }

        mAccountNetwork.userUpdatePassword(loginUser.getUserId(), newPwd,
                new AccountNetwork.OnUpdatePwdCallback() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess() {
                closeWaitTip();
                loginUser.setUserPwd(newPwd);
                mAppContext.saveLoginInfo(loginUser);
                mAppContext.logout();
                LoginStateObserver.getInstance().notifyStateChanged();
                ViewInject.toast(getString(R.string.toast_success_to_update_password));
                EditPasswordActivity.this.finish();
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
                    getString(R.string.toast_being_alter_password));
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

}
