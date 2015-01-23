package com.cisoft.shop.order.model;

import android.content.Context;

import com.cisoft.myapplication.R;
import com.cisoft.shop.ApiConstants;
import com.cisoft.shop.MainActivity;
import com.cisoft.shop.MyApplication;
import com.cisoft.shop.bean.Order;
import com.cisoft.shop.bean.OrderGoods;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.goods.model.INetWorkFinished;
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
public class OrderModel extends AbsService implements IOrderModel {

    public OrderModel(Context context) {
        super(context, ApiConstants.MODULE_COMMODITY);
    }

    @Override
    public void loadOrderList(int page, int size, final INetWorkFinished<Order> finishedListener) {
        KJStringParams params = new KJStringParams();
        //TODO 更改shop获取
        Shop shop = ((MyApplication) ((MainActivity) context).getApplication()).getShop();
        params.put(ApiConstants.KEY_ORDER_MER_ID, String.valueOf(shop.getId()));
        params.put(ApiConstants.KEY_ORDER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_ORDER_SIZE, String.valueOf(size));
        asyncUrlGet(ApiConstants.METHOD_ORDER_FIND_ORDERS_BY_MER_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                List<Order> orderList = new ArrayList<Order>();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    JSONArray jsonArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        orderList.add(new Order(jsonArr.getJSONObject(i)));
                    }
                    if (finishedListener != null) {
                        finishedListener.onSuccess(orderList);
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