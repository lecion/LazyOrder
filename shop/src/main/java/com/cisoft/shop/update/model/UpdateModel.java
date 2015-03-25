package com.cisoft.shop.update.model;

import android.content.Context;

import com.cisoft.shop.Api;
import com.cisoft.shop.R;
import com.cisoft.shop.bean.UpdateInfo;
import com.cisoft.shop.http.AbsService;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

/**
 * Created by Lecion on 3/24/15.
 */
public class UpdateModel extends AbsService{
    private Context context;

    public UpdateModel(Context context) {
        super(context, Api.MODULE_SETTING);
        this.context = context;
    }

    public void checkUpdate(final UpdateLisnter lisenter) {
        KJStringParams params = new KJStringParams();
        params.put(Api.KEY_SETTING_TYPE, "MERCHANT");
        asyncUrlGet(Api.METHOD_CHECK_UPDATE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                int state = jsonObject.getInt(Api.KEY_STATE);
                if ( state == Api.RESPONSE_STATE_SUCCESS ) {
                    lisenter.onSuccess(new UpdateInfo(jsonObject.getJSONObject(Api.KEY_DATA)));
                } else {
                    lisenter.onFailure(getResponseStateInfo(state));
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                lisenter.onFailure(getResponseStateInfo(stateCode));
            }
        });
    }

    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case Api.RESPONSE_STATE_FAILURE:
                stateInfo = "更新失败";
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

    public interface UpdateLisnter {
        void onSuccess(UpdateInfo info);
        void onFailure(String msg);
    }

}
