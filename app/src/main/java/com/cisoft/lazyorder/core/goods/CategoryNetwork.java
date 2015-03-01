package com.cisoft.lazyorder.core.goods;

import android.content.Context;
import com.cisoft.lazyorder.bean.goods.GoodsCategory;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
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

    public void loadCateogryByShopId(int shopId, final OnCategoryLoadCallback onCategoryLoadCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_CAT_MER_ID, String.valueOf(shopId));

        super.getRequest(ApiConstants.METHOD_CATEGORY_FIND_ALL_BY_MER_ID, params, true, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<GoodsCategory> goodsCategorys = new ArrayList<GoodsCategory>();
                goodsCategorys.add(new GoodsCategory(0, "全部"));
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray goodsCateogyArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    JSONObject goodsCategoryObj = null;
                    GoodsCategory goodsCategory = null;
                    for (int i = 0; i < goodsCateogyArr.length(); i++) {
                        goodsCategoryObj = goodsCateogyArr.getJSONObject(i);
                        goodsCategory = new GoodsCategory(goodsCategoryObj);
                        goodsCategorys.add(goodsCategory);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (onCategoryLoadCallback != null) {
                    onCategoryLoadCallback.onSuccess(goodsCategorys);
                }

            }
        }, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (onCategoryLoadCallback != null) {
                    onCategoryLoadCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if (onCategoryLoadCallback != null) {
                    onCategoryLoadCallback.onPreStart();
                }
            }
        });
    }


    public interface OnCategoryLoadCallback {

        public void onPreStart();

        public void onSuccess(List<GoodsCategory> categories);

        public void onFailure(int stateCode, String errorMsg);
    }
}
