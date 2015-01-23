package com.cisoft.lazyorder.bean.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by comet on 2014/11/15.
 */
public class DishOrder extends AbsBean implements Serializable{

    private int id;
    private int shopId; /* 店家id */
    private String shopName;    /* 店家名称 */
    private String userName;    /* 用户的姓名 */
    private String userPhone;   /* 用户的电话号码 */
    private String address; /* 用户的联系地址 */
    private int buildingId; /* 用户的楼栋id */
    private String dormitory;   /* 用户的寝室号 */
    private String extraMsg;    /* 额外留言 */
    private String submitTime;  /* 订单提交时间 */
    private int deliveryMoney;  /* 配送费 */
    private double moneyAll;    /* 订单总价 */
    private String orderNo; /* 订单编号 */
    private List<Goods> goodsList;  /* 所订购的商品列表 */

    public DishOrder(){}

    public DishOrder(JSONObject jsonObj){
        this.parse(jsonObj);
    };

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getDeliveryMoney() {
        return deliveryMoney;
    }

    public void setDeliveryMoney(int deliveryMoney) {
        this.deliveryMoney = deliveryMoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public double getMoneyAll() {
        return moneyAll;
    }

    public void setMoneyAll(double moneyAll) {
        this.moneyAll = moneyAll;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "DishOrder{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", address='" + address + '\'' +
                ", buildingId=" + buildingId +
                ", dormitory='" + dormitory + '\'' +
                ", extraMsg='" + extraMsg + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", deliveryMoney'" + deliveryMoney + '\'' +
                ", moneyAll=" + moneyAll +
                ", goodsList=" + goodsList +
                '}';
    }

    @Override
    public void parse(JSONObject jsonObj) {
        goodsList = new ArrayList<Goods>();
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_ORDER_FIND_ALL_ID);
                } else if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_SHOP_ID)) {
                    this.shopId =  jsonObj.getInt(ApiConstants.KEY_ORDER_FIND_ALL_SHOP_ID);
                } else if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_SHOP_NAME)) {
                    this.shopName =  jsonObj.getString(ApiConstants.KEY_ORDER_FIND_ALL_SHOP_NAME);
                } else if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_SUBMIT_TIME)) {
                    this.submitTime =  jsonObj.getString(ApiConstants.KEY_ORDER_FIND_ALL_SUBMIT_TIME);
                } else if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_ADDRESS)) {
                    this.address = jsonObj.getString(ApiConstants.KEY_ORDER_FIND_ALL_ADDRESS);
                } else if(key.equals(ApiConstants.KEY_ORDER_FIND_ALL_ORDER_NUM)) {
                    this.orderNo = jsonObj.getString(ApiConstants.KEY_ORDER_FIND_ALL_ORDER_NUM);
                } else if(key.equals(ApiConstants.KEY_ORDER_FIND_ALL_EXTRA_MSG)){
                    this.extraMsg = jsonObj.getString(ApiConstants.KEY_ORDER_FIND_ALL_EXTRA_MSG);
                } else if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_MONEY_ALL)) {
                    this.moneyAll =  jsonObj.getDouble(ApiConstants.KEY_ORDER_FIND_ALL_MONEY_ALL);
                } else if (key.equals(ApiConstants.KEY_ORDER_FIND_ALL_GOODS_LIST)) {
                    JSONArray goodsListArr = jsonObj.getJSONArray(ApiConstants.KEY_ORDER_FIND_ALL_GOODS_LIST);
                    JSONObject goodsObj = null;
                    Goods goods = null;
                    for (int i = 0; i < goodsListArr.length(); i++) {
                        goodsObj = goodsListArr.getJSONObject(i);
                        goods = new Goods();
                        goods.setId(goodsObj.getInt(ApiConstants.KEY_ORDER_FIND_ALL_GOODS_LIST_ITEM_GOODS_ID));
                        goods.setCmName(goodsObj.getString(ApiConstants.KEY_ORDER_FIND_ALL_GOODS_LIST_ITEM_GOODS_NAME));
                        goods.setOrderNum(goodsObj.getInt(ApiConstants.KEY_ORDER_FIND_ALL_GOODS_LIST_ITEM_GOODS_ORDERED_COUNT));
                        goodsList.add(goods);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
