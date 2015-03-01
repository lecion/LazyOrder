package com.cisoft.lazyorder.core.account;

import android.content.Context;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

public class AccountNetwork extends BaseNetwork {

	public AccountNetwork(Context context) {
		super(context, ApiConstants.MODULE_ACCOUNT);
	}


    /**
     * 用户注册
     * @param userPhone
     * @param password
     * @param registerCallback
     */
	public void userRegister(String userPhone, String password, final OnRegisterCallback registerCallback) {
		HttpParams params = new HttpParams();
		params.put(ApiConstants.KEY_ACCOUNT_USER_PHONE, userPhone);
        params.put(ApiConstants.KEY_ACCOUNT_USER_PWD, password);
	
		super.getRequest(ApiConstants.METHOD_ACCOUNT_REGISTER, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_DATA);
                    User user = new User(dataObj);

                    if (registerCallback != null) {
                        registerCallback.onSuccess(user);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    if(registerCallback != null){
                        registerCallback.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }

            }
        }, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (registerCallback != null) {
                    registerCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if (registerCallback != null) {
                    registerCallback.onPreStart();
                }
            }
        });
	}

    /**
     * 用户登录
     * @param account
     * @param password
     * @param onLoginCallback
     */
    public void userLogin(String account, String password, final OnLoginCallback onLoginCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ACCOUNT_USER_ACCOUNT, account);
        params.put(ApiConstants.KEY_ACCOUNT_USER_PWD, password);

        getRequest(ApiConstants.METHOD_ACCOUNT_LOGIN, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_DATA);
                    User user = new User(dataObj);

                    if (onLoginCallback != null) {
                        onLoginCallback.onSuccess(user);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    if(onLoginCallback != null){
                        onLoginCallback.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onLoginCallback != null){
                    onLoginCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(onLoginCallback != null){
                    onLoginCallback.onPreStart();
                }
            }
        });
    }


    /**
     * 用户修改密码
     * @param userId
     * @param newPwd
     * @param onUpdatePwdCallback
     */
    public void userUpdatePassword(int userId, String newPwd, final OnUpdatePwdCallback onUpdatePwdCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ACCOUNT_USER_ID, String.valueOf(userId));
        params.put(ApiConstants.KEY_ACCOUNT_USER_PWD, newPwd);

        getRequest(ApiConstants.METHOD_ACCOUNT_UPDATE_PWD, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(onUpdatePwdCallback != null){
                    onUpdatePwdCallback.onSuccess();
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onUpdatePwdCallback != null){
                    onUpdatePwdCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(onUpdatePwdCallback != null){
                    onUpdatePwdCallback.onPreStart();
                }
            }
        });
    }

    /**
     * 用户修改绑定的手机号码
     * @param userId
     * @param newPhone
     * @param onAlterPhoneBindingCallback
     */
    public void userAlterPhoneBinding(int userId, String newPhone, final OnAlterPhoneBindingCallback onAlterPhoneBindingCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ACCOUNT_USER_ID, String.valueOf(userId));
        params.put(ApiConstants.KEY_ACCOUNT_USER_PHONE, newPhone);

        getRequest(ApiConstants.METHOD_ACCOUNT_UPDATE_PHONE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(onAlterPhoneBindingCallback != null){
                    onAlterPhoneBindingCallback.onSuccess();
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onAlterPhoneBindingCallback != null){
                    onAlterPhoneBindingCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(onAlterPhoneBindingCallback != null){
                    onAlterPhoneBindingCallback.onPreStart();
                }
            }
        });
    }


    public interface OnRegisterCallback {
    	
    	public void onPreStart();
    	
        public void onSuccess(User user);

        public void onFailure(int stateCode, String errorMsg);
    }

    public interface OnLoginCallback {

        public void onPreStart();

        public void onSuccess(User user);

        public void onFailure(int stateCode, String errorMsg);
    }

    public interface OnUpdatePwdCallback {

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }

    public interface OnAlterPhoneBindingCallback {

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }

}
