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
     * @param userPhone
     * @param sendCallback
     */
    public void obtainSMSAuthCode(String userPhone, final OnSMSCodeSendCallback sendCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COMMON_USER_PHONE, userPhone);

        getRequest(ApiConstants.METHOD_COMMON_GET_SMS_AUTH_CODE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (sendCallback != null) {
                    sendCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (sendCallback != null) {
                    sendCallback.onFailure(stateCode, errorMsg);
                }
            }
        },  new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(sendCallback != null){
                    sendCallback.onPreStart();
                }
            }
        });
    }


    /**
     * 通过短信验证码验证手机
     * @param userPhone
     * @param authCode
     * @param verifyCallback
     */
    public void verifyPhoneBySMSAuthCode(String userPhone, String authCode, final OnPhoneVerifyCallback verifyCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COMMON_USER_PHONE, userPhone);
        params.put(ApiConstants.KEY_COMMON_SMS_AUTH_CODE, authCode);

        getRequest(ApiConstants.METHOD_COMMON_VERIFY_PHONE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(verifyCallback != null){
                    verifyCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(verifyCallback != null){
                    verifyCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(verifyCallback != null){
                    verifyCallback.onPreStart();
                }
            }
        });
    }



    /**
     * 短信验证码发送完成的回调
     */
    public interface OnSMSCodeSendCallback {

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }

    /**
     * 手机验证完成的回调
     */
    public interface OnPhoneVerifyCallback {

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }

}
