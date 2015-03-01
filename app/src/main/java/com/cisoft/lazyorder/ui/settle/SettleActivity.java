package com.cisoft.lazyorder.ui.settle;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.bean.order.Order;
import com.cisoft.lazyorder.bean.order.SettledOrder;
import com.cisoft.lazyorder.core.order.OrderNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.address.InsertAddressActivity;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.ui.order.OrderDetailActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.EmptyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.List;

public class SettleActivity extends BaseActivity {

    @BindView(id = R.id.rl_root_view)
    private RelativeLayout mRlRootView;
    @BindView(id = R.id.rl_hide_area)
    private RelativeLayout mRlHideArea;
    @BindView(id = R.id.rl_choice_address, click = true)
    private RelativeLayout mRlChoiceAddress;
    @BindView(id = R.id.tv_name)
    private TextView mTvName;
    @BindView(id = R.id.tv_phone)
    private TextView mTvPhone;
    @BindView(id = R.id.tv_address)
    private TextView mTvAddress;
    @BindView(id = R.id.et_extra_message)
    private EditText mEtExtraMsg;
    @BindView(id = R.id.tv_order_price)
    private TextView mTvOrderPrice;
    @BindView(id = R.id.tv_distribution_price)
    private TextView mTvDistributionPrice;
    @BindView(id = R.id.tv_deduction)
    private TextView mTvDeduction;
    @BindView(id = R.id.tv_settled_price)
    private TextView mTvSettledPrice;
    @BindView(id = R.id.btn_submit_order, click = true)
    private Button mBtnSubmitOrder;

    private Dialog mWaitTipDialog;
    private Dialog mSuccessTipDialog;
    private EmptyView mEmptyView;

    private AppContext mAppContext;
    private OrderNetwork mOrderNetwork;
    private SettledOrder mSettledOrder;
    private AddressInfo mChosenAddressInfo;
    private String mExtraMsg;

    public static final int REQ_CODE_CHOOSE_ADDRESS = 100;
    public static final int REQ_CODE_INSERT_ADDRESS = 200;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_settle);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mOrderNetwork = new OrderNetwork(this);
    }

    @Override
    public void initWidget() {
        mRlHideArea.setVisibility(View.GONE);
        mEmptyView = new EmptyView(this, mRlRootView);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                settleOrderByServer();
            }
        });
        settleOrderByServer();
    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.rl_choice_address:
                if (mAppContext.isLogin()) {
                    ManageAddressActivity.startFrom(this, ManageAddressActivity.CHOOSE_ADDRESS_MODE,
                            REQ_CODE_CHOOSE_ADDRESS);
                } else {
                    InsertAddressActivity.startFrom(this, InsertAddressActivity.NORMAL_INSERT_MODE,
                            REQ_CODE_INSERT_ADDRESS);
                }
                break;
            case R.id.btn_submit_order:
                doSubmitOrder();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_CHOOSE_ADDRESS:
                if (resultCode == 1) {
                    mChosenAddressInfo = (AddressInfo) data
                            .getSerializableExtra(ManageAddressActivity.CHOSEN_ADDRESS);
                    mTvName.setText(mChosenAddressInfo.getName());
                    mTvPhone.setText(mChosenAddressInfo.getPhone());
                    mTvAddress.setText(mChosenAddressInfo.getAddress());
                }
                break;
            case REQ_CODE_INSERT_ADDRESS:
                if (resultCode == 1) {
                    mChosenAddressInfo = (AddressInfo) data
                            .getSerializableExtra(InsertAddressActivity.INSERTED_ADDRESS_OBJ);
                    mTvName.setText(mChosenAddressInfo.getName());
                    mTvPhone.setText(mChosenAddressInfo.getPhone());
                    mTvAddress.setText(mChosenAddressInfo.getAddress());
                    // 将填写的地址信息记录下来
                    mAppContext.setRecentName(mChosenAddressInfo.getName());
                    mAppContext.setRecentName(mChosenAddressInfo.getPhone());
                    mAppContext.setRecentName(mChosenAddressInfo.getAddress());
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    /**
     * 初始化地址信息
     */
    private void initAddressInfo() {
        if(mAppContext.isLogin()) {
            User loginUser = mAppContext.getLoginInfo();
            mChosenAddressInfo = loginUser.getDefAddressInfo();
            // 可能默认地址为空
            if (mChosenAddressInfo != null) {
                mTvName.setText(mChosenAddressInfo.getName());
                mTvPhone.setText(mChosenAddressInfo.getPhone());
                mTvAddress.setText(mChosenAddressInfo.getAddress());
            }
        } else {
            if (!StringUtils.isEmpty(mAppContext.getRecentName())
                    && !StringUtils.isEmpty(mAppContext.getRecentPhone())
                    && !StringUtils.isEmpty(mAppContext.getRecentAddress())) {
                mChosenAddressInfo = new AddressInfo(mAppContext.getRecentName(),
                        mAppContext.getRecentPhone(),
                        mAppContext.getRecentAddress(), 0);
                mTvName.setText(mChosenAddressInfo.getName());
                mTvPhone.setText(mChosenAddressInfo.getPhone());
                mTvAddress.setText(mChosenAddressInfo.getAddress());
            }
        }
    }


    /**
     * 通过服务器结算账单信息
     */
    private void settleOrderByServer() {
        String settleOrderJsonStr = createSettleOrderJsonStr();
        mOrderNetwork.settleOrderByServer(settleOrderJsonStr, new OrderNetwork.OnOrderSettleFinish() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess(SettledOrder settledOrder) {
                closeWaitTip();
                mRlHideArea.setVisibility(View.VISIBLE);
                initAddressInfo();
                mSettledOrder = settledOrder;
                mTvOrderPrice.setText(String.valueOf(settledOrder.getOrderPrice()));
                mTvDistributionPrice.setText(String.valueOf(settledOrder.getShippingFee()));
                mTvDeduction.setText(String.valueOf(settledOrder.getDeduction()));
                mTvSettledPrice.setText(String.valueOf(settledOrder.getSettledPrice()));
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                closeWaitTip();
                ViewInject.toast(errorMsg);
                if (stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        });
    }

    /**
     * 提交订单
     */
    private void doSubmitOrder(){
        mExtraMsg = mEtExtraMsg.getText().toString();

        if (mChosenAddressInfo == null) {
            ViewInject.toast(getString(R.string.text_please_choose_address));
            return;
        }

        String submitOrderJsonStr = createSubmitOrderJsonStr();
        mOrderNetwork.submitOrderForServer(submitOrderJsonStr, new OrderNetwork.OnOrderSubmitFinish() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess(final Order order) {
                closeWaitTip();
                showSuccessTip();
                GoodsCart.getInstance().clear();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        closeSuccessTip();
                        OrderDetailActivity.startFrom(SettleActivity.this, order);
                        SettleActivity.this.finish();
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
     * 显示等待的提示对话框
     */
    private void showWaitTip() {
        if (mWaitTipDialog == null)
            mWaitTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.toast_wait));
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

    /**
     * 显示提交成功的对话框提示
     */
    private void showSuccessTip() {
        if (mSuccessTipDialog == null)
            mSuccessTipDialog = DialogFactory.createSuccessToastDialog(
                    this,
                    getString(R.string.toast_success_to_submit_order));
        mSuccessTipDialog.show();
    }

    /**
     * 关闭提交成功的对话框提示
     */
    private void closeSuccessTip() {
        if (mSuccessTipDialog != null
                && mSuccessTipDialog.isShowing()) {
            mSuccessTipDialog.dismiss();
        }
    }

    /**
     * 创建用于订单结算的json字符串
     * @return
     */
    private String createSettleOrderJsonStr(){
        String jsonStr = "";

        try {
            JSONObject resultJsonObj = new JSONObject();
            if (mAppContext.isLogin()) {
                resultJsonObj.put(ApiConstants.KEY_ORDER_USER_ID, mAppContext.getLoginUid());
                resultJsonObj.put(ApiConstants.KEY_ORDER_PHONE, mAppContext.getLoginInfo().getUserPhone());
            } else {
                resultJsonObj.put(ApiConstants.KEY_ORDER_USER_ID, 0);
                resultJsonObj.put(ApiConstants.KEY_ORDER_PHONE, "");
            }
            resultJsonObj.put(ApiConstants.KEY_ORDER_SHOP_ID, GoodsCart.getInstance().getShopId());

            JSONArray goodsListJsonArr = new JSONArray();
            JSONObject goodsJsonObj = null;
            List<Goods> goodsList = GoodsCart.getInstance().getAllGoods();
            for (Goods goods : goodsList) {
                goodsJsonObj = new JSONObject();
                goodsJsonObj.put(ApiConstants.KEY_ORDER_COM_ID, goods.getId());
                goodsJsonObj.put(ApiConstants.KEY_ORDER_GOODS_COUNT, goods.getOrderNum());
                goodsListJsonArr.put(goodsJsonObj);
            }
            resultJsonObj.put(ApiConstants.KEY_ORDER_GOODS_LIST, goodsListJsonArr);

            jsonStr = resultJsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }


    /**
     * 创建用于订单提交的json字符串
     * @return
     */
    public String createSubmitOrderJsonStr() {
        String jsonStr = "";

        try {
            JSONObject resultJsonObj = new JSONObject();

            if (mAppContext.isLogin()) {
                resultJsonObj.put(ApiConstants.KEY_ORDER_USER_ID, mAppContext.getLoginUid());
            }
            resultJsonObj.put(ApiConstants.KEY_ORDER_PHONE, mChosenAddressInfo.getPhone());
            resultJsonObj.put(ApiConstants.KEY_ORDER_ADDRESS, mChosenAddressInfo.getAddress());
            resultJsonObj.put(ApiConstants.KEY_ORDER_NAME, mChosenAddressInfo.getName());
            resultJsonObj.put(ApiConstants.KEY_ORDER_SHOP_ID, GoodsCart.getInstance().getShopId());
            resultJsonObj.put(ApiConstants.KEY_ORDER_EXTRA_MSG, mEtExtraMsg);

            JSONArray goodsListJsonArr = new JSONArray();
            JSONObject goodsJsonObj = null;
            List<Goods> goodsList = GoodsCart.getInstance().getAllGoods();
            for (Goods goods : goodsList) {
                goodsJsonObj = new JSONObject();
                goodsJsonObj.put(ApiConstants.KEY_ORDER_COM_ID, goods.getId());
                goodsJsonObj.put(ApiConstants.KEY_ORDER_GOODS_COUNT, goods.getOrderNum());
                goodsListJsonArr.put(goodsJsonObj);
            }
            resultJsonObj.put(ApiConstants.KEY_ORDER_GOODS_LIST, goodsListJsonArr);

            jsonStr = resultJsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }
}
