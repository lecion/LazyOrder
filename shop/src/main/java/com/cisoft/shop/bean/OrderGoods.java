package com.cisoft.shop.bean;

import com.cisoft.shop.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Lecion on 10/19/14.
 */
public class OrderGoods extends AbsBean implements Serializable{
    private int id;
    private int comId;
    private String comName;
    private int comNum;
    private double price;

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderGoods() {}

    public OrderGoods(int id, String comName, int comNum) {
        this.id = id;
        this.comName = comName;
        this.comNum = comNum;
    }

    public int getComNum() {
        return comNum;
    }

    public void setComNum(int comNum) {
        this.comNum = comNum;
    }

    public OrderGoods(JSONObject obj) {
        this.parse(obj);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(Api.KEY_ORDER_ID)) {
                    setId(jsonObj.getInt(key));
                } else if (key.equals(Api.KEY_ORDER_COM_ID)) {
                    setComId(jsonObj.getInt(key));
                } else if (key.equals(Api.KEY_ORDER_COM_NAME)) {
                    setComName(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_ORDER_COM_NUM)) {
                    setComNum(jsonObj.getInt(key));
                } else if (key.equals(Api.KEY_ORDER_COM_PRICE)){
                    setPrice(jsonObj.getDouble(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
