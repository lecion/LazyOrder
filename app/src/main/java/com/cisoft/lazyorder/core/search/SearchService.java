package com.cisoft.lazyorder.core.search;

import android.content.Context;

import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.core.goods.INetWorkFinished;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 14/11/17.
 */
public class SearchService extends AbsService {
    public SearchService(Context context, String moduleName) {
        super(context, ApiConstants.MODULE_SEARCH);
    }

    /**
     * 根据店铺ID、关键字查询商品
     * @param shopId
     * @param query
     */
    public void queryGoodsList(int shopId, String query, final INetWorkFinished<Goods> onNetwordFinished) {
        KJStringParams params = new KJStringParams();
        //TODO 搜索参数设置，待接口
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shopId));
        super.asyncUrlGet(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, false, new SuccessCallback() {
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
                    if (onNetwordFinished != null) {
                        onNetwordFinished.onSuccess(goodsList);
                    }
                } catch (JSONException e) {
                    //这里是json格式不对，无法完成解析
                    if (onNetwordFinished != null) {
                        onNetwordFinished.onFailure(getResponseStateInfo(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION));
                    }
                }

            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                //这里出错，根据code查询错误信息
                if (onNetwordFinished != null) {
                    onNetwordFinished.onFailure(getResponseStateInfo(stateCode));
                }
            }
        });
    }

}
