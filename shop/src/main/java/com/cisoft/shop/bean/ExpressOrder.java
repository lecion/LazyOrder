package com.cisoft.shop.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.cisoft.shop.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Lecion on 2015-3-8
 */
public class ExpressOrder extends AbsBean implements Parcelable{

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

    public ExpressOrder(int id, String address, String content, double distributionPrice, String expressNumber, String expressTime, String getMessageTime, String message, String statue, String userName, String userPhone) {
        this.id = id;
        this.address = address;
        this.content = content;
        this.distributionPrice = distributionPrice;
        this.expressNumber = expressNumber;
        this.expressTime = expressTime;
        this.getMessageTime = getMessageTime;
        this.message = message;
        this.statue = statue;
        this.userName = userName;
        this.userPhone = userPhone;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(address);
        dest.writeString(content);
        dest.writeDouble(distributionPrice);
        dest.writeString(expressNumber);
        dest.writeString(expressTime);
        dest.writeString(getMessageTime);
        dest.writeString(message);
        dest.writeString(statue);
        dest.writeString(userName);
        dest.writeString(userPhone);
    }

    public static final Creator<ExpressOrder> CREATOR = new Creator<ExpressOrder>() {
        @Override
        public ExpressOrder createFromParcel(Parcel source) {
            ExpressOrder expressOrder = new ExpressOrder(source.readInt(),
                    source.readString(),
                    source.readString(),
                    source.readDouble(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString());
            return expressOrder;
        }

        @Override
        public ExpressOrder[] newArray(int size) {
            return new ExpressOrder[0];
        }
    };
}
