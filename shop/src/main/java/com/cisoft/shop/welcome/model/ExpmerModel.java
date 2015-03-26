package com.cisoft.shop.welcome.model;

import android.content.Context;

import com.cisoft.shop.Api;
import com.cisoft.shop.R;
import com.cisoft.shop.bean.Expmer;
import com.cisoft.shop.http.AbsService;
import com.cisoft.shop.util.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

/**
 * Created by Lecion on 2015/2/20.
 */
public class ExpmerModel extends AbsService {

    public ExpmerModel(Context context) {
        super(context, Api.MODULE_EXPMER);
    }

    /**
     * 快递商家登陆
     * @param phone
     * @param pwd
     */
    public void expmerLogin(String phone, String pwd, final ILoginListener loginListener) {
        KJStringParams params = new KJStringParams();
        params.put(Api.KEY_EXPMER_EXPMER_PHONE, phone);
        params.put(Api.KEY_EXPMER_EXPMER_PWD, pwd);
        asyncUrlGet(Api.METHOD_EXPMER_EXPMER_LOGIN, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt(Api.KEY_STATE);
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

    /**
     * 更改营业状态
     * @param state
     */
    public void updateOperateState(int state, final IUpdateOperateState finishedListener) {
        Expmer expmer = L.getExpmer(context);
        KJStringParams params = new KJStringParams();
        params.put(Api.KEY_EXPMER_OPERATING_STATE, String.valueOf(state));
        params.put(Api.KEY_EXPRESS_EXPMER_ID, String.valueOf(expmer.getId()));
        asyncUrlGet(Api.METHOD_EXPMER_UPDATE_OPERATING_STATE, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt(Api.KEY_STATE);
                String data = jsonObj.getString(Api.KEY_MESSAGE);
                if (state == 200) {
                    finishedListener.onSuccess(state);
                } else {
                    finishedListener.onFailure(data);
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                finishedListener.onFailure(getResponseStateInfo(stateCode));
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
            case Api.RESPONSE_STATE_ERROR_LOGIN:
                stateInfo = "账号或密码错误";
                break;
            case Api.RESPONSE_STATE_SUCCESS:
                stateInfo = "加载商品成功";
                break;
            case Api.RESPONSE_STATE_NOT_NET:
                stateInfo = context.getResources().getString(R.string.no_net_service);
                break;
            case Api.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = context.getResources().getString(R.string.service_have_error_exception);
                break;
        }
        return stateInfo;
    }
}
