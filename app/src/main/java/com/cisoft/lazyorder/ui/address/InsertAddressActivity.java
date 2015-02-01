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

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.address.Address;
import com.cisoft.lazyorder.core.address.AddressNetwork;
import com.cisoft.lazyorder.core.address.AddressNetwork.OnInsertAddressFinish;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class InsertAddressActivity extends KJActivity {

	@BindView(id = R.id.et_input_name)
	private EditText mEtInputName;
	@BindView(id = R.id.et_input_phone)
	private EditText mEtInputPhone;
	@BindView(id = R.id.et_input_address)
	private EditText mEtInputAddress;
	@BindView(id = R.id.btn_sure_insert_address, click = true)
	private Button mBtnSureInsertAddress;

	private AddressNetwork mAddressNetwork;
	private Dialog mWaitInsertTipDialog;
	private Dialog mSuccessInsertTipDialog;

	public static final String INSERTED_ADDRESS_OBJ = "insertAddress";

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_address_insert);
	}

	@Override
	public void initData() {
		mAddressNetwork = new AddressNetwork(InsertAddressActivity.this);
	}

	@Override
	public void initWidget() {
		initialTitleBar();
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure_insert_address:
			doInsertAddress();
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 初始化title bar
	 */
	private void initialTitleBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getString(R.string.title_activity_insert_address));
	}

	/**
	 * 执行添加地址操作
	 * 
	 */
	private void doInsertAddress() {
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
			ViewInject.toast(getString(R.string.input_standard_phone_hint));
			return;
		}

		Address addressObj = new Address();
		addressObj.setName(inputName);
		addressObj.setPhone(inputPhone);
		addressObj.setAddress(inputAddress);
		networkAddAddress(addressObj);
	}

	/**
	 * 执行添加地址的网络操作
	 * 
	 * @param addressObj
	 */
	private void networkAddAddress(final Address addressObj) {
		mAddressNetwork.insertAddressByUserId(1, addressObj,
				new OnInsertAddressFinish() {
					@Override
					public void onPreStart() {
						showWaitTip();
					}

					@Override
					public void onSuccess(int addrId) {
						closeWaitTip();
						showSuccessTip();
						addressObj.setId(addrId);
						new Handler().postDelayed(new Runnable() {
							public void run() {
								closeSuccessTip();

								Intent data = new Intent();
								Bundle bundle = new Bundle();
								bundle.putSerializable(INSERTED_ADDRESS_OBJ,
										addressObj);
								data.putExtras(bundle);
								setResult(1, data);
								InsertAddressActivity.this.finish();
							}
						}, 2000);
					}

					@Override
					public void onFailure(int stateCode) {
						closeWaitTip();
						ViewInject.toast(mAddressNetwork
								.getResponseStateInfo(stateCode));
					}
				});
	}

	/**
	 * 显示正在添加的对话框提示
	 * 
	 */
	private void showWaitTip() {
		if (mWaitInsertTipDialog == null) {
			mWaitInsertTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.being_insert_address_tip));
			mWaitInsertTipDialog.setCancelable(false);
			mWaitInsertTipDialog.setCanceledOnTouchOutside(false);
		}
		mWaitInsertTipDialog.show();
	}

	/**
	 * 关闭正在添加的对话框提示
	 * 
	 */
	private void closeWaitTip() {
		if (mWaitInsertTipDialog != null && mWaitInsertTipDialog.isShowing()) {
			mWaitInsertTipDialog.dismiss();
		}
	}

	/**
	 * 显示添加成功的对话框提示
	 * 
	 */
	private void showSuccessTip() {
		if (mSuccessInsertTipDialog == null) {
			mSuccessInsertTipDialog = DialogFactory.createSuccessToastDialog(
                    InsertAddressActivity.this,
                    getString(R.string.success_to_insert_address));
			mSuccessInsertTipDialog.setCancelable(false);
			mSuccessInsertTipDialog.setCanceledOnTouchOutside(false);
		}
		mSuccessInsertTipDialog.show();
	}

	/**
	 * 关闭添加成功的对话框提示
	 * 
	 */
	private void closeSuccessTip() {
		if (mSuccessInsertTipDialog != null
				&& mSuccessInsertTipDialog.isShowing()) {
			mSuccessInsertTipDialog.dismiss();
		}
	}
}
