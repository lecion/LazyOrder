package com.cisoft.lazyorder.ui.sureinfo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.address.Address;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.core.order.OrderNetwork;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.ui.order.OrderDetailActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.SystemTool;

public class SureInfoActivity extends KJActivity {

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
    @BindView(id = R.id.btnSureInfo, click = true)
    private Button btnSureInfo;

    private Address mChoseAddress;
    private String mInputExtraMsg;
    private AppContext appContext;
    private OrderNetwork orderNetwork;

    public static final int REQ_CODE_CHOOSE_ADDRESS = 100;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_sure_info);
    }


    @Override
    public void initData() {
        appContext = (AppContext) getApplication();
        orderNetwork = new OrderNetwork(this);
    }


    @Override
    public void initWidget() {
        initActionBar();
        initDefaultData();
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  确认信息");
    }

    /**
     * 从SP里读取默认的信息数据
     */
    private void initDefaultData(){
        //读取默认的姓名并设置上

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.rl_choice_address:
                skipManageAddressActivity();
                break;
            case R.id.btnSureInfo:
                doSubmitOrder();
                break;
        }
    }


    private void skipManageAddressActivity() {
        Intent i = new Intent(SureInfoActivity.this, ManageAddressActivity.class);
        i.putExtra(ManageAddressActivity.ENTER_MODE, ManageAddressActivity.CHOOSE_ADDRESS_MODE);
        startActivityForResult(i, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_CHOOSE_ADDRESS:
                if (resultCode == 1) {
                    mChoseAddress = (Address) data
                            .getSerializableExtra(ManageAddressActivity.CHOSE_ADDRESS);
                    mTvName.setText(mChoseAddress.getName());
                    mTvPhone.setText(mChoseAddress.getPhone());
                    mTvAddress.setText(mChoseAddress.getAddress());
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }



    /**
     * 提交订单
     */
    private void doSubmitOrder(){


        mInputExtraMsg = mEtExtraMsg.getText().toString();

        /* 创建一个正在提交的提示对话框 */
        final Dialog submitingTipDialog = DialogFactory.createWaitToastDialog(this, "正在提交订单,请稍等...");
        submitingTipDialog.setCancelable(false);
        submitingTipDialog.setCanceledOnTouchOutside(false);
        submitingTipDialog.show();

        final DishOrder dishOrder = new DishOrder();
        dishOrder.setShopId(GoodsCart.getInstance().getShopId());
        dishOrder.setShopName(GoodsCart.getInstance().getShopName());
        dishOrder.setGoodsList(GoodsCart.getInstance().getAllGoods());
        dishOrder.setDeliveryMoney(countDeliveryMoney());
        dishOrder.setMoneyAll(GoodsCart.getInstance().getTotalPrice() + dishOrder.getDeliveryMoney());

        /* 执行提交订单的网络请求 */
        orderNetwork.submitDishOrderForServer(dishOrder, new OrderNetwork.OnDishOrderSubmitFinish() {
           @Override
           public void onSuccess(String orderNum, String message, double moneyAll) {
               submitingTipDialog.dismiss();

               GoodsCart.getInstance().clear();//清空购物车
               dishOrder.setOrderNo(orderNum);//设置订单编号
//               dishOrder.setAddress(tvAddress.getText().toString());
               dishOrder.setSubmitTime(SystemTool.getDataTime("yyyy-MM-dd HH:mm:ss"));
               orderSubmitSuccessDialog(dishOrder);
           }
           @Override
           public void onFailure(int stateCode) {
               submitingTipDialog.dismiss();
               ViewInject.toast(orderNetwork.getResponseStateInfo(stateCode));
           }
       });

    }

    /**
     * 订单提交成功时的对话框
     */
    private void orderSubmitSuccessDialog(final DishOrder dishOrder){
        final Dialog dialog = DialogFactory.createSuccessToastDialog(this, "订单提交成功");
        dialog.show();

        //2秒后跳转到订单详情页
        new Handler().postDelayed(new Runnable() {
            public void run() {
            dialog.dismiss();

            Bundle bundle = new Bundle();
            bundle.putSerializable(OrderDetailActivity.DISH_ORDER, dishOrder);
            Intent intent = new Intent(SureInfoActivity.this, OrderDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            SureInfoActivity.this.finish();
            }
        }, 2000);

    }



    private int countDeliveryMoney(){
        return 1;
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
}
