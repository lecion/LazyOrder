package com.cisoft.shop.goods.model;

import android.content.Context;

import com.cisoft.myapplication.R;
import com.cisoft.shop.ApiConstants;
import com.cisoft.shop.MainActivity;
import com.cisoft.shop.MyApplication;
import com.cisoft.shop.bean.GoodsCategory;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.http.AbsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJStringParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/26/14.
 */
public class CategoryModel extends AbsService {
    protected CategoryModel(Context context, String moduleName) {
        super(context, ApiConstants.MODULE_COM_CATEGORY);
    }

    public CategoryModel(Context context) {
        super(context, ApiConstants.MODULE_COM_CATEGORY);
    }

    public void loadCateogryByShopId(final INetWorkFinished<GoodsCategory> iNetWorkFinished) {
        Shop shop = ((MyApplication) ((MainActivity) context).getApplication()).getShop();
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_CAT_MER_ID, String.valueOf(shop.getId()));
        super.asyncUrlGet(ApiConstants.METHOD_CATEGORY_FIND_ALL_BY_MER_ID, params, new SuccessCallback() {
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
                        iNetWorkFinished.onFailure(getResponseStateInfo(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION));
                    }
                }
                KJLoger.debug("loadCateogryByShopId " + goodsCategoryList);
                //((GoodsActivity)context).setCateogryData(goodsCategoryList);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if (iNetWorkFinished != null) {
                    iNetWorkFinished.onFailure(getResponseStateInfo(stateCode));
                }
            }
        });
    }


    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_FAILURE:
                stateInfo = context.getResources().getString(R.string.fail_to_load_goods_category_list);
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = context.getResources().getString(R.string.success_to_load_goods_category_list);
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
