package com.cisoft.lazyorder.core.shop;

import android.content.Context;
import com.cisoft.lazyorder.bean.shop.Advertise;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/12/8.
 */
public class AdvertiseNetwork extends BaseNetwork {

    public AdvertiseNetwork(Context context) {
        super(context, ApiConstants.MODULE_ADVERTISE);
    }

    /**
     * 从网络获取广告数据
     */
    public void loadAdvertiseDate(final OnAdvertiseLoadFinish loadFinishCallback) {
        super.getRequest(ApiConstants.METHOD_ADVERTISE_FIND_ALL, null, true, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Advertise> advertises = new ArrayList<Advertise>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray advertiseArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    JSONObject advertiseObj = null;
                    Advertise advertise = null;
                    for (int i = 0; i < advertiseArr.length(); i++) {
                        advertiseObj = advertiseArr.getJSONObject(i);
                        advertise = new Advertise(advertiseObj);
                        advertises.add(advertise);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if(loadFinishCallback != null){
                        loadFinishCallback.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }


                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(advertises);
                }

            }
        }, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, null);
    }


    public interface OnAdvertiseLoadFinish{

        public void onSuccess(List<Advertise> advertises);

        public void onFailure(int stateCode, String errorMsg);
    }

}
