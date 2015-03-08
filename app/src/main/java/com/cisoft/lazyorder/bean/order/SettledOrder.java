package com.cisoft.lazyorder.bean.order;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Created by comet on 2015/2/27.
 */
public class SettledOrder extends BaseBean {

    private int userId;
    private String userPhone;
    private double orderPrice;
    private double deduction;
    private double shippingFee;
    private double settledPrice;

    public SettledOrder(){}

    public SettledOrder(JSONObject jsonObj) {
        this.parse(jsonObj);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getSettledPrice() {
        return settledPrice;
    }

    public void setSettledPrice(double settledPrice) {
        this.settledPrice = settledPrice;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator = jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ORDER_PRICE)) {
                    this.orderPrice = jsonObj.getDouble(ApiConstants.KEY_ORDER_PRICE);
                } else if (key.equals(ApiConstants.KEY_ORDER_DEDUCTION)) {
                    this.deduction = jsonObj.getDouble(ApiConstants.KEY_ORDER_DEDUCTION);
                } else if (key.equals(ApiConstants.KEY_ORDER_SHIPPING_FEE)) {
                    this.shippingFee = jsonObj.getDouble(ApiConstants.KEY_ORDER_SHIPPING_FEE);
                } else if (key.equals(ApiConstants.KEY_ORDER_SETTLED_PRICE)) {
                    this.settledPrice = jsonObj.getDouble(ApiConstants.KEY_ORDER_SETTLED_PRICE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
