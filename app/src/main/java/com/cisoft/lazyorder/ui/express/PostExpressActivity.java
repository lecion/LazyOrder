package com.cisoft.lazyorder.ui.express;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.order.ExpressOrder;
import com.cisoft.lazyorder.core.order.OrderService;
import com.cisoft.lazyorder.finals.SPConstants;
import com.cisoft.lazyorder.util.DialogFactory;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.activity.BaseActivity;
import org.kymjs.aframe.utils.PreferenceHelper;
import org.kymjs.aframe.utils.StringUtils;
import org.kymjs.aframe.utils.SystemTool;

public class PostExpressActivity extends BaseActivity {

    @BindView(id = R.id.et_sms_content)
    private EditText etSMSContent;

    @BindView(id = R.id.et_extra_message)
    private EditText etExtraMsg;

    @BindView(id = R.id.btn_post_express, click = true)
    private Button btnPostExpress;

    private OrderService orderService;


    public PostExpressActivity(){
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_post_express);
    }

    @Override
    protected void initData() {
        orderService = new OrderService(this);
    }

    @Override
    protected void initWidget() {
        initActionBar();
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.nav_back_arrow);
        actionBar.setTitle("  提交快递");
    }

    /**
     * 执行提交快递
     */
    private void doPostExpress() {
        String smsContent = etSMSContent.getText().toString();
        if (StringUtils.isEmpty(smsContent)) {
            ViewInject.toast("请粘贴上你收到的快递短信");
            return;
        }

        String extraMsg = etExtraMsg.getText().toString();

        /* 获取登录的用户信息 */
        String recentName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_NAME, null);
        String recentPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, null);
        String recentBuildName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_NAME, null);
        int recentBuildingId = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_ID, 0);
        String recentRoomNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ROOM_NUM, null);


        /* 创建一个正在提交的提示对话框 */
        final Dialog submitingTipDialog = DialogFactory.createToastDialog(this, "正在提交快递订单,请稍等...");
        submitingTipDialog.setCancelable(false);
        submitingTipDialog.setCanceledOnTouchOutside(false);
        submitingTipDialog.show();

        /* 组装Express类的订单对象 */
        final ExpressOrder expressOrder = new ExpressOrder();
        expressOrder.setUserName(recentName);
        expressOrder.setUserPhone(recentPhoneNum);
        expressOrder.setBuildingId(recentBuildingId);
        expressOrder.setDormitory(recentRoomNum);
        expressOrder.setAddress(recentBuildName + recentRoomNum);
        expressOrder.setExtraMsg(extraMsg);
        expressOrder.setSmsCotent(smsContent);
        expressOrder.setDeliveryMoney(1);

        /* 执行提交订单的网络请求 */
        orderService.submitExpressOrderForServer(expressOrder, new OrderService.OnExpressOrderSubmitFinish() {
            @Override
            public void onSuccess() {
                submitingTipDialog.dismiss();

                expressOrder.setSubmitTime(SystemTool.getDataTime("yyyy-MM-dd HH:mm:ss"));
                orderSubmitSuccessDialog(expressOrder);
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
    private void orderSubmitSuccessDialog(final ExpressOrder expressOrder){
        final Dialog dialog = DialogFactory.createSuccessToastDialog(this, "快递订单提交成功");
        dialog.show();

        //2秒后跳转到历史快递订单页
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();

                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ExpressOrderListFragment.EXPRESS_ORDER, expressOrder);
                data.putExtras(bundle);
                setResult(ExpressOrderListFragment.RESULT_CODE_POST_EXPRESS_SUCCESS, data);
                PostExpressActivity.this.finish();
            }
        }, 2000);

    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post_express:
                doPostExpress();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
