package com.cisoft.lazyorder.ui.sureinfo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.bean.sureinfo.Build;
import com.cisoft.lazyorder.core.sureinfo.BuildService;
import com.cisoft.lazyorder.core.order.OrderService;
import com.cisoft.lazyorder.finals.SPConstants;
import com.cisoft.lazyorder.ui.main.MainActivity;
import com.cisoft.lazyorder.ui.orderdetail.OrderDetailActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.ChoiceAddressDialog;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.KJActivityManager;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.utils.PreferenceHelper;
import org.kymjs.aframe.utils.StringUtils;

public class SureInfoActivity extends BaseActivity {


    @BindView(id = R.id.tvPhoneNum)
    private TextView tvPhoneNum;
    @BindView(id = R.id.etInputPhoneNum)
    private EditText etInputPhoneNum;
    @BindView(id = R.id.btnSelectPhoneNum, click = true)
    private Button btnSelectPhoneNum;

    @BindView(id = R.id.tvName)
    private TextView tvName;
    @BindView(id = R.id.etInputName)
    private EditText etInputName;
    @BindView(id = R.id.btnChangeName, click = true)
    private Button btnChangeName;

    @BindView(id = R.id.tvAddress)
    private TextView tvAddress;
    @BindView(id = R.id.btnSelectAddress, click = true)
    private Button btnSelectAddress;

    @BindView(id = R.id.btnSureInfo, click = true)
    private Button btnSureInfo;

    //是否是正在输入电话号码或姓名的状态标记位
    private boolean isInputPhoneNum = false;
    private boolean isInputName = false;

    private int schoolId = 0;
    private BuildService buildService;
    private OrderService orderService;
    //选择联系地址的对话框
    private Dialog choiceAddressDialog;

    /* 以下是用于保存提交的信息储存变量 */
    private String submitUserName;
    private String submitUserPhone;
    private int submitBuildingId = 0;
    private String submitRoomNum;
    private String submitExtraMsg = "";


    public SureInfoActivity(){
        setHiddenActionBar(false);
    }


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_sure_info);
    }


    @Override
    protected void initData() {
        buildService = new BuildService(this);
        orderService = new OrderService(this);
    }


    @Override
    protected void initWidget() {
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
        String defName = getResources().getString(R.string.input_name_hint);
        String recentName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_NAME, null);
        if (!StringUtils.isEmpty(recentName)) {
            defName = recentName;
            submitUserName = recentName;
        }
        tvName.setText(defName);

        //读取默认的电话号码并设置上
        String defPhoneNum = getResources().getString(R.string.input_phone_num_hint);
        String recentPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, null);
        if (!StringUtils.isEmpty(recentPhoneNum)) {
            defPhoneNum = recentPhoneNum;
            submitUserPhone = recentPhoneNum;
        }
        tvPhoneNum.setText(defPhoneNum);

        //读取默认的联系地址并设置上
        String defAddress = getResources().getString(R.string.input_contact_address_hint);
        String recentBuildName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_NAME, null);
        String recentRoomNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ROOM_NUM, null);
        if(!StringUtils.isEmpty(recentBuildName) && !StringUtils.isEmpty(recentRoomNum)){
            defAddress = recentBuildName + recentRoomNum;
            submitBuildingId = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_BUILDING_ID, 0);
            submitRoomNum = recentRoomNum;
        }
        tvAddress.setText(defAddress);


        //读取保存的学校id
        schoolId = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_SCHOOL_ID, 1);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btnSelectPhoneNum:
                if(isInputPhoneNum)
                    savePhoneNum();
                else
                    inputPhoneNum();
                break;
            case R.id.btnChangeName:
                if(isInputName)
                    saveName();
                else
                    inputName();
                break;
            case R.id.btnSelectAddress:
                selectAddress();
                break;
            case R.id.btnSureInfo:
                doSubmitOrder();
                break;
        }
    }

    /**
     * 输入电话号码
     */
    private void inputPhoneNum(){
        tvPhoneNum.setVisibility(View.GONE);
        etInputPhoneNum.setVisibility(View.VISIBLE);
        btnSelectPhoneNum.setText("确定");
        isInputPhoneNum = true;
    }

    /**
     * 电击确定后保存输入的电话号码
     */
    private void savePhoneNum(){
        tvPhoneNum.setVisibility(View.VISIBLE);
        etInputPhoneNum.setVisibility(View.GONE);
        btnSelectPhoneNum.setText("更改");
        isInputPhoneNum = false;
        String phoneNum = etInputPhoneNum.getText().toString();
        if(StringUtils.isEmpty(phoneNum)){
            phoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_PHONE_NUM, getResources().getString(R.string.input_phone_num_hint));
        } else {
            PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_PHONE_NUM, phoneNum);

            submitUserPhone = phoneNum;
        }
        tvPhoneNum.setText(phoneNum);
    }

    /**
     * 输入姓名
     */
    private void inputName(){
        tvName.setVisibility(View.GONE);
        etInputName.setVisibility(View.VISIBLE);
        btnChangeName.setText("确定");
        isInputName = true;
    }

    /**
     * 点击确定后保存输入的姓名
     */
    private void saveName(){
        tvName.setVisibility(View.VISIBLE);
        etInputName.setVisibility(View.GONE);
        btnChangeName.setText("更改");
        isInputName = false;
        String name = etInputName.getText().toString();
        if(StringUtils.isEmpty(name)){
            name = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_NAME, getResources().getString(R.string.input_name_hint));
        } else {
            PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                    SPConstants.KEY_RECENT_NAME, name);
            submitUserName = name;
        }
        tvName.setText(name);
    }

    /**
     * 创建一个选择楼栋信息的对话框
     * @return
     */
    private void selectAddress(){
        if (choiceAddressDialog == null) {
            ChoiceAddressDialog.Builder builder = new ChoiceAddressDialog.Builder(this);
            builder.setBuildService(buildService);
            builder.setSchoolId(schoolId);
            builder.setTitle("请确认联系地址");
            builder.setOnAddressSelectedListener(new ChoiceAddressDialog.OnAddressSelectedListener() {
                @Override
                public void onSelected(Build selectedBuild, int roomNum) {
                    String address = selectedBuild.getName() + roomNum;
                    tvAddress.setText(address);

                    PreferenceHelper.write(SureInfoActivity.this, SPConstants.SP_FILE_NAME,
                            SPConstants.KEY_RECENT_BUILDING_ID, selectedBuild.getId());
                    PreferenceHelper.write(SureInfoActivity.this, SPConstants.SP_FILE_NAME,
                            SPConstants.KEY_RECENT_BUILDING_NAME, selectedBuild.getName());
                    PreferenceHelper.write(SureInfoActivity.this, SPConstants.SP_FILE_NAME,
                            SPConstants.KEY_RECENT_ROOM_NUM, String.valueOf(roomNum));

                    submitBuildingId = selectedBuild.getId();
                    submitRoomNum = String.valueOf(roomNum);
                }
            });
            choiceAddressDialog = builder.create();
            choiceAddressDialog.setCancelable(false);
            choiceAddressDialog.setCanceledOnTouchOutside(false);
        }

        choiceAddressDialog.show();
    }


    /**
     * 提交订单
     */
    private void doSubmitOrder(){
        /* 判断信息是否填写完整 */
        if (StringUtils.isEmpty(submitUserName)) {
            Toast.makeText(this, R.string.input_name_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtils.isEmpty(submitUserPhone)) {
            Toast.makeText(this, R.string.input_phone_num_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        if (submitBuildingId == 0 || StringUtils.isEmpty(submitRoomNum)){
            Toast.makeText(this, R.string.input_contact_address_hint, Toast.LENGTH_SHORT).show();
            return;
        }


        /* 创建一个正在提交的提示对话框 */
        final Dialog submitingTipDialog = DialogFactory.createToastDialog(this, "正在提交订单,请稍等...");
        submitingTipDialog.setCancelable(false);
        submitingTipDialog.setCanceledOnTouchOutside(false);
        submitingTipDialog.show();

        /* 组装商品类的订单对象 */
        final DishOrder dishOrder = new DishOrder();
        dishOrder.setUserName(submitUserName);
        dishOrder.setUserPhone(submitUserPhone);
        dishOrder.setBuildingId(submitBuildingId);
        dishOrder.setDormitory(submitRoomNum);
        dishOrder.setExtraMsg(submitExtraMsg);
        dishOrder.setShopId(GoodsCart.getInstance().getShopId());
        dishOrder.setShopName(GoodsCart.getInstance().getShopName());
        dishOrder.setGoodsList(GoodsCart.getInstance().getAllGoods());
        dishOrder.setDeliveryMoney(countDeliveryMoney());
        dishOrder.setMoneyAll(GoodsCart.getInstance().getTotalPrice() + dishOrder.getDeliveryMoney());

        /* 执行提交订单的网络请求 */
        orderService.submitDishOrderForServer(dishOrder, new OrderService.OnDishOrderSubmitFinish() {
           @Override
           public void onSuccess(String orderNum, String message, double moneyAll) {
               submitingTipDialog.dismiss();

               GoodsCart.getInstance().clear();//清空购物车
               dishOrder.setOrderNo(orderNum);//设置订单编号
               orderSubmitSuccessDialog(dishOrder);
           }
           @Override
           public void onFailure(int stateCode) {
               submitingTipDialog.dismiss();
               ViewInject.toast(orderService.getResponseStateInfo(stateCode));
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
            KJActivityManager.create().finishOthersActivity(MainActivity.class);
            }
        }, 2000);

    }



    private int countDeliveryMoney(){
        return 1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sure_info, menu);
        return true;
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
