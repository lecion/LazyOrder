package com.cisoft.shop.goods.model;

import android.content.Context;

import com.cisoft.myapplication.R;
import com.cisoft.shop.ApiConstants;
import com.cisoft.shop.MainActivity;
import com.cisoft.shop.MyApplication;
import com.cisoft.shop.bean.Goods;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.http.AbsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 12/4/14.
 */
public class GoodsModel extends AbsService implements IGoodsModel {

    protected GoodsModel(Context context, String moduleName) {
        super(context, ApiConstants.MODULE_COMMODITY);
    }

    public GoodsModel(Context context) {
        super(context, ApiConstants.MODULE_COMMODITY);
    }

    @Override
    public void loadGoodsList(int page, int size, int type, String sortType, final INetWorkFinished<Goods> finishedListener) {
        KJStringParams params = new KJStringParams();
        Shop shop = ((MyApplication) ((MainActivity) context).getApplication()).getShop();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shop.getId()));
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        params.put(ApiConstants.KEY_COM_SORT, sortType);
        asyncUrlGet(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                List<Goods> goodsList = new ArrayList<Goods>();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    JSONArray jsonArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        goodsList.add(new Goods(jsonArr.getJSONObject(i)));
                    }
                    if (finishedListener != null) {
                        finishedListener.onSuccess(goodsList);
                    }
                } catch (JSONException e) {
                    //这里是json格式不对，无法完成解析
                    if (finishedListener != null) {
                        finishedListener.onFailure(getResponseStateInfo(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION));
                    }
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if (finishedListener != null) {
                    finishedListener.onFailure(getResponseStateInfo(stateCode));
                }
            }
        });
    }

    @Override
    public void loadGoodsListByType(int page, int size, int type, String sortType, final INetWorkFinished<Goods> finishedListener) {
        KJStringParams params = new KJStringParams();
        Shop shop = ((MyApplication) ((MainActivity) context).getApplication()).getShop();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shop.getId()));
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        params.put(ApiConstants.KEY_COM_SORT, sortType);
        String methodName = "";
        if (type == 0) {
            methodName = ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID;
        } else {
            params.put(ApiConstants.KEY_COM_TYPE_ID, String.valueOf(type));
            methodName = ApiConstants.METHOD_COMMODITY_FIND_BY_MER_AND_TYPE_ID;
        }

        asyncUrlGet(methodName, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                List<Goods> goodsList = new ArrayList<Goods>();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    JSONArray jsonArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        goodsList.add(new Goods(jsonArr.getJSONObject(i)));
                    }
                    if (finishedListener != null) {
                        finishedListener.onSuccess(goodsList);
                    }
                } catch (JSONException e) {
                    //这里是json格式不对，无法完成解析
                    if (finishedListener != null) {
                        finishedListener.onFailure(getResponseStateInfo(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION));
                    }
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if (finishedListener != null) {
                    finishedListener.onFailure(getResponseStateInfo(stateCode));
                }
            }
        });
    }

    @Override
    public void updateComState(int state, final IUpdateGoodsState finishedListener) {
        Shop shop = ((MyApplication) ((MainActivity) context).getApplication()).getShop();
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COM_COM_ID, String.valueOf(shop.getId()));
        params.put(ApiConstants.KEY_COM_COM_STATE, String.valueOf(state));
        asyncUrlGet(ApiConstants.METHOD_COMMODITY_UPDATE_COM_STATE, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt(ApiConstants.KEY_STATE);
                String data = jsonObj.getString(ApiConstants.KEY_DATA);
                if (state == 200) {
                    finishedListener.onSuccess(state);
                } else {
                    finishedListener.onFailure(data);
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                finishedListener.onFailure(getResponseStateInfo(stateCode));
            }
        });
    }


    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_FAILURE:
                stateInfo = context.getResources().getString(R.string.fail_to_load_goods_list);
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = context.getResources().getString(R.string.success_to_load_goods_list);
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

    public interface IUpdateGoodsState {
        public void onSuccess(int code);

        public void onFailure(String msg);
    }
}