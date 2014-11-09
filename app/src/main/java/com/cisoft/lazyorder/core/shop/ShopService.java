package com.cisoft.lazyorder.core.shop;

import android.content.Context;

import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/10/20.
 */
public class ShopService extends AbsService{

    public ShopService(Context context) {
        super(context, ApiConstants.MODULE_MERCHANTS);
    }



    /**
     * 从网络加载指定类别的店家列表的数据
     * @param page
     * @param pager
     */
    public void loadShopDataByTypeId(int typeId, final int page, int pager, final OnShopLoadFinish loadFinishCallback){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_MER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_MER_PAGER, String.valueOf(pager));
        params.put(ApiConstants.KEY_MER_TYPE_ID, String.valueOf(typeId));

        super.asyncUrlGet(ApiConstants.METHOD_MERCHANTS_FIND_BY_TYPE_ID, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Shop> shops = new ArrayList<Shop>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopArr = jsonObj.getJSONArray(ApiConstants.KEY_MER_DATA);
                    JSONObject shopObj = null;
                    Shop shop = null;
                    for (int i = 0; i < shopArr.length(); i++) {
                        shopObj = shopArr.getJSONObject(i);
                        shop = new Shop(shopObj);
                        shops.add(shop);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(shops);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(stateCode);
                }

            }
        });
    }



    public interface OnShopLoadFinish{
        public void onSuccess(List<Shop> shops);

        public void onFailure(int stateCode);
    }
}
