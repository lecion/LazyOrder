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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.address.Address;
import com.cisoft.lazyorder.bean.express.SmsInfo;
import com.cisoft.lazyorder.bean.order.ExpressOrder;
import com.cisoft.lazyorder.core.order.OrderNetwork;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.utils.SystemTool;

public class PostExpressActivity extends KJActivity {

    @BindView(id = R.id.et_sms_content)
    private EditText mEtSMSContent;
    @BindView(id = R.id.et_extra_message)
    private EditText mEtExtraMsg;
    @BindView(id = R.id.rl_choice_address, click = true)
    private RelativeLayout mRlChoiceAddress;
    @BindView(id = R.id.tv_name)
    private TextView mTvName;
    @BindView(id = R.id.tv_phone)
    private TextView mTvPhone;
    @BindView(id = R.id.tv_address)
    private TextView mTvAddress;

    @BindView(id = R.id.btn_post_express, click = true)
    private Button mBtnPostExpress;
    private Dialog mWaitTipDialog;
    private Dialog mSuccessTipDialog;

    private AppContext mAppContext;
    private OrderNetwork mOrderNetwork;

    public static final int REQ_CODE_IMPORT_SMS = 100;
    public static final int REQ_CODE_CHOOSE_ADDRESS = 200;
    public static final String POSTED_EXPRESS = "submitedExpress";

    private int schoolId;
    private Address mChoiceAddress;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_post_express);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mOrderNetwork = new OrderNetwork(this);
        schoolId = mAppContext.getSchoolId();
    }

    @Override
    public void initWidget() {
        initialTitleBar();
        initDefaultData();
    }


    /**
     * 初始化标题栏
     */
    private void initialTitleBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.nav_back_arrow);
        actionBar.setTitle("  提交快递");
    }


    /**
     * 从SP里读取默认的信息数据
     */
    private void initDefaultData(){

    }

    /**
     * 执行提交快递
     */
    private void doPostExpress() {
        String smsContent = mEtSMSContent.getText().toString();
        String extraMsg = mEtExtraMsg.getText().toString();

        if (StringUtils.isEmpty(smsContent)) {
            ViewInject.toast("请粘贴上你收到的快递短信");
            return;
        }
        if (mChoiceAddress == null) {
            ViewInject.toast("请选择地址");
            return;
        }

        /* 组装Express类的订单对象 */
        final ExpressOrder expressOrder = new ExpressOrder();
        expressOrder.setUserName(mChoiceAddress.getName());
        expressOrder.setUserPhone(mChoiceAddress.getPhone());
        expressOrder.setAddress(mChoiceAddress.getAddress());
        expressOrder.setExtraMsg(extraMsg);
        expressOrder.setSmsCotent(smsContent);
        expressOrder.setDeliveryMoney(1);

        mOrderNetwork.submitExpressOrderForServer(expressOrder, new OrderNetwork.OnExpressOrderSubmitFinish() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess() {
                closeWaitTip();
                showSuccessTip();
                expressOrder.setSubmitTime(SystemTool.getDataTime("yyyy-MM-dd HH:mm:ss"));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        closeSuccessTip();
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(POSTED_EXPRESS, expressOrder);
                        data.putExtras(bundle);
                        setResult(1, data);
                        PostExpressActivity.this.finish();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(int stateCode) {
                closeWaitTip();
                ViewInject.toast(mOrderNetwork.getResponseStateInfo(stateCode));
            }
        });
    }




    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post_express:
                doPostExpress();
                break;
            case R.id.rl_choice_address:
                skipChooseAddressActivity();
                break;
        }
    }


    private void skipChooseAddressActivity() {
        Intent i = new Intent(this, ManageAddressActivity.class);
        i.putExtra(ManageAddressActivity.ENTER_MODE, ManageAddressActivity.CHOOSE_ADDRESS_MODE);
        startActivityForResult(i, REQ_CODE_CHOOSE_ADDRESS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_express, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_sms_import:
                startActivityForResult(new Intent(this, ChoiceSmsActivity.class), REQ_CODE_IMPORT_SMS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_IMPORT_SMS:
                if (resultCode == 1) {
                    SmsInfo smsInfo = (SmsInfo) data.getSerializableExtra(ChoiceSmsActivity.IMPORT_SMS);
                    mEtSMSContent.setText(smsInfo.getSmsbody());
                }
                break;
            case REQ_CODE_CHOOSE_ADDRESS:
                if (resultCode == 1) {
                    mChoiceAddress = (Address) data.getSerializableExtra(ManageAddressActivity.CHOSE_ADDRESS);
                    mTvName.setText(mChoiceAddress.getName());
                    mTvPhone.setText(mChoiceAddress.getPhone());
                    mTvAddress.setText(mChoiceAddress.getAddress());
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 显示等待的提示对话框
     */
    private void showWaitTip() {
        if (mWaitTipDialog == null)
            mWaitTipDialog = DialogFactory.createWaitToastDialog(this,
                    getString(R.string.wait));
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
                    getString(R.string.success_to_submit_feedback));
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
}
