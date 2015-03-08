package com.cisoft.lazyorder.core.about;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Html;

import com.cisoft.lazyorder.bean.about.UpdateInfo;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.util.DialogFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;


/**
 * Created by comet on 2014/11/29.
 */
public class AboutNetwork extends BaseNetwork {

    public AboutNetwork(Context context) {
        super(context, ApiConstants.MODULE_SETTING);
    }


    /**
     * 得到最新的程序的版本信息
     * @return
     */
    public void checkVersionUpdate() {
        final Dialog tipDialog = DialogFactory.createWaitToastDialog(mContext, "正在检查更新...");
        tipDialog.show();

        getRequest(ApiConstants.METHOD_ABOUT_CHECK_UPDATE, null, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                tipDialog.dismiss();

                final UpdateInfo updateInfo;
                PackageInfo packageInfo;
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_DATA);
                    updateInfo = new UpdateInfo(dataObj);

                    packageInfo = mContext.getPackageManager().getPackageInfo(
                            mContext.getPackageName(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                //根据比较版本号来检查是否有更新
                if (updateInfo.getVersionCode() > packageInfo.versionCode) {
                    //显示提示更新的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("发现新版本：" + updateInfo.getVersionName());
                    builder.setMessage(Html.fromHtml(updateInfo.getUpdateContent()).toString());
                    builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            Intent intent = new Intent(mContext, AppDownloadService.class);
                            intent.putExtra(AppDownloadService.VERSION_NAME, updateInfo.getVersionName());
                            intent.putExtra(AppDownloadService.DOWNLOAD_URL, updateInfo.getDownloadUrl());
                            mContext.startService(intent);
                        }
                    });

                    builder.create().show();
                } else {
                    ViewInject.toast("当前已经是最新版本了");
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                tipDialog.dismiss();
                ViewInject.toast(errorMsg);
            }
        }, null);
    }


    /**
     * 提交反馈给服务器
     */
    public void submitFeedbackForServer(String feedbackContent, String contactMethod, final OnSubmitFeedbackFinish submitFinishCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ABOUT_FEEDBACK_CONTENT, feedbackContent);
        params.put(ApiConstants.KEY_ABOUT_CONTACT_METHOD, contactMethod);

        getRequest(ApiConstants.METHOD_ABOUT_SUBMIT_FEEDBACK, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (submitFinishCallback != null) {
                    submitFinishCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (submitFinishCallback != null) {
                    submitFinishCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if (submitFinishCallback != null) {
                    submitFinishCallback.onPreStart();
                }
            }
        });
    }


    /**
     * 提交完反馈后的回调
     */
    public interface OnSubmitFeedbackFinish {

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }
}
