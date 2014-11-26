package com.cisoft.lazyorder.bean.order;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by comet on 2014/11/15.
 */
public class ExpressOrder extends AbsBean implements Serializable{

    private int id;
    private String userName;
    private String userPhone;
    private String submitTime;
    private int buildingId;
    private String dormitory;
    private String address;
    private String smsCotent;
    private String extraMsg;
    private int deliveryMoney;

    public ExpressOrder(){}

    public ExpressOrder(JSONObject jsonObj){
        this.parse(jsonObj);
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getDormitory() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSmsCotent() {
        return smsCotent;
    }

    public void setSmsCotent(String smsCotent) {
        this.smsCotent = smsCotent;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

    public int getDeliveryMoney() {
        return deliveryMoney;
    }

    public void setDeliveryMoney(int deliveryMoney) {
        this.deliveryMoney = deliveryMoney;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_ID);
                } else if (key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_BUILDING_ID)) {
                    this.buildingId =  jsonObj.getInt(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_BUILDING_ID);
                } else if (key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_ROOM_NUM)) {
                    this.dormitory =  jsonObj.getString(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_ROOM_NUM);
                } else if (key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_SSM)) {
                    this.smsCotent =  jsonObj.getString(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_SSM);
                } else if (key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_EXTRA_MSG)) {
                    this.extraMsg = jsonObj.getString(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_EXTRA_MSG);
                } else if(key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_DELIVERY_MONEY)) {
                    this.deliveryMoney = jsonObj.getInt(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_DELIVERY_MONEY);
                } else if(key.equals(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_SUBMIT_TIME)) {
                    this.submitTime = jsonObj.getString(ApiConstants.KEY_ORDER_EXPRESS_ORDER_LIST_SUBMIT_TIME);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
