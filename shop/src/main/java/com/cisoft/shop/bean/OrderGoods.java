package com.cisoft.shop.bean;

import com.cisoft.shop.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Lecion on 10/19/14.
 */
public class OrderGoods extends AbsBean implements Serializable{
    private int id;
    private String cmName;
    private int orderNum;

    public OrderGoods() {}

    public OrderGoods(int id, String cmName, int orderNum) {
        this.id = id;
        this.cmName = cmName;
        this.orderNum = orderNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
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
                if (key.equals(ApiConstants.KEY_COM_COM_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_COM_COM_ID);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_GOOD_NAME)) {
                    this.cmName = jsonObj.getString(ApiConstants.KEY_HIS_ORDER_GOOD_NAME);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_GOOD_COUNT)) {
                    this.orderNum = jsonObj.getInt(ApiConstants.KEY_HIS_ORDER_GOOD_COUNT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
