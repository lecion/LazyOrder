package com.cisoft.lazyorder.ui.address;

import android.app.Activity;
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
import com.cisoft.lazyorder.core.address.AddressNetwork.OnInsertAddressFinish;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class InsertAddressActivity extends BaseActivity {


    /*
	 * 进入此界面的模式
	 * NETWORK_INSERT_MODE：
	 *      需要通过网络提交到服务器且返回给上一个页面
	 * NORMAL_INSERT_MODE：
	 *      直接将添加的地址信息返回给上一个页面
	 */
    public static final String ENTER_MODE = "enterMode";
    public static final int NETWORK_INSERT_MODE = 1;
    public static final int NORMAL_INSERT_MODE = 2;


	@BindView(id = R.id.et_input_name)
	private EditText mEtInputName;
	@BindView(id = R.id.et_input_phone)
	private EditText mEtInputPhone;
	@BindView(id = R.id.et_input_address)
	private EditText mEtInputAddress;
	@BindView(id = R.id.btn_sure_insert_address, click = true)
	private Button mBtnSureInsertAddress;
    private Dialog mWaitInsertTipDialog;
    private Dialog mSuccessInsertTipDialog;

    private AppContext mAppContext;
	private AddressNetwork mAddressNetwork;
	private User mLoginUser;

	public static final String INSERTED_ADDRESS_OBJ = "insertAddress";

    private int mEnterMode = NORMAL_INSERT_MODE;


	@Override
	public void setRootView() {
		setContentView(R.layout.activity_address_insert);
	}

	@Override
	public void initData() {
        mEnterMode = getIntent().getIntExtra(ENTER_MODE, NORMAL_INSERT_MODE);
		mAddressNetwork = new AddressNetwork(InsertAddressActivity.this);
        mAppContext = (AppContext) getApplication();
        mLoginUser = mAppContext.getLoginInfo();
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
			ViewInject.toast(getString(R.string.toast_input_standard_phone));
			return;
		}

		AddressInfo addressObj = new AddressInfo();
		addressObj.setName(inputName);
		addressObj.setPhone(inputPhone);
		addressObj.setAddress(inputAddress);

        if(mEnterMode == NETWORK_INSERT_MODE){
            networkAddAddress(addressObj);
        } else {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(INSERTED_ADDRESS_OBJ,
                    addressObj);
            data.putExtras(bundle);
            setResult(1, data);
            InsertAddressActivity.this.finish();
        }
	}

	/**
	 * 执行添加地址的网络操作
	 * 
	 * @param addressObj
	 */
	private void networkAddAddress(final AddressInfo addressObj) {
		mAddressNetwork.insertAddressByUserId(mLoginUser.getUserId(), addressObj,
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
		if (mWaitInsertTipDialog == null) {
			mWaitInsertTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.toast_being_insert_address));
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
                    getString(R.string.toast_success_to_insert_address));
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

    public static void startFrom(Activity activity, int enterMode) {
        Intent i = new Intent(activity, InsertAddressActivity.class);
        i.putExtra(InsertAddressActivity.ENTER_MODE, enterMode);
        activity.startActivity(i);
    }

    public static void startFrom(Activity activity, int enterMode, int requestCode) {
        Intent i = new Intent(activity, InsertAddressActivity.class);
        i.putExtra(InsertAddressActivity.ENTER_MODE, enterMode);
        activity.startActivityForResult(i, requestCode);
    }
}
