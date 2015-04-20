package com.cisoft.shop.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.cisoft.shop.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lecion on 2014-12-09
 */
public class Order extends AbsBean implements Parcelable{

    private int id;
    private String userPhone;
    private String userName;
    private String timeGo;
    private String orderState;
    private String orderNumber;
    private String content;
    private double orderPrice;
    private double distributionPrice;   //配送费
    private String address;             //地址信息
    private double settledPrice;
    private double deduction;
    private List<OrderGoods> goodsList;

    public Order(int id, String userPhone, String userName, String timeGo, String orderState, String orderNumber, String content, double orderPrice, double distributionPrice, String address, double settledPrice, double deduction, List<OrderGoods> goodsList) {
        this.id = id;
        this.userPhone = userPhone;
        this.userName = userName;
        this.timeGo = timeGo;
        this.orderState = orderState;
        this.orderNumber = orderNumber;
        this.content = content;
        this.orderPrice = orderPrice;
        this.distributionPrice = distributionPrice;
        this.address = address;
        this.settledPrice = settledPrice;
        this.deduction = deduction;
        this.goodsList = goodsList;
    }

    public double getDistributionPrice() {
        return distributionPrice;
    }

    public void setDistributionPrice(double distributionPrice) {
        this.distributionPrice = distributionPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Order(JSONObject jsonObj){
        this.parse(jsonObj);
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeGo() {
        return timeGo;
    }

    public void setTimeGo(String timeGo) {
        this.timeGo = timeGo;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        goodsList = new ArrayList<OrderGoods>();
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(Api.KEY_ORDER_ID)) {
                    this.id = jsonObj.getInt(key);
                } else if (key.equals(Api.KEY_ORDER_USER_PHONE)) {
                    this.userPhone =  jsonObj.getString(key);
                } else if (key.equals(Api.KEY_ORDER_USER_NAME)) {
                    this.userName =  jsonObj.getString(key);
                } else if (key.equals(Api.KEY_ORDER_TIME_GO)) {
                    this.timeGo = jsonObj.getString(key);
                } else if (key.equals(Api.KEY_ORDER_ORDER_STATUE)) {
                    this.orderState = jsonObj.getString(key);
                } else if (key.equals(Api.KEY_ORDER_ORDER_NUMBER)) {
                    this.orderNumber = jsonObj.getString(key);
                } else if (key.equals(Api.KEY_ORDER_ORDER_CONTENT)) {
                    this.content = jsonObj.getString(key);
                } else if (key.equals(Api.KEY_ORDER_RDER_PRICE)) {
                    this.orderPrice = jsonObj.getDouble(key);
                } else if (key.equals(Api.KEY_ORDER_DISTRIBUTION_PRICE)) {
                    setDistributionPrice(jsonObj.getDouble(key));
                } else if (key.equals(Api.kEY_ORDER_ADDRESS)) {
                    setAddress(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_ORDER_DEDUCTION)) {
                    setDeduction(jsonObj.getDouble(key));
                } else if (key.equals(Api.KEY_ORDER_SETTLED_PRICE)) {
                    setSettledPrice(jsonObj.getDouble(key));
                }else if (key.equals(Api.KEY_ORDER_ORDER_COMMODITY_VO_LIST)) {
                    JSONArray goodListArr = jsonObj.getJSONArray(key);
                    JSONObject goodsObj = null;
                    for (int i = 0; i < goodListArr.length(); i++) {
                        goodsObj = goodListArr.getJSONObject(i);
                        goodsList.add(new OrderGoods(goodsObj));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getSettledPrice() {
        return settledPrice;
    }

    public void setSettledPrice(double settledPrice) {
        this.settledPrice = settledPrice;
    }

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userPhone);
        dest.writeString(userName);
        dest.writeString(timeGo);
        dest.writeString(orderState);
        dest.writeString(orderNumber);
        dest.writeString(content);
        dest.writeDouble(orderPrice);
        dest.writeDouble(distributionPrice);
        dest.writeString(address);
        dest.writeDouble(settledPrice);
        dest.writeDouble(deduction);
        dest.writeList(goodsList);

    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            Order order = new Order(
                    source.readInt(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readDouble(),
                    source.readDouble(),
                    source.readString(),
                    source.readDouble(),
                    source.readDouble(),
                    source.readArrayList(new ArrayList<OrderGoods>().getClass().getClassLoader())
            );
            return order;
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[0];
        }
    };
}
