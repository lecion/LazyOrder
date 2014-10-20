package com.cisoft.lazyorder.bean.goods;

/**
 * Created by Lecion on 10/19/14.
 */
public class Goods {
    private int id;
    private String cmName;
    private String cmPicture;
    private int cateId;
    private String catName;
    private int salesNum;
    private double cmPrice;

    public Goods(int salesNum, int id, String cmName, String cmPicture, int cateId, String catName, double cmPrice) {
        this.salesNum = salesNum;
        this.id = id;
        this.cmName = cmName;
        this.cmPicture = cmPicture;
        this.cateId = cateId;
        this.catName = catName;
        this.cmPrice = cmPrice;
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
}
