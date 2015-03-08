package com.cisoft.lazyorder.core.express;

import android.content.Context;

import com.cisoft.lazyorder.bean.express.Express;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/11/8.
 */
public class ExpressNetwork extends BaseNetwork {

    public ExpressNetwork(Context context) {
        super(context, ApiConstants.MODULE_EXPRESS);
    }


    /**
     * 从网络加载Express列表的数据
     * @param page
     * @param pager
     */
    public void loadExpressListData(String userPhone, int page, int pager, final OnExpressListLoadFinish loadFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_EXPRESS_PHONE, userPhone);
        params.put(ApiConstants.KEY_EXPRESS_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_EXPRESS_PAGER, String.valueOf(pager));

        getRequest(ApiConstants.METHOD_EXPRESS_FIND_ALL_BY_PHONE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Express> expresses = new ArrayList<Express>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray expressArr = jsonObj.getJSONArray(
                            ApiConstants.KEY_DATA);
                    JSONObject expressObj = null;
                    Express express = null;
                    for (int i = 0; i < expressArr.length(); i++) {
                        expressObj = expressArr.getJSONObject(i);
                        express = new Express(expressObj);
                        expresses.add(express);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if(loadFinishCallback != null){
                        loadFinishCallback.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }

                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(expresses);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(loadFinishCallback != null){
                    loadFinishCallback.onPreStart();
                }
            }
        });
    }


    /**
     * 提交Express数据给服务器
     */
    public void submitExpressOrderForServer(String expressJsonStr, final OnExpressSubmitFinish submitFinishCallback) {
//        HttpParams params = new HttpParams();
//        params.put(ApiConstants.KEY_ORDER_SAVE_ORDER_JSON_DATA, createExpressOrderJsonStrByObj(express));
//
//        getRequest(ApiConstants.METHOD_ORDER_SAVE_ORDER, params, new SuccessCallback() {
//            @Override
//            public void onSuccess(String result) {
//                if(submitFinishCallback != null){
//                    submitFinishCallback.onSuccess();
//                }
//            }
//        }, new FailureCallback() {
//            @Override
//            public void onFailure(int stateCode, String errorMsg) {
//                if(submitFinishCallback != null){
//                    submitFinishCallback.onFailure(stateCode, errorMsg);
//                }
//            }
//        }, new PrepareCallback() {
//            @Override
//            public void onPreStart() {
//                if(submitFinishCallback != null){
//                    submitFinishCallback.onPreStart();
//                }
//            }
//        });
    }



    /**
     * Express列表数据加载完成后的回调
     */
    public interface OnExpressListLoadFinish {

        public void onPreStart();

        public void onSuccess(List<Express> expresses);

        public void onFailure(int stateCode, String errorMsg);
    }

    /**
     * Express数据提交完成后的回调
     */
    public interface OnExpressSubmitFinish {

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }
}
