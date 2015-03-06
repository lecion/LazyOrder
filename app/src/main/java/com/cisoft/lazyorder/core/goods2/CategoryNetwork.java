package com.cisoft.lazyorder.core.goods2;

import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.GoodsCategory;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/26/14.
 */
public class CategoryNetwork extends BaseNetwork {

    public CategoryNetwork(Context context) {
        super(context, ApiConstants.MODULE_COM_CATEGORY);
    }

    public void loadCateogryByShopId(int shopId, final INetWorkFinished<GoodsCategory> iNetWorkFinished) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_CAT_MER_ID, String.valueOf(shopId));

        super.getRequest(ApiConstants.METHOD_CATEGORY_FIND_ALL_BY_MER_ID, params, true, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                List<GoodsCategory> goodsCategoryList = new ArrayList<GoodsCategory>();
                goodsCategoryList.add(new GoodsCategory(0, "全部"));
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray goodsCategoryArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    for (int i = 0; i < goodsCategoryArr.length(); i++) {
                        goodsCategoryList.add(new GoodsCategory(goodsCategoryArr.getJSONObject(i)));
                    }
                    if (iNetWorkFinished != null) {
                        iNetWorkFinished.onSuccess(goodsCategoryList);
                    }
                } catch (JSONException e) {
                    if (iNetWorkFinished != null) {
                        iNetWorkFinished.onFailure(getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }
                ((GoodsActivity)mContext).setCateogryData(goodsCategoryList);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (iNetWorkFinished != null) {
                    iNetWorkFinished.onFailure(errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {

            }
        });
    }


    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RES_STATE_FAILURE:
                stateInfo = mContext.getResources().getString(R.string.toast_fail_to_load_goods_category_list);
                break;
            default:
                stateInfo = super.getResponseStateInfo(stateCode);
                break;
        }
        return stateInfo;
    }
}
