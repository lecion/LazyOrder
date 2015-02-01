package com.cisoft.lazyorder.core.order;

import android.content.Context;

import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.order.DishOrder;
import com.cisoft.lazyorder.bean.order.ExpressOrder;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

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
     * 提交商品类订单给服务器
     */
    public void submitDishOrderForServer(DishOrder dishOrder, final OnDishOrderSubmitFinish submitFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_SAVE_ORDER_JSON_DATA, createDishOrderJsonStrByObj(dishOrder));

        getRequest(ApiConstants.METHOD_ORDER_SAVE_ORDER, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject dataObj = jsonObj.getJSONObject(ApiConstants.KEY_ORDER_SAVE_ORDER_DATA);

                    if(submitFinishCallback != null){
                        submitFinishCallback.onSuccess(dataObj.getString(ApiConstants.KEY_ORDER_SAVE_ORDER_AFTER_ORDER_NUM),
                                dataObj.getString(ApiConstants.KEY_ORDER_SAVE_ORDER_AFTER_MSG),
                                dataObj.getDouble(ApiConstants.KEY_ORDER_SAVE_ORDER_AFTER_MONEY_ALL));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if(submitFinishCallback != null){
                    submitFinishCallback.onFailure(stateCode);
                }
            }
        }, null);
    }

    /**
     * 通过商品类订单对象得到json形式的订单数据
     * @param dishOrder
     * @return
     */
    private String createDishOrderJsonStrByObj(DishOrder dishOrder){
        String jsonStr = "";

        try {
            JSONObject resultJsonObj = new JSONObject();
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_TYPE, ApiConstants.KEY_ORDER_SAVE_ORDER_TYPE_DISH);//商品类订单
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_USER_NAME, dishOrder.getUserName());//用户的姓名
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_USER_PHONE, dishOrder.getUserPhone());//用户的联系电话
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_BUILDING_ID, dishOrder.getBuildingId());//用户的楼栋id
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_ROOM_NUM, dishOrder.getDormitory());//用户的寝室号
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_EXTRA_MSG, dishOrder.getExtraMsg());//额外留言
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_SHOP_ID, dishOrder.getShopId());//店家id

            List<Goods> goodsList = dishOrder.getGoodsList();
            JSONArray comListJsonArray = new JSONArray();
            JSONObject jsonObj = null;
            for (Goods good : goodsList) {
                jsonObj = new JSONObject();
                jsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_COM_LIST_ITEM_COM_ID, good.getId());
                jsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_COM_LIST_ITEM_ORDERED_COUNT, good.getOrderNum());
                comListJsonArray.put(jsonObj);
            }
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_COM_LIST, comListJsonArray);//所定的商品列表

            jsonStr = resultJsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }




    /**
     * 从网络加载Dish类历史订单列表的数据
     * @param page
     * @param pager
     */
    public void loadDishHisOrderData(String userPhone, final int page, int pager, final OnDishHisOrderLoadFinish loadFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_FIND_ALL_USER_PHONE, userPhone);
        params.put(ApiConstants.KEY_ORDER_FIND_ALL_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_ORDER_FIND_ALL_PAGER, String.valueOf(pager));

        getRequest(ApiConstants.METHOD_ORDER_FIND_ALL, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<DishOrder> HistoryOrders = new ArrayList<DishOrder>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray hisOrderArr = jsonObj.getJSONArray(ApiConstants.KEY_ORDER_FIND_ALL_DATA);
                    JSONObject hisOrderObj = null;
                    DishOrder hisOrder = null;
                    for (int i = 0; i < hisOrderArr.length(); i++) {
                        hisOrderObj = hisOrderArr.getJSONObject(i);
                        hisOrder = new DishOrder(hisOrderObj);
                        HistoryOrders.add(hisOrder);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(HistoryOrders);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(stateCode);
                }

            }
        }, null);
    }


    /**
     * 从网络加载Express类型历史订单列表的数据
     * @param page
     * @param pager
     */
    public void loadExpressHisOrderData(String userPhone, final int page, int pager, final OnExpressHisOrderLoadFinish loadFinishCallback){
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_USER_PHONE, userPhone);
        params.put(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_PAGER, String.valueOf(pager));

        getRequest(ApiConstants.METHOD_ORDER_EXPRESS_ORDER_LIST, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<ExpressOrder> expressOrders = new ArrayList<ExpressOrder>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray hisOrderArr = jsonObj.getJSONArray(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_DATA);
                    JSONObject hisOrderObj = null;
                    ExpressOrder hisOrder = null;
                    for (int i = 0; i < hisOrderArr.length(); i++) {
                        hisOrderObj = hisOrderArr.getJSONObject(i);
                        hisOrder = new ExpressOrder(hisOrderObj);
                        expressOrders.add(hisOrder);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(expressOrders);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(stateCode);
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
     * 通过Express类订单对象得到json形式的订单数据
     * @param expressOrder
     * @return
     */
    private String createExpressOrderJsonStrByObj(ExpressOrder expressOrder){
        String jsonStr = "";

        try {
            JSONObject resultJsonObj = new JSONObject();
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_TYPE, ApiConstants.KEY_ORDER_SAVE_ORDER_TYPE_EXPRESS);//Express类订单
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_USER_NAME, expressOrder.getUserName());//用户的姓名
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_USER_PHONE, expressOrder.getUserPhone());//用户的联系电话
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_BUILDING_ID, expressOrder.getBuildingId());//用户的楼栋id
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_ROOM_NUM, expressOrder.getDormitory());//用户的寝室号
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_EXTRA_MSG, expressOrder.getExtraMsg());//额外留言
            resultJsonObj.put(ApiConstants.KEY_ORDER_SAVE_ORDER_SMS_CONTENT, expressOrder.getSmsCotent());//收到的快递短信

            jsonStr = resultJsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }



    /**
     * 提交Express类订单给服务器
     */
    public void submitExpressOrderForServer(ExpressOrder expressOrder, final OnExpressOrderSubmitFinish submitFinishCallback) {
        HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ORDER_SAVE_ORDER_JSON_DATA, createExpressOrderJsonStrByObj(expressOrder));

        getRequest(ApiConstants.METHOD_ORDER_SAVE_ORDER, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(submitFinishCallback != null){
                    submitFinishCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                if(submitFinishCallback != null){
                    submitFinishCallback.onFailure(stateCode);
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
     * Dish类历史订单加载完成后的回调
     */
    public interface OnDishHisOrderLoadFinish {
        public void onSuccess(List<DishOrder> historyOrders);

        public void onFailure(int stateCode);
    }

    /**
     * Express类历史订单加载完成后的回调
     */
    public interface OnExpressHisOrderLoadFinish{

        public void onPreStart();

        public void onSuccess(List<ExpressOrder> expressOrders);

        public void onFailure(int stateCode);
    }

    /**
     * Dish类订单提交完成后的回调
     */
    public interface OnDishOrderSubmitFinish {
        public void onSuccess(String orderNum, String message, double moneyAll);

        public void onFailure(int stateCode);
    }

    /**
     * Express类订单提交完成后的回调
     */
    public interface OnExpressOrderSubmitFinish{

        public void onPreStart();

        public void onSuccess();

        public void onFailure(int stateCode);
    }
}
