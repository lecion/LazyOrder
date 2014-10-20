package com.cisoft.lazyorder.core.shop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.ui.ViewInject;

import android.content.Context;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.bean.shop.ShopCategory;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.core.AbsService.FailureCallback;
import com.cisoft.lazyorder.core.AbsService.SuccessCallback;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.shop.ShopActivity;

public class ShopCategoryService extends AbsService {

	public ShopCategoryService(Context context) {
		super(context, ApiConstants.MODULE_MER_CATEGORY);
	}

	
	
	/**
     * 加载全部店家类别列表的数据
     */
    public void loadAllShopCategoryData(){
    	KJStringParams params = new KJStringParams();
//        params.put(ApiConstants.KEY_MER_PAGE, String.valueOf(page));

        super.asyncUrlGet(ApiConstants.METHOD_MER_CATEGORY_FIND_ALL, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
            	List<ShopCategory> shopCategorys = new ArrayList<ShopCategory>();
            	shopCategorys.add(new ShopCategory(0, "全部"));
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
                ((ShopActivity)context).shopCategoryListAdapter.addData(shopCategorys);
                ((ShopActivity)context).shopCategoryListAdapter.refresh();
            }}, new FailureCallback() {

                @Override
                public void onFailure(int stateCode) {
                    ViewInject.toast(getResponseStateInfo(stateCode));
                }
        });
    }
	
	
	
	
	/**
     * 根据请求api响应的状态码来获取对应的信息
     * @param stateCode
     * @return
     */
    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_FAILURE:
                stateInfo = context.getResources().getString(R.string.fail_to_load_shop_category_list);
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = context.getResources().getString(R.string.success_to_load_shop_category_list);
                break;
            case ApiConstants.RESPONSE_STATE_NOT_NET:
                stateInfo = context.getResources().getString(R.string.no_net_receiver);
                break;
        }

        return stateInfo;
    }

}
