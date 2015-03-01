package com.cisoft.lazyorder.ui.address;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.core.address.AddressNetwork;
import com.cisoft.lazyorder.core.address.AddressNetwork.OnAddressListLoadFinish;
import com.cisoft.lazyorder.core.address.AddressNetwork.OnDeleteAddressFinish;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.EmptyView;
import com.cisoft.lazyorder.widget.SlideListView;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ManageAddressActivity extends BaseActivity implements
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
	public static final String CHOSEN_ADDRESS = "choseAddress";

    @BindView(id = R.id.rl_root_view)
    private RelativeLayout mRootView;
    @BindView(id = R.id.ll_content)
    private LinearLayout mLlContent;
    @BindView(id = R.id.lv_address_list)
    private SlideListView mLvAddressListView;
    private EmptyView mEmptyView;
    private Dialog mWaitTipDialog;

	private AddressListAdapter mAddressListAdapter;
	private AddressNetwork mAddressNetwork;
	private AddressListAdapter.OnOperateBtnClickCallback onOperateBtnCallback;
	private List<AddressInfo> mAddressesListData;
	private AppContext mAppContext;
    private User mLoginUser;

	private int mEnterMode = LOOK_ADDRESS_MODE;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_address_manage);
	}

	@Override
	public void initData() {
		mEnterMode = getIntent().getIntExtra(ENTER_MODE, LOOK_ADDRESS_MODE);
		mAddressNetwork = new AddressNetwork(ManageAddressActivity.this);
		mAddressesListData = new ArrayList<AddressInfo>();
        mAppContext = (AppContext) getApplication();
        mLoginUser = mAppContext.getLoginInfo();
	}

	@Override
	public void initWidget() {
		initialAddressListView();
		loadAddressListData();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_address:
                InsertAddressActivity.startFrom(this, InsertAddressActivity.NETWORK_INSERT_MODE,
                        REQ_CODE_INSERT_ADDRESS);
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
				AddressInfo insertedAddressObj = (AddressInfo) data
						.getSerializableExtra(InsertAddressActivity.INSERTED_ADDRESS_OBJ);
				mAddressesListData.add(insertedAddressObj);
				mAddressListAdapter.refresh();
			}
			break;
		case REQ_CODE_UPDATE_ADDRESS:
			if (resultCode == 1) {
				AddressInfo updatedAddressObj = (AddressInfo) data
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
	 * 初始化地址列表视图
	 */
	private void initialAddressListView() {
		onOperateBtnCallback = new AddressListAdapter.OnOperateBtnClickCallback() {
			@Override
			public void onDeleteBtnClick(AddressInfo wantDeleteAddress) {
				mLvAddressListView.turnToNormal();
                doDeleteAddress(wantDeleteAddress);
			}

			@Override
			public void onUpdateBtnClick(AddressInfo wantUpdateAddress) {
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
                loadAddressListData();
            }
        });
	}

	/**
	 * 加载地址列表数据
	 */
	private void loadAddressListData() {
		mAddressNetwork.loadAddrListByUId(mLoginUser.getUserId(), new OnAddressListLoadFinish() {
			@Override
			public void onPreStart() {
				showWaitTip();
			}

			@Override
			public void onSuccess(List<AddressInfo> addresses) {
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
			public void onFailure(int stateCode, String errorMsg) {
				closeWaitTip();
                mLlContent.setVisibility(View.GONE);
                ViewInject.toast(errorMsg);
                if(stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
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
	private void doDeleteAddress(final AddressInfo wantDeleteAddressObj) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_confirm_delete_address_title))
				.setMessage(getString(R.string.dialog_confirm_delete_address_content))
				.setPositiveButton(getString(R.string.btn_confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
                                if(wantDeleteAddressObj.isDefault() == 1) {
                                    ViewInject.toast(getString(R.string.toast_can_not_delete_default_address));
                                    return;
                                }

								mAddressNetwork.deleteAddressByAddrId(mLoginUser.getUserId(), wantDeleteAddressObj.getId(),
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
														.toast(getString(R.string.toast_success_to_delete_address));
											}

											@Override
											public void onFailure(int stateCode, String errorMsg) {
												closeWaitTip();
												ViewInject.toast(errorMsg);
											}
										});
							}
						}).setNegativeButton(getString(R.string.btn_cancel), null)
				.create().show();
		;
	}


	/**
	 * 跳转到修改地址界面
	 * 
	 * @param wantUpdateAddressObj
	 */
	private void skipUpdateAddressActivity(AddressInfo wantUpdateAddressObj) {
		Intent i = new Intent(ManageAddressActivity.this,
				UpdateAddressActivity.class);
		i.putExtra(UpdateAddressActivity.WANT_UPDATE_ADDRESS_OBJ,
				wantUpdateAddressObj);
		startActivityForResult(i, REQ_CODE_UPDATE_ADDRESS);
	}

	/**
	 * 显示正在加载的等待提示对话框
	 * 
	 */
	private void showWaitTip() {
		if (mWaitTipDialog == null)
			mWaitTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.toast_wait));
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
		final AddressInfo checkedAddress = (AddressInfo) adapterView
				.getItemAtPosition(position);
		
		mAddressNetwork.setDefaultAddressByAddrId(mLoginUser.getUserId(), checkedAddress.getId(),
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
							bundle.putSerializable(CHOSEN_ADDRESS,
									checkedAddress);
							data.putExtras(bundle);
							setResult(1, data);
                            ManageAddressActivity.this.finish();
						}
					}

					@Override
					public void onFailure(int stateCode, String errorMsg) {
						closeWaitTip();
						ViewInject.toast(errorMsg);
					}
				});

	}

	/**
	 * 修改地址数据集合中的地址数据
	 */
	private void updateDataInAddressList(AddressInfo newAddressObj) {
        for (int i = 0; i < mAddressesListData.size(); i++) {
			AddressInfo tmpAddress = mAddressesListData.get(i);
			if(tmpAddress.isDefault() == 1 && newAddressObj.isDefault() == 1
                    && tmpAddress.getId() != newAddressObj.getId()) {
                tmpAddress.setDefault(0);
            }
			if (tmpAddress.getId() == newAddressObj.getId()){
				mAddressesListData.set(i, newAddressObj);
			}
		}
	}

    public static void startFrom(Activity activity, int enterMode) {
        Intent i = new Intent(activity, ManageAddressActivity.class);
        i.putExtra(ManageAddressActivity.ENTER_MODE, enterMode);
        activity.startActivity(i);
    }

    public static void startFrom(Activity activity, int enterMode, int requestCode) {
        Intent i = new Intent(activity, ManageAddressActivity.class);
        i.putExtra(ManageAddressActivity.ENTER_MODE, enterMode);
        activity.startActivityForResult(i, requestCode);
    }
}
