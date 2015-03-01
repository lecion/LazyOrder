package com.cisoft.lazyorder.core.order;

import android.content.Context;

import com.cisoft.lazyorder.bean.order.Order;
import com.cisoft.lazyorder.bean.order.SettledOrder;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/11/8.
 */
public class OrderNetwork extends BaseNetwork {

    public OrderNetwork(Context context) {
        super(context, ApiConstants.MODULE_ORDER);
    }


    /**
     * 提交商品订单给服务器
     */
    public void submitOrderForServer(String orderJsonStr, final OnOrderSubmitFinish submitFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_JSON_STR, orderJsonStr);

        super.postRequest(ApiConstants.METHOD_ORDER_SUBMIT, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_DATA);
                    Order order = new Order(dataObj);

                    if(submitFinishCallback != null){
                        submitFinishCallback.onSuccess(order);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(submitFinishCallback != null){
                    submitFinishCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(submitFinishCallback != null){
                    submitFinishCallback.onPreStart();
                }
            }
        });
    }


    /**
     * 从网络加载订单列表的数据
     * @param page
     * @param pager
     */
    public void loadOrderListData(String userPhone, final int page, int pager,
                                  final OnOrderListLoadFinish loadFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_PHONE, userPhone);
        params.put(ApiConstants.KEY_ORDER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_ORDER_PAGER, String.valueOf(pager));

        super.getRequest(ApiConstants.METHOD_ORDER_FIND_ALL, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Order> orders = new ArrayList<Order>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray orderArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    JSONObject orderObj = null;
                    Order order = null;
                    for (int i = 0; i < orderArr.length(); i++) {
                        orderObj = orderArr.getJSONObject(i);
                        order = new Order(orderObj);
                        orders.add(order);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(orders);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(stateCode, errorMsg);
                }

            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if(loadFinishCallback != null){
                    loadFinishCallback.onPreStart();
                }
            }
        });
    }


    /**
     * 通过服务器结算订单
     */
    public void settleOrderByServer(String jsonStr, final OnOrderSettleFinish onOrderSettleFinish) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_JSON_STR, jsonStr);

        super.postRequest(ApiConstants.METHOD_ORDER_SETTLE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_DATA);
                    SettledOrder settledOrder = new SettledOrder(dataObj);

                    if (onOrderSettleFinish != null) {
                        onOrderSettleFinish.onSuccess(settledOrder);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if(onOrderSettleFinish != null){
                        onOrderSettleFinish.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (onOrderSettleFinish != null) {
                    onOrderSettleFinish.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
            @Override
            public void onPreStart() {
                if (onOrderSettleFinish != null) {
                    onOrderSettleFinish.onPreStart();
                }
            }
        });
    }

    /**
     * 历史订单加载完成后的回调
     */
    public interface OnOrderListLoadFinish {

        public void onPreStart();

        public void onSuccess(List<Order> orders);

        public void onFailure(int stateCode, String errorMsg);
    }


    /**
     * 订单提交完成后的回调
     */
    public interface OnOrderSubmitFinish {

        public void onPreStart();

        public void onSuccess(Order order);

        public void onFailure(int stateCode, String errorMsg);
    }


    /**
     * 订单结算完成后的回调
     */
    public interface OnOrderSettleFinish {

        public void onPreStart();

        public void onSuccess(SettledOrder settledOrder);

        public void onFailure(int stateCode, String errorMsg);
    }
}
