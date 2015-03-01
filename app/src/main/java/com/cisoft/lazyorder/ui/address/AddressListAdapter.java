package com.cisoft.lazyorder.ui.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.address.AddressInfo;

import java.util.List;

public class AddressListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AddressInfo> mData;
	private OnOperateBtnClickCallback mOperateBtnCallback;

	public AddressListAdapter(Context context, List<AddressInfo> data,
			OnOperateBtnClickCallback operateBtnCallback) {
		mContext = context;
		mData = data;
		mOperateBtnCallback = operateBtnCallback;
	}

	public void addData(List<AddressInfo> addData) {
		mData.addAll(addData);
	}

	public void clearAll() {
		mData.clear();
	}

	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public AddressInfo getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.activity_address_list_cell, null);
			holder = new ViewHolder();
			holder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.mTvPhone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			holder.mTvAddress = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.mIvIsDefault = (ImageView) convertView
					.findViewById(R.id.iv_is_default);
			holder.mBtnUpdateAddress = (ImageButton) convertView
					.findViewById(R.id.btn_update_address);
			holder.mBtnDeleteAddress = (ImageButton) convertView
					.findViewById(R.id.btn_delete_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final AddressInfo address = mData.get(position);
		holder.mTvName.setText(address.getName());
		holder.mTvPhone.setText(address.getPhone());
		holder.mTvAddress.setText(address.getAddress());
		if (address.isDefault() == 1)
			holder.mIvIsDefault.setVisibility(View.VISIBLE);
		else
			holder.mIvIsDefault.setVisibility(View.INVISIBLE);
		holder.mBtnDeleteAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mOperateBtnCallback.onDeleteBtnClick(address);
			}
		});
		holder.mBtnUpdateAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mOperateBtnCallback.onUpdateBtnClick(address);
			}
		});

		return convertView;
	}

	public static class ViewHolder {
		public TextView mTvName;
		public TextView mTvPhone;
		public TextView mTvAddress;
		public ImageView mIvIsDefault;
		public ImageButton mBtnDeleteAddress;
		public ImageButton mBtnUpdateAddress;
	}

    public interface OnOperateBtnClickCallback {

        public void onDeleteBtnClick(AddressInfo wantDeleteAddress);

        public void onUpdateBtnClick(AddressInfo wantUpdateAddress);
    }
}
