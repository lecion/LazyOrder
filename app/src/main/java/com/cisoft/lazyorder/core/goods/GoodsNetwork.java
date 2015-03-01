package com.cisoft.lazyorder.core.goods;

import android.app.Activity;
import android.content.Context;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/21/14.
 */
public class GoodsNetwork extends BaseNetwork {

    public GoodsNetwork(Context context) {
        super(context, ApiConstants.MODULE_COMMODITY);
    }

    /**
     * 根据店铺ID获得所有商品
     * @param page 页码
     * @param size 每页显示的数量
     * @param sortType 排序方式，默认为人气
     */
    public void loadGoodsList(int shopId, final int page, int size, final String sortType, final OnGoodsLoadCallback onGoodsLoadCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shopId));
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        params.put(ApiConstants.KEY_COM_SORT, sortType);
        super.getRequest(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Goods> goodses = new ArrayList<Goods>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray goodsArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    JSONObject goodsObj = null;
                    Goods goods = null;
                    for (int i = 0; i < goodsArr.length(); i++) {
                        goodsObj = goodsArr.getJSONObject(i);
                        goods = new Goods(goodsObj);
                        goodses.add(goods);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(onGoodsLoadCallback != null){
                    onGoodsLoadCallback.onSuccess(goodses);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onGoodsLoadCallback != null){
                    onGoodsLoadCallback.onFailure(stateCode, errorMsg);
                }

            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(onGoodsLoadCallback != null){
                    onGoodsLoadCallback.onPreStart();
                }
            }
        });
    }

    /**
     * 根据店铺ID，商品类别获得所有商品
     * @param shopId
     * @param typeId
     * @param page
     * @param size
     * @param sortType
     * @param onGoodsLoadCallback
     */
    public void loadGoodsListByType(int shopId, int typeId, final int page, int size, final String sortType, final OnGoodsLoadCallback onGoodsLoadCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shopId));
        params.put(ApiConstants.KEY_COM_TYPE_ID, String.valueOf(typeId));
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        params.put(ApiConstants.KEY_COM_SORT, sortType);
        //final GoodsFragment f = getFragmentBySortType(sortType);
        super.getRequest(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Goods> goodses = new ArrayList<Goods>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray goodsArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    JSONObject goodsObj = null;
                    Goods goods = null;
                    for (int i = 0; i < goodsArr.length(); i++) {
                        goodsObj = goodsArr.getJSONObject(i);
                        goods = new Goods(goodsObj);
                        goodses.add(goods);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(onGoodsLoadCallback != null){
                    onGoodsLoadCallback.onSuccess(goodses);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onGoodsLoadCallback != null){
                    onGoodsLoadCallback.onFailure(stateCode, errorMsg);
                }

            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(onGoodsLoadCallback != null){
                    onGoodsLoadCallback.onPreStart();
                }
            }
        });
    }



    public interface OnGoodsLoadCallback {

        public void onPreStart();

        public void onSuccess(List<Goods> goodses);

        public void onFailure(int stateCode, String errorMsg);
    }

}
