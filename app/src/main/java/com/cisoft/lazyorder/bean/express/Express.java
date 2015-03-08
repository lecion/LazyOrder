package com.cisoft.lazyorder.bean.express;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by comet on 2014/11/15.
 */
public class Express extends BaseBean implements Serializable{

    private int id;
    private String userName;
    private String userPhone;
    private String address;
    private String state;
    private String expressNum;
    private String submitTime;
    private String obtainTime;
    private String smsCotent;
    private String extraMsg;
    private double shippingFee;

    public Express(){}

    public Express(JSONObject jsonObj){
        this.parse(jsonObj);
    };

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getObtainTime() {
        return obtainTime;
    }

    public void setObtainTime(String obtainTime) {
        this.obtainTime = obtainTime;
    }

    public String getSmsCotent() {
        return smsCotent;
    }

    public void setSmsCotent(String smsCotent) {
        this.smsCotent = smsCotent;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_EXPRESS_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_EXPRESS_ID);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_NAME)) {
                    this.userName = jsonObj.getString(ApiConstants.KEY_EXPRESS_NAME);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_PHONE)) {
                    this.userPhone = jsonObj.getString(ApiConstants.KEY_EXPRESS_PHONE);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_ADDRESS)) {
                    this.address = jsonObj.getString(ApiConstants.KEY_EXPRESS_ADDRESS);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_NUMBER)) {
                    this.expressNum = jsonObj.getString(ApiConstants.KEY_EXPRESS_NUMBER);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_SUBMIT_TIME)) {
                    this.submitTime = jsonObj.getString(ApiConstants.KEY_EXPRESS_SUBMIT_TIME);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_OBTAIN_TIME)) {
                    this.obtainTime = jsonObj.getString(ApiConstants.KEY_EXPRESS_OBTAIN_TIME);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_SMS_CONTENT)) {
                    this.smsCotent = jsonObj.getString(ApiConstants.KEY_EXPRESS_SMS_CONTENT);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_EXTRA_MSG)) {
                    this.extraMsg = jsonObj.getString(ApiConstants.KEY_EXPRESS_EXTRA_MSG);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_SHIPPING_FEE)) {
                    this.shippingFee = jsonObj.getDouble(ApiConstants.KEY_EXPRESS_SHIPPING_FEE);
                } else if (key.equals(ApiConstants.KEY_EXPRESS_STATE)) {
                    this.state = jsonObj.getString(ApiConstants.KEY_EXPRESS_STATE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
