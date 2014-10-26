package com.cisoft.lazyorder.core.goods;

import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.GoodsCategory;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/26/14.
 */
public class CategoryService extends AbsService {
    public CategoryService(Context context, String moduleName) {
        super(context, moduleName);
    }

    public void loadCateogryByShopIdFromNet(int shopId) {
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_CAT_MER_ID, String.valueOf(shopId));
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                KJLoger.debug("loadCateogryByShopIdFromNet " + goodsCategoryList);
                ((GoodsActivity)context).setCateogryData(goodsCategoryList);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(getResponseStateInfo(stateCode));
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
                stateInfo = context.getResources().getString(R.string.no_net_receiver);
                break;
        }

        return stateInfo;
    }
}
