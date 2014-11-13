package com.cisoft.lazyorder.core.orderlist;

import android.content.Context;

import com.cisoft.lazyorder.bean.orderlist.HistoryOrder;
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
public class HistoryOrderService extends AbsService{

    public HistoryOrderService(Context context) {
        super(context, ApiConstants.MODULE_HISTORY_ORDER);
    }



    /**
     * 从网络加载历史订单列表的数据
     * @param page
     * @param pager
     */
    public void loadHistoryOrderData(String userPhone, final int page, int pager, final OnHistoryOrderLoadFinish loadFinishCallback){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_HIS_ORDER_USER_PHONE, userPhone);
        params.put(ApiConstants.KEY_HIS_ORDER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_HIS_ORDER_PAGER, String.valueOf(pager));

        super.asyncUrlGet(ApiConstants.METHOD_HIS_ORDER_FIND_ALL, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<HistoryOrder> HistoryOrders = new ArrayList<HistoryOrder>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray hisOrderArr = jsonObj.getJSONArray(ApiConstants.KEY_HIS_ORDER_DATA);
                    JSONObject hisOrderObj = null;
                    HistoryOrder hisOrder = null;
                    for (int i = 0; i < hisOrderArr.length(); i++) {
                        hisOrderObj = hisOrderArr.getJSONObject(i);
                        hisOrder = new HistoryOrder(hisOrderObj);
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
        });
    }



    public interface OnHistoryOrderLoadFinish{
        public void onSuccess(List<HistoryOrder> historyOrders);

        public void onFailure(int stateCode);
    }
}
