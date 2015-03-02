package com.cisoft.shop.welcome.model;

import android.content.Context;

import com.cisoft.shop.R;
import com.cisoft.shop.ApiConstants;
import com.cisoft.shop.http.AbsService;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

/**
 * Created by Lecion on 2015/2/20.
 */
public class ExpmerModel extends AbsService {

    public ExpmerModel(Context context) {
        super(context, ApiConstants.MODULE_EXPMER);
    }

    /**
     * 快递商家登陆
     * @param phone
     * @param pwd
     */
    public void expmerLogin(String phone, String pwd, final ILoginListener loginListener) {
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_EXPMER_EXPMER_PHONE, phone);
        params.put(ApiConstants.KEY_EXPMER_EXPMER_PWD, pwd);
        asyncUrlGet(ApiConstants.METHOD_EXPMER_EXPMER_LOGIN, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt(ApiConstants.KEY_STATE);
                if (state == 200) {
                    //登陆成功
                    loginListener.onSuccess(jsonObj.getJSONObject("data"));
                } else {
                    loginListener.onFailure(getResponseStateInfo(state));
                }

            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                loginListener.onFailure(getResponseStateInfo(stateCode));
            }
        });
    }

    public interface IUpdateOperateState{
        public void onSuccess(int code);

        public void onFailure(String msg);
    }

    public interface ILoginListener {
        public void onSuccess(JSONObject data);

        public void onFailure(String msg);
    }

    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_ERROR_LOGIN:
                stateInfo = "账号或密码错误";
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = "加载商品成功";
                break;
            case ApiConstants.RESPONSE_STATE_NOT_NET:
                stateInfo = context.getResources().getString(R.string.no_net_service);
                break;
            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = context.getResources().getString(R.string.service_have_error_exception);
                break;
        }
        return stateInfo;
    }
}
