package com.cisoft.lazyorder.ui.address;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.address.Address;
import com.cisoft.lazyorder.core.address.AddressNetwork;
import com.cisoft.lazyorder.core.address.AddressNetwork.OnAddressListLoadFinish;
import com.cisoft.lazyorder.core.address.AddressNetwork.OnDeleteAddressFinish;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.EmptyView;
import com.cisoft.lazyorder.widget.SlideListView;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ManageAddressActivity extends KJActivity implements
        SlideListView.OnItemClickListener {

	/*
	 * 进入此界面的模式 CHOICE_ADDRESS_MODE：
	 * 通过其他界面进入此界面来选择或编辑地址，点击某一个地址后会设置成默认地址并返回说选地址的内容
	 * LOOK_ADDRESS_MODE：只能进行编辑，点击某一个地址后会设置成默认地址而不会跳转
	 */
	public static final String ENTER_MODE = "enterMode";
	public static final int CHOOSE_ADDRESS_MODE = 1;
	public static final int LOOK_ADDRESS_MODE = 2;
	public static final int REQ_CODE_UPDATE_ADDRESS = 100;
	public static final int REQ_CODE_INSERT_ADDRESS = 200;
	public static final String CHOSE_ADDRESS = "choseAddress";

    @BindView(id = R.id.rl_root_view)
    private RelativeLayout mRootView;
    @BindView(id = R.id.ll_content)
    private LinearLayout mLlContent;
	@BindView(id = R.id.btn_want_insert_address, click = true)
	private Button mBtnWantInsertAddress;
    @BindView(id = R.id.lv_address_list)
    private SlideListView mLvAddressListView;
    private EmptyView mEmptyView;

	private AddressListAdapter mAddressListAdapter;
	private AddressNetwork mAddressNetwork;
	private AddressListAdapter.OnOperateBtnClickCallback onOperateBtnCallback;
	private List<Address> mAddressesListData;
	private Dialog mWaitTipDialog;
	private int mEnterMode = LOOK_ADDRESS_MODE;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_address_manage);
	}

	@Override
	public void initData() {
		mEnterMode = getIntent().getExtras().getInt(ENTER_MODE, LOOK_ADDRESS_MODE);
		mAddressNetwork = new AddressNetwork(ManageAddressActivity.this);
		mAddressesListData = new ArrayList<Address>();
	}

	@Override
	public void initWidget() {
		initialTitleBar();
		initialAddressListView();
		asyncLoadAddressListData();
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btn_want_insert_address:
			skipInsertAddressActivity();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CODE_INSERT_ADDRESS:
			if (resultCode == 1) {
				Address insertedAddressObj = (Address) data
						.getSerializableExtra(InsertAddressActivity.INSERTED_ADDRESS_OBJ);
				mAddressesListData.add(insertedAddressObj);
				mAddressListAdapter.refresh();
			}
			break;
		case REQ_CODE_UPDATE_ADDRESS:
			if (resultCode == 1) {
				Address updatedAddressObj = (Address) data
						.getSerializableExtra(UpdateAddressActivity.UPDATED_ADDRESS_OBJ);
				updateDataInAddressList(updatedAddressObj);
				mAddressListAdapter.refresh();
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	/**
	 * 初始化title bar
	 */
	private void initialTitleBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getString(R.string.title_activity_manage_address));
	}

	/**
	 * 初始化地址列表视图
	 */
	private void initialAddressListView() {
		onOperateBtnCallback = new AddressListAdapter.OnOperateBtnClickCallback() {
			@Override
			public void onDeleteBtnClick(Address wantDeleteAddress) {
				mLvAddressListView.turnToNormal();
                doDeleteAddress(wantDeleteAddress);
			}

			@Override
			public void onUpdateBtnClick(Address wantUpdateAddress) {
                mLvAddressListView.turnToNormal();
				skipUpdateAddressActivity(wantUpdateAddress);
			}
		};
		mAddressListAdapter = new AddressListAdapter(
				ManageAddressActivity.this, mAddressesListData,
				onOperateBtnCallback);
		mLvAddressListView.setAdapter(mAddressListAdapter);
		mLvAddressListView.setOnItemClickListener(this);
        mEmptyView = new EmptyView(this, mRootView);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                mEmptyView.hideEmptyView();
                asyncLoadAddressListData();
            }
        });
	}

	/**
	 * 加载地址列表数据
	 */
	private void asyncLoadAddressListData() {
		mAddressNetwork.loadAddrListByUId(1, new OnAddressListLoadFinish() {
			@Override
			public void onPreStart() {
				showWaitTip();
			}

			@Override
			public void onSuccess(List<Address> addresses) {
				closeWaitTip();
				if (addresses.size() == 0) {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
				} else {
                    mLlContent.setVisibility(View.VISIBLE);
					mAddressListAdapter.clearAll();
					mAddressListAdapter.addData(addresses);
					mAddressListAdapter.refresh();
				}
			}

			@Override
			public void onFailure(int stateCode) {
				closeWaitTip();
                mLlContent.setVisibility(View.GONE);
                ViewInject.toast(mAddressNetwork
                        .getResponseStateInfo(stateCode));
                if(stateCode == ApiConstants.RESPONSE_STATE_NOT_NET || stateCode == ApiConstants.RESPONSE_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
			}
		});
	}

	/**
	 * 执行删除地址操作
	 */
	private void doDeleteAddress(final Address wantDeleteAddressObj) {
        if(wantDeleteAddressObj.isDefault() == 1) {
            ViewInject.toast(getString(R.string.app_name));
            return;
        }
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.confirm_delete_address_title))
				.setMessage(getString(R.string.confirm_delete_address_tip))
				.setPositiveButton(getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mAddressNetwork.deleteAddressByAddrId(wantDeleteAddressObj.getId(),
										new OnDeleteAddressFinish() {

											@Override
											public void onPreStart() {
												showWaitTip();
											}

											@Override
											public void onSuccess() {
												closeWaitTip();
												mAddressesListData.remove(wantDeleteAddressObj);
												mAddressListAdapter.refresh();
												ViewInject
														.toast(getString(R.string.success_to_delete_address));
											}

											@Override
											public void onFailure(int stateCode) {
												closeWaitTip();
												ViewInject.toast(mAddressNetwork
														.getResponseStateInfo(stateCode));
											}
										});
							}
						}).setNegativeButton(getString(R.string.cancel), null)
				.create().show();
		;
	}

	/**
	 * 跳转到修改地址界面
	 * 
	 * @param wantUpdateAddressObj
	 */
	private void skipUpdateAddressActivity(Address wantUpdateAddressObj) {
		Intent i = new Intent(ManageAddressActivity.this,
				UpdateAddressActivity.class);
		i.putExtra(UpdateAddressActivity.WANT_UPDATE_ADDRESS_OBJ,
				wantUpdateAddressObj);
		startActivityForResult(i, REQ_CODE_UPDATE_ADDRESS);
	}

	/**
	 * 跳转到添加地址界面
	 */
	private void skipInsertAddressActivity() {
		startActivityForResult(new Intent(ManageAddressActivity.this,
				InsertAddressActivity.class), REQ_CODE_INSERT_ADDRESS);
	}

	/**
	 * 显示正在加载的等待提示对话框
	 * 
	 */
	private void showWaitTip() {
		if (mWaitTipDialog == null)
			mWaitTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.wait));
		mWaitTipDialog.show();
	}

	/**
	 * 关闭正在加载的等待提示对话框
	 * 
	 */
	private void closeWaitTip() {
		if (mWaitTipDialog != null && mWaitTipDialog.isShowing()) {
			mWaitTipDialog.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView adapterView, View view, int position,
			long id) {
		final Address checkedAddress = (Address) adapterView
				.getItemAtPosition(position);
		
		mAddressNetwork.setDefaultAddressByAddrId(checkedAddress.getId(),
				new AddressNetwork.OnSetDefaultAddressFinish() {
					@Override
					public void onPreStart() {
						showWaitTip();
					}

					@Override
					public void onSuccess() {
						closeWaitTip();
						checkedAddress.setDefault(1);
						updateDataInAddressList(checkedAddress);
						mAddressListAdapter.refresh();
						
						if(mEnterMode == CHOOSE_ADDRESS_MODE){
							Intent data = new Intent();
							Bundle bundle = new Bundle();
							bundle.putSerializable(CHOSE_ADDRESS,
									checkedAddress);
							data.putExtras(bundle);
							setResult(1, data);
						}
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
	 * 修改地址数据集合中的地址数据
	 */
	private void updateDataInAddressList(Address newAddressObj) {
        for (int i = 0; i < mAddressesListData.size(); i++) {
			Address tmpAddress = mAddressesListData.get(i);
			if(tmpAddress.isDefault() == 1 && newAddressObj.isDefault() == 1
                    && tmpAddress.getId() != newAddressObj.getId()) {
                tmpAddress.setDefault(0);
            }
			if (tmpAddress.getId() == newAddressObj.getId()){
				mAddressesListData.set(i, newAddressObj);
			}
		}
	}


}
