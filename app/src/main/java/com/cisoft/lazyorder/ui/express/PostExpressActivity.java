package com.cisoft.lazyorder.ui.express;

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
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.bean.express.SmsInfo;
import com.cisoft.lazyorder.bean.express.Express;
import com.cisoft.lazyorder.core.express.ExpressNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.address.InsertAddressActivity;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.util.DialogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class PostExpressActivity extends BaseActivity {

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
    private ExpressNetwork mExpressNetwork;

    public static final int REQ_CODE_IMPORT_SMS = 100;
    public static final int REQ_CODE_CHOOSE_ADDRESS = 200;
    public static final int REQ_CODE_INSERT_ADDRESS = 300;
    public static final String POSTED_EXPRESS = "submitedExpress";

    private int schoolId = 0;
    private AddressInfo mChoiceAddress;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_post_express);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mExpressNetwork = new ExpressNetwork(this);
        schoolId = mAppContext.getSchoolId();
    }

    @Override
    public void initWidget() {
        initAddressInfo();
    }

    /**
     * 初始化地址信息
     */
    private void initAddressInfo(){
        if(mAppContext.isLogin()) {
            User loginUser = mAppContext.getLoginInfo();
            mChoiceAddress = loginUser.getDefAddressInfo();
            // 可能默认地址为空
            if (mChoiceAddress != null) {
                mTvName.setText(mChoiceAddress.getName());
                mTvPhone.setText(mChoiceAddress.getPhone());
                mTvAddress.setText(mChoiceAddress.getAddress());
            }
        } else {
            if (!StringUtils.isEmpty(mAppContext.getRecentName())
                    && !StringUtils.isEmpty(mAppContext.getRecentPhone())
                    && !StringUtils.isEmpty(mAppContext.getRecentAddress())) {
                mChoiceAddress = new AddressInfo(mAppContext.getRecentName(),
                        mAppContext.getRecentPhone(),
                        mAppContext.getRecentAddress(), 0);
                mTvName.setText(mChoiceAddress.getName());
                mTvPhone.setText(mChoiceAddress.getPhone());
                mTvAddress.setText(mChoiceAddress.getAddress());
            }
        }
    }

    /**
     * 执行提交快递
     */
    private void doPostExpress() {
        String smsContent = mEtSMSContent.getText().toString();
        String extraMsg = mEtExtraMsg.getText().toString();

        if (StringUtils.isEmpty(smsContent)) {
            ViewInject.toast(getString(R.string.toast_sms_content_not_be_empty));
            return;
        }
        if (mChoiceAddress == null) {
            ViewInject.toast(getString(R.string.toast_address_info_not_be_empty));
            return;
        }
        String expressJsonStr = createExpressJsonStr(smsContent, extraMsg);
        mExpressNetwork.submitExpressOrderForServer(expressJsonStr,
                new ExpressNetwork.OnExpressSubmitFinish() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess() {
                closeWaitTip();
                showSuccessTip();

                final Express express = new Express();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        closeSuccessTip();
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(POSTED_EXPRESS, express);
                        data.putExtras(bundle);
                        setResult(1, data);
                        PostExpressActivity.this.finish();
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




    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post_express:
                doPostExpress();
                break;
            case R.id.rl_choice_address:
                if (mAppContext.isLogin()) {
                    ManageAddressActivity.startFrom(this, ManageAddressActivity.CHOOSE_ADDRESS_MODE,
                            REQ_CODE_CHOOSE_ADDRESS);
                } else {
                    InsertAddressActivity.startFrom(this, InsertAddressActivity.NORMAL_INSERT_MODE,
                            REQ_CODE_INSERT_ADDRESS);
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_express, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sms_import:
                startActivityForResult(new Intent(this, ChoiceSmsActivity.class),
                        REQ_CODE_IMPORT_SMS);
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
                    mChoiceAddress = (AddressInfo) data.getSerializableExtra(ManageAddressActivity.CHOSEN_ADDRESS);
                    mTvName.setText(mChoiceAddress.getName());
                    mTvPhone.setText(mChoiceAddress.getPhone());
                    mTvAddress.setText(mChoiceAddress.getAddress());
                }
                break;
            case REQ_CODE_INSERT_ADDRESS:
                if (resultCode == 1) {
                    mChoiceAddress = (AddressInfo) data
                            .getSerializableExtra(InsertAddressActivity.INSERTED_ADDRESS_OBJ);
                    mTvName.setText(mChoiceAddress.getName());
                    mTvPhone.setText(mChoiceAddress.getPhone());
                    mTvAddress.setText(mChoiceAddress.getAddress());
                    // 将填写的地址信息记录下来
                    mAppContext.setRecentName(mChoiceAddress.getName());
                    mAppContext.setRecentName(mChoiceAddress.getPhone());
                    mAppContext.setRecentName(mChoiceAddress.getAddress());
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
                    getString(R.string.toast_success_to_submit_express));
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
     * 创建Express json字符串
     * @return
     */
    private String createExpressJsonStr(String smsContent, String extraMsg){
        String jsonStr = "";

        try {
            JSONObject resultJsonObj = new JSONObject();
            resultJsonObj.put(ApiConstants.KEY_EXPRESS_NAME, mChoiceAddress.getName());
            resultJsonObj.put(ApiConstants.KEY_EXPRESS_PHONE, mChoiceAddress.getPhone());
            resultJsonObj.put(ApiConstants.KEY_EXPRESS_ADDRESS, mChoiceAddress.getAddress());
            resultJsonObj.put(ApiConstants.KEY_EXPRESS_SHIPPING_FEE, 1);
            resultJsonObj.put(ApiConstants.KEY_EXPRESS_EXTRA_MSG, extraMsg);
            resultJsonObj.put(ApiConstants.KEY_EXPRESS_SMS_CONTENT, smsContent);

            jsonStr = resultJsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }
}
