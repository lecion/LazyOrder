package com.cisoft.shop.expressorder.view;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cisoft.shop.R;
import com.cisoft.shop.bean.ExpressOrder;
import com.cisoft.shop.util.DeviceUtil;

/**
 * Created by Lecion on 2/28/15.
 */
public class OrderDetailDialog extends DialogFragment {
    private TextView tvMsg;
    private TextView tvOrderNumber;
    private TextView tvDistribution;
    private TextView tvContent;
    private TextView tvUserName;
    private TextView tvUserPhone;
    private TextView tvAddress;
    private TextView tvGetMsgTime;


    private ExpressOrder order;


    public static OrderDetailDialog newInstance(ExpressOrder order) {
        OrderDetailDialog dialog = new OrderDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", order);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            order = args.getParcelable("order");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_fragment_express_order_detail, container, false);
        initWidget(v);
        initData();
        return v;
    }

    private void initData() {
        tvOrderNumber.setText("No." + order.getExpressNumber());
        tvDistribution.setText("￥" + order.getDistributionPrice());
        tvContent.setText(order.getContent());
        tvUserName.setText(order.getUserName());
        tvUserPhone.setText(order.getUserPhone());
        tvAddress.setText(order.getAddress());
        tvMsg.setText(order.getMessage());
        tvGetMsgTime.setText(order.getGetMessageTime());
    }

    private void initWidget(View parent) {
        tvOrderNumber = (TextView) parent.findViewById(R.id.tv_order_number);
        tvDistribution = (TextView) parent.findViewById(R.id.tv_distribution_price_show);
        tvContent = (TextView) parent.findViewById(R.id.tv_content_show);
        tvUserName = (TextView) parent.findViewById(R.id.tv_user_name_show);
        tvUserPhone = (TextView) parent.findViewById(R.id.tv_user_phone_show);
        tvAddress = (TextView) parent.findViewById(R.id.tv_address_show);
        tvMsg = (TextView) parent.findViewById(R.id.tv_order_exress_msg_show);
        tvGetMsgTime = (TextView) parent.findViewById(R.id.tv_order_get_msg_time_show);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(DeviceUtil.getScreenWidth(getActivity()) / 10 * 9, window.getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
