package com.cisoft.lazyorder.core.about;

import android.content.Context;
import com.cisoft.lazyorder.bean.about.UpdateInfo;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;


/**
 * Created by comet on 2014/11/29.
 */
public class AboutNetwork extends BaseNetwork {

    public AboutNetwork(Context context) {
        super(context, ApiConstants.MODULE_ABOUT);
    }


    /**
     * 得到最新的程序的版本信息
     * @return
     */
    public void obtainLastestVersionInfo(final OnLoadVersionInfoFinish loadFinishCallback) {

        getRequest(ApiConstants.METHOD_ABOUT_CHECK_UPDATE, null, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_DATA);

                    UpdateInfo updateInfo = new UpdateInfo(dataObj);

                    if (loadFinishCallback != null) {
                        loadFinishCallback.onSuccess(updateInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (loadFinishCallback != null) {
                    loadFinishCallback.onFailure(stateCode, errorMsg);
                }
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
     * 从网络加载完版本信息后回调
     */
    public interface OnLoadVersionInfoFinish {

        public void onSuccess(UpdateInfo updateInfo);

        public void onFailure(int stateCode, String errorMsg);
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
