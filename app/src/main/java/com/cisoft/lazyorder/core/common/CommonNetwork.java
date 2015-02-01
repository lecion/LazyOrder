package com.cisoft.lazyorder.core.common;

import android.content.Context;

import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by comet on 2014/11/22.
 */
public class CommonNetwork extends BaseNetwork {

    public CommonNetwork(Context context) {
        super(context, ApiConstants.MODULE_COMMON);
    }

    /**
     * 获取短信验证码
     * @param userMobileNum
     * @param sendFinishCallback
     */
    public void obtainSMSAuthCode(String userMobileNum, final OnSMSCodeSendFinish sendFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COMMON_USER_PHONE, userMobileNum);

        getRequest(ApiConstants.METHOD_COMMON_GET_SMS_AUTH_CODE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (sendFinishCallback != null) {
                    sendFinishCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if (sendFinishCallback != null) {
                    sendFinishCallback.onFailure(stateCode);
                }
            }
        }, null);
    }


    /**
     * 通过短信验证码验证手机
     * @param userMobileNum
     * @param authCode
     * @param verifyFinishCallback
     */
    public void verifyPhoneBySMSAuthCode(String userMobileNum, String authCode, final OnPhoneVerifyFinish verifyFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COMMON_USER_PHONE, userMobileNum);
        params.put(ApiConstants.KEY_COMMON_SMS_AUTH_CODE, authCode);

        getRequest(ApiConstants.METHOD_COMMON_VERIFY_PHONE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(verifyFinishCallback != null){
                    verifyFinishCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if(verifyFinishCallback != null){
                    verifyFinishCallback.onFailure(stateCode);
                }
            }
        }, null);
    }



    /**
     * 短信验证码发送完成的回调
     */
    public interface OnSMSCodeSendFinish{
        public void onSuccess();

        public void onFailure(int stateCode);
    }

    /**
     * 手机验证完成的回调
     */
    public interface OnPhoneVerifyFinish{
        public void onSuccess();

        public void onFailure(int stateCode);
    }

}
