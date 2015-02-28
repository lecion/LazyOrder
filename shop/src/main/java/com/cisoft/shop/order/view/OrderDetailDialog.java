package com.cisoft.shop.order.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.cisoft.myapplication.R;
import com.cisoft.shop.util.DeviceUtil;

/**
 * Created by Lecion on 2/28/15.
 */
public class OrderDetailDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_fragment_order_detail, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(DeviceUtil.getScreenHeight(getActivity()) / 3, window.getAttributes().height);
    }
}
