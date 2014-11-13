package com.cisoft.lazyorder.bean.orderlist;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by comet on 2014/11/12.
 */
public class HistoryOrder extends AbsBean {

    private int id;
    private int shopId;
    private String shopName;
    private String date;
    private String address;
    private double totalPrice;
    private List<Map<String, String>> goodList;


    public HistoryOrder(){};

    public HistoryOrder(int id, int shopId, String shopName, String date, String address, double totalPrice, List<Map<String, String>> goodList) {
        this.id = id;
        this.shopId = shopId;
        this.shopName = shopName;
        this.date = date;
        this.address = address;
        this.totalPrice = totalPrice;
        this.goodList = goodList;
    }

    public HistoryOrder(int shopId, String shopName, String date, String address, List<Map<String, String>> goodList, double totalPrice) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.date = date;
        this.address = address;
        this.goodList = goodList;
        this.totalPrice = totalPrice;
    }

    public HistoryOrder(JSONObject jsonObj){
        this.parse(jsonObj);
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Map<String, String>> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<Map<String, String>> goodList) {
        this.goodList = goodList;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        goodList = new ArrayList<Map<String, String>>();
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_HIS_ORDER_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_HIS_ORDER_ID);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_MER_ID)) {
                   this.shopId =  jsonObj.getInt(ApiConstants.KEY_HIS_ORDER_MER_ID);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_MER_NAME)) {
                    this.shopName =  jsonObj.getString(ApiConstants.KEY_HIS_ORDER_MER_NAME);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_TIME)) {
                    this.date =  jsonObj.getString(ApiConstants.KEY_HIS_ORDER_TIME);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_ADDRESS)) {
                    this.address =  jsonObj.getString(ApiConstants.KEY_HIS_ORDER_ADDRESS);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_TOTAL_PRICE)) {
                    this.totalPrice =  jsonObj.getDouble(ApiConstants.KEY_HIS_ORDER_TOTAL_PRICE);
                } else if (key.equals(ApiConstants.KEY_HIS_ORDER_GOOD_LIST)) {
                    JSONArray goodListArr = jsonObj.getJSONArray(ApiConstants.KEY_HIS_ORDER_GOOD_LIST);
                    JSONObject goodObj = null;
                    Map<String, String> goodMap = null;
                    for (int i = 0; i < goodListArr.length(); i++) {
                        goodObj = goodListArr.getJSONObject(i);
                        goodMap = new HashMap<String, String>();
                        goodMap.put(ApiConstants.KEY_HIS_ORDER_GOOD_NAME, goodObj.getString(ApiConstants.KEY_HIS_ORDER_GOOD_NAME));
                        goodMap.put(ApiConstants.KEY_HIS_ORDER_GOOD_COUNT, String.valueOf(goodObj.getInt(ApiConstants.KEY_HIS_ORDER_GOOD_COUNT)));
                        goodList.add(goodMap);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
