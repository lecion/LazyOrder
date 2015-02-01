package com.cisoft.lazyorder.ui.account;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.core.common.CommonNetwork;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends KJActivity {

    @BindView(id = R.id.et_mobile_number)
    private EditText etMobileNumber;

    @BindView(id = R.id.et_input_code)
    private EditText etInputCode;

    @BindView(id = R.id.btn_obtain_code, click = true)
    private Button btnObtainCode;

    @BindView(id = R.id.btn_sure_login, click = true)
    private Button btnSureLogin;

    private ObtainCodeBtnEnableTimer obtainCodeBtnEnableTimer;
    private CommonNetwork commonNetwork;
    private SmsContent smsContent;

    //重新发送验证码的间隔时间（单位：秒）
    public static final int OBTAIN_CODE_INTERVAL = 30;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {
        smsContent = new SmsContent(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContent);
        commonNetwork = new CommonNetwork(this);
    }

    @Override
    public void initWidget() {
        initActionBar();
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  用户登录");
    }

    /**
     * 获取验证码
     */
    private void obtainAuthCode() {
        String userMobileNumber = etMobileNumber.getText().toString();
        if (StringUtils.isEmpty(userMobileNumber)) {
            ViewInject.toast("请输入手机号码");
            return;
        }

        if(!StringUtils.isPhone(userMobileNumber)){
            ViewInject.toast("请输入正确的手机号码");
            return;
        }


        /* 创建一个正在发送短信验证码的提示对话框 */
        final Dialog obtainingTipDialog = DialogFactory.createWaitToastDialog(this, "正在发送短信验证码");
        obtainingTipDialog.setCancelable(false);
        obtainingTipDialog.setCanceledOnTouchOutside(false);
        obtainingTipDialog.show();

        commonNetwork.obtainSMSAuthCode(userMobileNumber, new CommonNetwork.OnSMSCodeSendFinish() {
            @Override
            public void onSuccess() {
                obtainingTipDialog.dismiss();
                ViewInject.toast("短信验证码已经发送,请等待...");

                //使获取验证码的按钮在规定时间内不可用
                if(obtainCodeBtnEnableTimer != null){
                    obtainCodeBtnEnableTimer.cancel();
                    obtainCodeBtnEnableTimer.start();
                } else {
                    obtainCodeBtnEnableTimer = new ObtainCodeBtnEnableTimer(OBTAIN_CODE_INTERVAL * 1000, 1000);
                    obtainCodeBtnEnableTimer.start();
                }
            }

            @Override
            public void onFailure(int stateCode) {
                obtainingTipDialog.dismiss();

                ViewInject.toast(commonNetwork.getResponseStateInfo(stateCode));
            }
        });
    }





    /**
     * 执行确认登录操作
     */
    private void doSureLogin() {
        final String userMobileNumber = etMobileNumber.getText().toString();
        String authCode = etInputCode.getText().toString();

        if (StringUtils.isEmpty(authCode)) {
            ViewInject.toast("请输入验证码");
            return;
        }

        if (StringUtils.isEmpty(userMobileNumber)) {
            ViewInject.toast("请输入手机号码");
            return;
        }

        if(!StringUtils.isPhone(userMobileNumber)){
            ViewInject.toast("请输入正确的手机号码");
            return;
        }

        /* 创建一个正在验证的提示对话框 */
        final Dialog verifyingTipDialog = DialogFactory.createWaitToastDialog(this, "正在验证");
        verifyingTipDialog.setCancelable(false);
        verifyingTipDialog.setCanceledOnTouchOutside(false);
        verifyingTipDialog.show();

        commonNetwork.verifyPhoneBySMSAuthCode(userMobileNumber, authCode, new CommonNetwork.OnPhoneVerifyFinish() {
            @Override
            public void onSuccess() {
                verifyingTipDialog.dismiss();

                verifySuccessDialog(userMobileNumber);
            }

            @Override
            public void onFailure(int stateCode) {
                verifyingTipDialog.dismiss();

                ViewInject.toast(commonNetwork.getResponseStateInfo(stateCode));
            }
        });
    }


    /**
     * 验证成功后的提示对话框
     */
    private void verifySuccessDialog(String userMobileNumber){
        final Dialog dialog = DialogFactory.createSuccessToastDialog(this, "登录成功");
        dialog.show();

        AppContext app = (AppContext) getApplication();
        app.loginByPhone(userMobileNumber);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();

                LoginActivity.this.finish();
            }
        }, 2000);

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure_login:
                doSureLogin();
                break;
            case R.id.btn_obtain_code:
                obtainAuthCode();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(smsContent);
        if(obtainCodeBtnEnableTimer != null) {
            obtainCodeBtnEnableTimer.cancel();
        }
    }

    /*
    * 监听短信数据库
    */
    class SmsContent extends ContentObserver {

        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[] { "_id", "address", "read", "body" },
                    " read=? and body like ?", new String[] {"0", "%懒人点餐%"}, "_id desc");

            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1");
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);

                etInputCode.setText(getDynamicPassword(smsBody));
            }

            // 在用managedQuery的时候，不能主动调用close()方法， 否则在4.0+的系统上， 会发生崩溃
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }

        /**
         * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
         *
         * @param str 短信内容
         * @return 截取得到的6位动态密码
         */
        public String getDynamicPassword(String str) {
            // 6是验证码的位数一般为六位
            Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
                    + 6 + "})(?![0-9])");
            Matcher m = continuousNumberPattern.matcher(str);
            String dynamicPassword = "";
            while (m.find()) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }

            return dynamicPassword;
        }
    }


    /**
     * 使获取短信验证码的按钮可用的倒计时内部类
     */
    class ObtainCodeBtnEnableTimer extends CountDownTimer {

        public ObtainCodeBtnEnableTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btnObtainCode.setEnabled(true);
            btnObtainCode.setText("重新获取");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnObtainCode.setEnabled(false);
            int leftWaitTime = (int)(millisUntilFinished / 1000);
            btnObtainCode.setText(leftWaitTime + "s 重新获取");
        }
    }
}
