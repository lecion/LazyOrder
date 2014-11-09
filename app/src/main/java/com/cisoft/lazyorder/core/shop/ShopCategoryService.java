package com.cisoft.lazyorder.core.shop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.cisoft.lazyorder.AppConfig;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;

public class ShopCategoryService extends AbsService {

	public ShopCategoryService(Context context) {
		super(context, ApiConstants.MODULE_MER_CATEGORY);
	}

	
	
	/**
     * 加载全部店家类别列表的数据
     */
    public void loadAllShopCategoryData(final OnCategoryLoadFinish loadFinishCallback){
    	String url = packageApiUrlByMethodNameAndParams(ApiConstants.METHOD_MER_CATEGORY_FIND_ALL, null);
        String result = null;
        result = httpCacher.get(url);
        if (result != null && !AppConfig.IS_DEBUG) {
            List<ShopCategory> shopCategorys = new ArrayList<ShopCategory>();
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray shopCategoryArr = jsonObj.getJSONArray(ApiConstants.KEY_MC_DATA);
                JSONObject shopCategoryObj = null;
                ShopCategory shopCategory = null;
                for (int i = 0; i < shopCategoryArr.length(); i++) {
                    shopCategoryObj = shopCategoryArr.getJSONObject(i);
                    shopCategory = new ShopCategory(shopCategoryObj);
                    shopCategorys.add(shopCategory);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(loadFinishCallback != null){
                loadFinishCallback.onSuccess(shopCategorys);
            }

        } else {
            super.asyncUrlGet(ApiConstants.METHOD_MER_CATEGORY_FIND_ALL, null, true, new SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    List<ShopCategory> shopCategorys = new ArrayList<ShopCategory>();
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray shopCateogyArr = jsonObj.getJSONArray(ApiConstants.KEY_MC_DATA);
                        JSONObject shopCategoryObj = null;
                        ShopCategory shopCategory = null;
                        for (int i = 0; i < shopCateogyArr.length(); i++) {
                            shopCategoryObj = shopCateogyArr.getJSONObject(i);
                            shopCategory = new ShopCategory(shopCategoryObj);
                            shopCategorys.add(shopCategory);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(loadFinishCallback != null){
                        loadFinishCallback.onSuccess(shopCategorys);
                    }

                }
            }, new FailureCallback() {

                @Override
                public void onFailure(int stateCode) {
                    if(loadFinishCallback != null){
                        loadFinishCallback.onFailure(stateCode);
                    }
                }
            });
        }
    }
	
	



    public interface OnCategoryLoadFinish{

        public void onSuccess(List<ShopCategory> categories);

        public void onFailure(int stateCode);
    }

}
