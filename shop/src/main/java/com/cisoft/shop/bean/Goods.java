package com.cisoft.shop.bean;

import com.cisoft.shop.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Lecion on 10/19/14.
 */
public class Goods extends AbsBean implements Serializable{
    private int id;
    private String cmName;
    private String cmPicture;
    private int cateId;
    private String catName;
    private int salesNum;
    private double cmPrice;
    private int orderNum;
    private int shopId;
    private String shopName;
    private int state;

    public Goods() {}

    public Goods(int salesNum, int id, String cmName, String cmPicture, int cateId, String catName, double cmPrice) {
        this.salesNum = salesNum;
        this.id = id;
        this.cmName = cmName;
        this.cmPicture = cmPicture;
        this.cateId = cateId;
        this.catName = catName;
        this.cmPrice = cmPrice;
        this.orderNum = 0;
        this.state = 1;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Goods(JSONObject obj) {
        this.orderNum = 0;
        this.state = 1;
        this.parse(obj);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public String getCmPicture() {
        return cmPicture;
    }

    public void setCmPicture(String cmPicture) {
        this.cmPicture = cmPicture;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public double getCmPrice() {
        return cmPrice;
    }

    public void setCmPrice(double cmPrice) {
        this.cmPrice = cmPrice;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_COM_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_COM_ID);
                } else if (key.equals(ApiConstants.KEY_COM_NAME)) {
                    this.cmName = jsonObj.getString(ApiConstants.KEY_COM_NAME);
                } else if(key.equals(ApiConstants.KEY_COM_PICTURE)) {
                    this.cmPicture = jsonObj.getString(ApiConstants.KEY_COM_PICTURE);
                } else if(key.equals(ApiConstants.KEY_COM_CAT_ID)) {
                    this.cateId = jsonObj.getInt(ApiConstants.KEY_COM_CAT_ID);
                } else if(key.equals(ApiConstants.KEY_COM_CAT_NAME)) {
                    this.catName = jsonObj.getString(ApiConstants.KEY_COM_CAT_NAME);
                } else if(key.equals(ApiConstants.KEY_COM_SALES_NUM)) {
                    this.salesNum = jsonObj.getInt(ApiConstants.KEY_COM_SALES_NUM);
                } else if(key.equals(ApiConstants.KEY_COM_PRICE)) {
                    this.cmPrice = jsonObj.getInt(ApiConstants.KEY_COM_PRICE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
