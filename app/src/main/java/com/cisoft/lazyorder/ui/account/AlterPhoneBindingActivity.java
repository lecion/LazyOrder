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
public class AlterPhoneBindingActivity extends BaseActivity {

    @BindView(id = R.id.et_new_phone)
    private EditText mEtNewPhone;
    @BindView(id = R.id.btn_confirm_modify, click = true)
    private Button mBtnConfirmModify;
    private Dialog mWaitTipDialog;

    private AppContext mAppContext;
    private AccountNetwork mAccountNetwork;
    private User mLoginUser;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_alter_phone_binding);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mAccountNetwork = new AccountNetwork(this);
        mLoginUser = mAppContext.getLoginInfo();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_modify:
                doAlterPhoneBinding();
                break;
        }
    }

    private void doAlterPhoneBinding() {
        final String newPhone = mEtNewPhone.getText().toString();

        if (StringUtils.isEmpty(newPhone)) {
            ViewInject.toast(getString(R.string.toast_new_phone_can_not_be_empty));
            return;
        }
        if (!StringUtils.isPhone(newPhone)) {
            ViewInject.toast(getString(R.string.toast_input_standard_phone));
            return;
        }
        mAccountNetwork.userAlterPhoneBinding(mLoginUser.getUserId(), newPhone,
                new AccountNetwork.OnAlterPhoneBindingCallback() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess() {
                closeWaitTip();
                mLoginUser.setUserPhone(newPhone);
                mAppContext.saveLoginInfo(mLoginUser);
                mAppContext.logout();
                LoginStateObserver.getInstance().notifyStateChanged();
                ViewInject.toast(getString(R.string.toast_success_to_alter_phone_binding));
                AlterPhoneBindingActivity.this.finish();
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

}
