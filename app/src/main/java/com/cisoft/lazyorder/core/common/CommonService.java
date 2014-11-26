package com.cisoft.lazyorder.core.common;

import android.content.Context;

import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.kymjs.aframe.http.KJStringParams;

/**
 * Created by comet on 2014/11/22.
 */
public class CommonService extends AbsService {

    public CommonService(Context context) {
        super(context, ApiConstants.MODULE_COMMON);
    }

    /**
     * 获取短信验证码
     * @param userMobileNum
     * @param sendFinishCallback
     */
    public void obtainSMSAuthCode(String userMobileNum, final OnSMSCodeSendFinish sendFinishCallback){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COMMON_USER_PHONE, userMobileNum);

        super.asyncUrlGet(ApiConstants.METHOD_COMMON_GET_SMS_AUTH_CODE, params, false, new SuccessCallback() {
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
        });
    }


    /**
     * 通过短信验证码验证手机
     * @param userMobileNum
     * @param authCode
     * @param verifyFinishCallback
     */
    public void verifyPhoneBySMSAuthCode(String userMobileNum, String authCode, final OnPhoneVerifyFinish verifyFinishCallback){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COMMON_USER_PHONE, userMobileNum);
        params.put(ApiConstants.KEY_COMMON_SMS_AUTH_CODE, authCode);

        super.asyncUrlGet(ApiConstants.METHOD_COMMON_VERIFY_PHONE, params, false, new SuccessCallback() {
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
        });
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
