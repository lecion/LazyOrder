package com.cisoft.lazyorder.core.shop;

import android.content.Context;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;
import java.util.ArrayList;
import java.util.List;

public class ShopCategoryNetwork extends BaseNetwork {

	public ShopCategoryNetwork(Context context) {
		super(context, ApiConstants.MODULE_MER_CATEGORY);
	}

	
	/**
     * 加载全部店家类别列表的数据
     */
    public void loadShopCategoryListData(int schoolId, final OnCategoryLoadCallback onCategoryLoadCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_MC_SCHOOL_ID, String.valueOf(schoolId));

        super.getRequest(ApiConstants.METHOD_MER_CATEGORY_FIND_ALL, params, true, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<ShopCategory> shopCategorys = new ArrayList<ShopCategory>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopCateogyArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    JSONObject shopCategoryObj = null;
                    ShopCategory shopCategory = null;
                    for (int i = 0; i < shopCateogyArr.length(); i++) {
                        shopCategoryObj = shopCateogyArr.getJSONObject(i);
                        shopCategory = new ShopCategory(shopCategoryObj);
                        shopCategorys.add(shopCategory);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if(onCategoryLoadCallback != null){
                        onCategoryLoadCallback.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }

                if (onCategoryLoadCallback != null) {
                    onCategoryLoadCallback.onSuccess(shopCategorys);
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

        public void onSuccess(List<ShopCategory> categories);

        public void onFailure(int stateCode, String errorMsg);
    }

}
