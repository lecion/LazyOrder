package com.cisoft.lazyorder.ui.address;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.core.address.AddressNetwork;
import com.cisoft.lazyorder.core.address.AddressNetwork.OnUpdateAddressFinish;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class UpdateAddressActivity extends BaseActivity {

	@BindView(id = R.id.et_input_name)
	private EditText mEtInputName;
	@BindView(id = R.id.et_input_phone)
	private EditText mEtInputPhone;
	@BindView(id = R.id.et_input_address)
	private EditText mEtInputAddress;
	@BindView(id = R.id.btn_sure_update_address, click = true)
	private Button mBtnSureUpdateAddress;
    private Dialog mWaitUpdateTipDialog;
    private Dialog mSuccessUpdateTipDialog;

    private AppContext mAppContext;
	private AddressNetwork mAddressNetwork;
	private AddressInfo mWantUpdateAddressObj;
    private User mLoginUser;

	public static final String WANT_UPDATE_ADDRESS_OBJ = "wantUpdateAddressObj";
	public static final String UPDATED_ADDRESS_OBJ = "updatedAddressObj";

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_address_update);
	}

	@Override
	public void initData() {
        mAppContext = (AppContext) getApplication();
		mAddressNetwork = new AddressNetwork(UpdateAddressActivity.this);
		mWantUpdateAddressObj = (AddressInfo) getIntent().getSerializableExtra(
				WANT_UPDATE_ADDRESS_OBJ);
        mLoginUser = mAppContext.getLoginInfo();
	}

	@Override
	public void initWidget() {
		initialDefText();
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure_update_address:
			doUpdateAddress();
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			UpdateAddressActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 自动填写上默认的输入框内容
	 */
	private void initialDefText() {
		mEtInputName.setText(mWantUpdateAddressObj.getName());
		mEtInputPhone.setText(mWantUpdateAddressObj.getPhone());
		mEtInputAddress.setText(mWantUpdateAddressObj.getAddress());
	}

	/**
	 * 执行修改地址操作
	 * 
	 */
	private void doUpdateAddress() {
		String inputName = mEtInputName.getText().toString();
		String inputPhone = mEtInputPhone.getText().toString();
		String inputAddress = mEtInputAddress.getText().toString();

		if (StringUtils.isEmpty(inputName)) {
			ViewInject.toast(getString(R.string.input_name_hint));
			return;
		}
		if (StringUtils.isEmpty(inputPhone)) {
			ViewInject.toast(getString(R.string.input_phone_hint));
			return;
		}
		if (StringUtils.isEmpty(inputAddress)) {
			ViewInject.toast(getString(R.string.input_address_hint));
			return;
		}
		if (!StringUtils.isPhone(inputPhone)) {
			ViewInject.toast(getString(R.string.toast_input_standard_phone));
			return;
		}

		mWantUpdateAddressObj.setName(inputName);
		mWantUpdateAddressObj.setPhone(inputPhone);
		mWantUpdateAddressObj.setAddress(inputAddress);
		networkUpdateAddress(mWantUpdateAddressObj);
	}

	/**
	 * 执行添加地址的网络操作
	 * 
	 */
	private void networkUpdateAddress(final AddressInfo wantUpdateAddressObj) {
		mAddressNetwork.updateAddressByAddrId(wantUpdateAddressObj,
				new OnUpdateAddressFinish() {

					@Override
					public void onPreStart() {
						showWaitTip();
					}

					@Override
					public void onSuccess() {
						closeWaitTip();
						showSuccessTip();

						new Handler().postDelayed(new Runnable() {
							public void run() {
								closeSuccessTip();

								Intent data = new Intent();
								Bundle bundle = new Bundle();
								bundle.putSerializable(UPDATED_ADDRESS_OBJ,
										wantUpdateAddressObj);
								data.putExtras(bundle);
								setResult(1, data);
								UpdateAddressActivity.this.finish();
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
	 * 显示正在添加的对话框提示
	 * 
	 */
	private void showWaitTip() {
		if (mWaitUpdateTipDialog == null) {
			mWaitUpdateTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.toast_being_update_address));
			mWaitUpdateTipDialog.setCancelable(false);
			mWaitUpdateTipDialog.setCanceledOnTouchOutside(false);
		}
		mWaitUpdateTipDialog.show();
	}

	/**
	 * 关闭正在添加的对话框提示
	 * 
	 */
	private void closeWaitTip() {
		if (mWaitUpdateTipDialog != null && mWaitUpdateTipDialog.isShowing()) {
			mWaitUpdateTipDialog.dismiss();
		}
	}

	/**
	 * 显示添加成功的对话框提示
	 * 
	 */
	private void showSuccessTip() {
		if (mSuccessUpdateTipDialog == null) {
			mSuccessUpdateTipDialog = DialogFactory.createSuccessToastDialog(
                    UpdateAddressActivity.this,
                    getString(R.string.toast_success_to_update_address));
			mSuccessUpdateTipDialog.setCancelable(false);
			mSuccessUpdateTipDialog.setCanceledOnTouchOutside(false);
		}
		mSuccessUpdateTipDialog.show();
	}

	/**
	 * 关闭添加成功的对话框提示
	 * 
	 */
	private void closeSuccessTip() {
		if (mSuccessUpdateTipDialog != null
				&& mSuccessUpdateTipDialog.isShowing()) {
			mSuccessUpdateTipDialog.dismiss();
		}
	}
}
