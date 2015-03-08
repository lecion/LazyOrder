package com.cisoft.lazyorder.bean.order;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by comet on 2014/11/15.
 */
public class Order extends BaseBean implements Serializable {

    private int orderId;
    private String orderNum;
    private String orderTime;
    private int shopId;
    private String shopName;
    private double orderPrice;
    private double deduction;
    private double shippingFee;
    private double settledPrice;
    private String extraMsg;
    private String name;
    private String phone;
    private String address;
    private List<GoodsXCount> goodsList;

    public Order() {}

    public Order(String orderNum, String orderTime, int shopId, String shopName,
                 double orderPrice, double deduction, double shippingFee, double settledPrice,
                 String extraMsg, String name, String phone, String address, List<GoodsXCount> goodsList) {
        this.orderNum = orderNum;
        this.orderTime = orderTime;
        this.shopId = shopId;
        this.shopName = shopName;
        this.orderPrice = orderPrice;
        this.deduction = deduction;
        this.shippingFee = shippingFee;
        this.settledPrice = settledPrice;
        this.extraMsg = extraMsg;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.goodsList = goodsList;
    }

    public Order(JSONObject jsonObj) {
        this.parse(jsonObj);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<GoodsXCount> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsXCount> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        goodsList = new ArrayList<GoodsXCount>();
        try {
            Iterator<String> iterator = jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ORDER_ID)) {
                    this.orderId = jsonObj.getInt(ApiConstants.KEY_ORDER_ID);
                } else if (key.equals(ApiConstants.KEY_ORDER_NUMBER)) {
                    this.orderNum = jsonObj.getString(ApiConstants.KEY_ORDER_NUMBER);
                } else if (key.equals(ApiConstants.KEY_ORDER_TIME)) {
                    this.orderTime = jsonObj.getString(ApiConstants.KEY_ORDER_TIME);
                } else if (key.equals(ApiConstants.KEY_ORDER_SHOP_ID)) {
                    this.shopId = jsonObj.getInt(ApiConstants.KEY_ORDER_SHOP_ID);
                } else if (key.equals(ApiConstants.KEY_ORDER_SHOP_NAME)) {
                    this.shopName = jsonObj.getString(ApiConstants.KEY_ORDER_SHOP_NAME);
                } else if (key.equals(ApiConstants.KEY_ORDER_NAME)) {
                    this.name = jsonObj.getString(ApiConstants.KEY_ORDER_NAME);
                } else if (key.equals(ApiConstants.KEY_ORDER_PHONE)) {
                    this.phone = jsonObj.getString(ApiConstants.KEY_ORDER_PHONE);
                } else if (key.equals(ApiConstants.KEY_ORDER_ADDRESS)) {
                    this.address = jsonObj.getString(ApiConstants.KEY_ORDER_ADDRESS);
                } else if (key.equals(ApiConstants.KEY_ORDER_PRICE)) {
                    this.orderPrice = jsonObj.getDouble(ApiConstants.KEY_ORDER_PRICE);
                } else if (key.equals(ApiConstants.KEY_ORDER_DEDUCTION)) {
                    this.deduction = jsonObj.getDouble(ApiConstants.KEY_ORDER_DEDUCTION);
                } else if (key.equals(ApiConstants.KEY_ORDER_SHIPPING_FEE)) {
                    this.shippingFee = jsonObj.getDouble(ApiConstants.KEY_ORDER_SHIPPING_FEE);
                } else if (key.equals(ApiConstants.KEY_ORDER_SETTLED_PRICE)) {
                    this.settledPrice = jsonObj.getDouble(ApiConstants.KEY_ORDER_SETTLED_PRICE);
                } else if (key.equals(ApiConstants.KEY_ORDER_EXTRA_MSG)) {
                    this.extraMsg = jsonObj.getString(ApiConstants.KEY_ORDER_EXTRA_MSG);
                } else if (key.equals(ApiConstants.KEY_ORDER_GOODS_LIST)) {
                    JSONArray goodsXCountArr = jsonObj.getJSONArray(ApiConstants.KEY_ORDER_GOODS_LIST);
                    JSONObject goodsXCountObj = null;
                    GoodsXCount goodsXCount = null;
                    for (int i = 0; i < goodsXCountArr.length(); i++) {
                        goodsXCountObj = goodsXCountArr.getJSONObject(i);
                        goodsXCount = new GoodsXCount(goodsXCountObj);
                        goodsList.add(goodsXCount);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNum='" + orderNum + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", orderPrice=" + orderPrice +
                ", deduction=" + deduction +
                ", shippingFee=" + shippingFee +
                ", settledPrice=" + settledPrice +
                ", extraMsg='" + extraMsg + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
