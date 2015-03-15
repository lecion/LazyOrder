package com.cisoft.shop.bean;

import com.cisoft.shop.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Lecion on 2014/2/16.
 */
public class Shop extends AbsBean implements Serializable{
    private int id;
    private String name;
    private String openTime;
    private String closeTime;
    private int monthSales;
    private int operatingState;
    private String faceImgUrl;
    private String promotionInfo;
    private String address;
    private int nodistributionPrice;
    private String cID;

    public Shop(){};

    public Shop(int id, String name, String openTime, String closeTime, int monthSales, int operatingState, String faceImgUrl, String promotionInfo, String address, int nodistributionPrice, String cID) {
        this.id = id;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.monthSales = monthSales;
        this.operatingState = operatingState;
        this.faceImgUrl = faceImgUrl;
        this.promotionInfo = promotionInfo;
        this.address = address;
        this.nodistributionPrice = nodistributionPrice;
        this.cID = cID;
    }
    
    public Shop(JSONObject jsonObj){
    	this.parse(jsonObj);
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public int getOperatingState() {
        return operatingState;
    }

    public void setOperatingState(int operatingState) {
        this.operatingState = operatingState;
    }

    public String getFaceImgUrl() {
        return faceImgUrl;
    }

    public void setFaceImgUrl(String faceImgUrl) {
        this.faceImgUrl = faceImgUrl;
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

    public int getNodistributionPrice() {
        return nodistributionPrice;
    }

    public void setNodistributionPrice(int nodistributionPrice) {
        this.nodistributionPrice = nodistributionPrice;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public void parse(JSONObject jsonObj){
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(Api.KEY_MER_ID)) {
                    this.id = jsonObj.getInt(Api.KEY_MER_ID);
                } else if (key.equals(Api.KEY_MER_NAME)) {
                    setName(jsonObj.getString(Api.KEY_MER_NAME));
                } else if(key.equals(Api.KEY_MER_OPEN_TIME)) {
                    setOpenTime(jsonObj.getString(Api.KEY_MER_OPEN_TIME));
                } else if(key.equals(Api.KEY_MER_CLOSE_TIME)) {
                    setCloseTime(jsonObj.getString(Api.KEY_MER_CLOSE_TIME));
                } else if(key.equals(Api.KEY_MER_MONTH_SALES)) {
                    setMonthSales(jsonObj.getInt(Api.KEY_MER_MONTH_SALES));
                } else if(key.equals(Api.KEY_MER_FACE_PIC)) {
                    setFaceImgUrl(jsonObj.getString(Api.KEY_MER_FACE_PIC));
                } else if(key.equals(Api.KEY_MER_OPERATING_STATE)) {
                    setOperatingState(jsonObj.getInt(Api.KEY_MER_OPERATING_STATE));
                } else if(key.equals(Api.KEY_MER_PROMOTION_INFO)) {
                    setPromotionInfo(jsonObj.getString(Api.KEY_MER_PROMOTION_INFO));
                } else if(key.equals(Api.KEY_MER_ADDRESS)) {
                    setAddress(jsonObj.getString(Api.KEY_MER_ADDRESS));
                } else if (key.equals(Api.KEY_MER_NODISTRIBUTION_PRICE)) {
                    setNodistributionPrice(jsonObj.getInt(Api.KEY_MER_NODISTRIBUTION_PRICE));
                } else if (key.equals(Api.KEY_MER_CID)) {
                    setcID(Api.KEY_MER_CID);
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
				+ ", operatingState=" + operatingState + ", faceImgUrl=" + faceImgUrl
				+ ", promotionInfo=" + promotionInfo + ", address=" + address
				+ "]";
	}
}
