package com.cisoft.shop.bean;


import com.cisoft.shop.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Lecion on 2015-3-8
 */
public class ExpressOrder extends AbsBean {

    private int id;
    private String address;             //地址信息
    private String content;
    private double distributionPrice;   //配送费
    private String expressNumber;
    private String expressTime;
    private String getMessageTime;
    private String message;
    private String statue;
    private String userName;
    private String userPhone;

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

    public ExpressOrder(JSONObject jsonObj){
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

    public String getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(String expressTime) {
        this.expressTime = expressTime;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(Api.KEY_EXPRESS_ID)) {
                    setId(jsonObj.getInt(Api.KEY_EXPRESS_ID));
                } else if (key.equals(Api.KEY_EXPRESS_ADDRESS)) {
                    setAddress(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_CONTENT)) {
                    setContent(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_DISTRIBUTION_PRICE)) {
                    setDistributionPrice(jsonObj.getDouble(key));
                } else if (key.equals(Api.KEY_EXPRESS_EXPRESS_NUMBER)) {
                    setExpressNumber(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_EXPRESS_TIME)) {
                    setExpressTime(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_GET_MESSAGE_TIME)) {
                    setGetMessageTime(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_MESSAGE)) {
                    setMessage(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_STATUE)) {
                    setStatue(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_USER_NAME)) {
                    setUserName(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPRESS_USER_PHONE)) {
                    setUserPhone(jsonObj.getString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGetMessageTime() {
        return getMessageTime;
    }

    public void setGetMessageTime(String getMessageTime) {
        this.getMessageTime = getMessageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
