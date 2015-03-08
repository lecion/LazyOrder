package com.cisoft.lazyorder.core.search;

import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 14/11/17.
 */
public class SearchService extends BaseNetwork {
    public SearchService(Context context, String moduleName) {
        super(context, ApiConstants.MODULE_COMMODITY);
    }

    public SearchService(Context context) {
        this(context, ApiConstants.MODULE_COMMODITY);
    }

    /**
     * 根据店铺ID、关键字查询商品
     * @param shopId
     * @param query
     */
    public void queryGoodsList(int shopId, String query, final OnQueryGoodsFinishCallback onQueryGoodsFinishCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shopId));
        params.put(ApiConstants.KEY_COM_KEY_NAME, query);
        getRequest(ApiConstants.METHOD_COMMODITY_FIND_COMMODITY_BY_KEY, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Goods> goodsList = new ArrayList<Goods>();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    JSONArray jsonArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        goodsList.add(new Goods(jsonArr.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (onQueryGoodsFinishCallback != null) {
                    onQueryGoodsFinishCallback.onSuccess(goodsList);
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                //这里出错，根据code查询错误信息
                if (onQueryGoodsFinishCallback != null) {
                    onQueryGoodsFinishCallback.onFailure(stateCode,
                            getResponseStateInfo(stateCode));
                }
            }
        }, null);
    }

    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RES_STATE_FAILURE:
                stateInfo = mContext.getResources().getString(R.string.toast_fail_to_query_goods_list);
                break;
            case ApiConstants.RES_STATE_SUCCESS:
                stateInfo = mContext.getResources().getString(R.string.toast_success_to_load_goods_list);
                break;
            case ApiConstants.RES_STATE_NOT_NET:
                stateInfo = mContext.getResources().getString(R.string.toast_have_not_network);
                break;
            case ApiConstants.RES_STATE_SERVICE_EXCEPTION:
                stateInfo = mContext.getResources().getString(R.string.toast_service_have_exception);
                break;
        }
        return stateInfo;
    }


    public interface OnQueryGoodsFinishCallback {

        public void onSuccess(List<Goods> goodses);

        public void onFailure(int stateCode, String errorMsg);

    }
}
