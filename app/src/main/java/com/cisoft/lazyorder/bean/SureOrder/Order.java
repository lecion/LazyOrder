package com.cisoft.lazyorder.bean.SureOrder;

/**
 * Created by comet on 2014/10/26.
 */
public class Order {
    private int id;
    private int goodId;
    private String goodName;
    private int shopId;
    private String shopName;
    private int orderTotal;
    private double goodPrice;


    public Order(){}

    public Order(int goodId, String goodName, int shopId, String shopName, int orderTotal, double goodPrice) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.shopId = shopId;
        this.shopName = shopName;
        this.orderTotal = orderTotal;
        this.goodPrice = goodPrice;
    }

    public Order(int id, int goodId, int shopId, String goodName, String shopName, double goodPrice, int orderTotal) {
        this.id = id;
        this.goodId = goodId;
        this.shopId = shopId;
        this.goodName = goodName;
        this.shopName = shopName;
        this.goodPrice = goodPrice;
        this.orderTotal = orderTotal;
    }


    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
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

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", goodId=" + goodId +
                ", goodName='" + goodName + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", orderTotal=" + orderTotal +
                ", goodPrice=" + goodPrice +
                '}';
    }
}
