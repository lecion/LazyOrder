package com.cisoft.lazyorder.ui.goods;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cisoft.lazyorder.R;

/**
 * Created by Lecion on 10/27/14.
 */
public class OrderDialogFragment extends DialogFragment {

    public static OrderDialogFragment newInstance() {
        return new OrderDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods_order_layout, container, false);
        return v;
    }
}
