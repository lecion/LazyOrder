package com.cisoft.lazyorder.bean.shop;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Created by comet on 2014/10/16.
 */
public class Shop extends AbsBean{
    public int id;
    public String name;
    public String openTime;
    public String closeTime;
    public int monthSales;
    public int openState;
    public String faceImgUrl;
    public String promotionInfo;
    public String address;

    public Shop(){};

    public Shop(int id, String name, String openTime, String closeTime, int monthSales, String faceImgUrl, String promotionInfo, String address) {
        this.id = id;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.monthSales = monthSales;
        this.faceImgUrl = faceImgUrl;
        this.promotionInfo = promotionInfo;
        this.address = address;
    }

    public Shop(String name, String openTime, String closeTime, int monthSales, String faceImgUrl, String promotionInfo, String address) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.monthSales = monthSales;
        this.faceImgUrl = faceImgUrl;
        this.promotionInfo = promotionInfo;
        this.address = address;
    }

    
    public Shop(JSONObject jsonObj){
    	this.parse(jsonObj);
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }


    public String getOpenTime() {
        return openTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }


    public String getCloseTime() {
        return closeTime;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public int getMonthSales() {
        return monthSales;
    }


    public String getFaceImgUrl() {
        return faceImgUrl;
    }

    public void setFaceImgUrl(String faceImgUrl) {
        this.faceImgUrl = faceImgUrl;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }

    public String getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(String promotionInfo) {
        this.promotionInfo = promotionInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void parse(JSONObject jsonObj){
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_MER_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_MER_ID);
                } else if (key.equals(ApiConstants.KEY_MER_NAME)) {
                    this.name = jsonObj.getString(ApiConstants.KEY_MER_NAME);
                } else if(key.equals(ApiConstants.KEY_MER_OPEN_TIME)) {
                    this.openTime = jsonObj.getString(ApiConstants.KEY_MER_OPEN_TIME);
                } else if(key.equals(ApiConstants.KEY_MER_CLOSE_TIME)) {
                    this.closeTime = jsonObj.getString(ApiConstants.KEY_MER_CLOSE_TIME);
                } else if(key.equals(ApiConstants.KEY_MER_MONTH_SALES)) {
                    this.monthSales = jsonObj.getInt(ApiConstants.KEY_MER_MONTH_SALES);
                } else if(key.equals(ApiConstants.KEY_MER_FACE_PIC)) {
                    this.faceImgUrl = jsonObj.getString(ApiConstants.KEY_MER_FACE_PIC);
                } else if(key.equals(ApiConstants.KEY_MER_OPEN_STATE)) {
                    this.openState = jsonObj.getInt(ApiConstants.KEY_MER_OPEN_STATE);
                } else if(key.equals(ApiConstants.KEY_MER_PROMOTION_INFO)) {
                    this.promotionInfo = jsonObj.getString(ApiConstants.KEY_MER_PROMOTION_INFO);
                } else if(key.equals(ApiConstants.KEY_MER_ADDRESS)) {
                    this.address = jsonObj.getString(ApiConstants.KEY_MER_ADDRESS);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


	@Override
	public String toString() {
		return "Shop [id=" + id + ", name=" + name + ", openTime=" + openTime
				+ ", closeTime=" + closeTime + ", monthSales=" + monthSales
				+ ", openState=" + openState + ", faceImgUrl=" + faceImgUrl
				+ ", promotionInfo=" + promotionInfo + ", address=" + address
				+ "]";
	}
    
}
