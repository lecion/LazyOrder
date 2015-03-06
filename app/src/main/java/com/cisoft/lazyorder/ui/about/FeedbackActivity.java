package com.cisoft.lazyorder.ui.about;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.core.about.AboutNetwork;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.util.DialogFactory;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by comet on 2014/12/3.
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(id = R.id.et_input_feedback_content)
    private EditText mEtFeedbackContent;
    @BindView(id = R.id.et_input_contact_method)
    private EditText mEtContactMethod;
    @BindView(id = R.id.btn_submit_feedback, click = true)
    private Button mBtnSubmitFeedback;
    private Dialog mWaitTipDialog;
    private Dialog mSuccessTipDialog;

    private AboutNetwork mAboutNetwork;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_feedback);
    }


    @Override
    public void initData() {
        mAboutNetwork = new AboutNetwork(this);
    }


    @Override
    public void initWidget() {
        setTitle(R.string.title_activity_feedback);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_feedback:
                doSubmitFeedback();
                break;
        }
    }

    /**
     * 执行提交反馈
     */
    private void doSubmitFeedback() {
        String feedContent = mEtFeedbackContent.getText().toString();
        String contactMethod = mEtContactMethod.getText().toString();

        /* 检查信息是否填写完整 */
        if (StringUtils.isEmpty(feedContent)) {
            ViewInject.toast(getText(R.string.input_feedback_content_hint).toString());
            return;
        }

        if (StringUtils.isEmpty(contactMethod)) {
            ViewInject.toast(getText(R.string.input_feedback_contact_method_hint).toString());
            return;
        } else {
            //只能是电话号码或者邮箱
            if (!StringUtils.isPhone(contactMethod) && !StringUtils.isEmail(contactMethod)) {
                ViewInject.toast(getText(R.string.input_feedback_contact_method_error_hint).toString());
                return;
            }
        }

        mAboutNetwork.submitFeedbackForServer(feedContent, contactMethod, new AboutNetwork.OnSubmitFeedbackFinish() {
            @Override
            public void onPreStart() {
                showWaitTip();
            }

            @Override
            public void onSuccess() {
                closeWaitTip();
                showSuccessTip();

                //2秒后关闭当前页面
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        closeSuccessTip();
                        FeedbackActivity.this.finish();
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
                    getString(R.string.toast_success_to_submit_feedback));
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

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        activity.startActivity(intent);
    }
}
