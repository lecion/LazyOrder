package com.cisoft.lazyorder.bean.shop;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Created by comet on 2014/12/8.
 */
public class Advertise extends BaseBean {

    private String type;   //广告的类别
    private String imageUrl;   //广告图的url地址
    private String contentUrl; //如果是WebView式广告的话，内容的url地址
    private String contentTitle; //如果是WebView式广告的话，内容的url标题
    private int shopId; //如果是店家商品列表页式的广告,店家的id
    private String shopName;
    private String promotionInfo;
    private String address;
    private String faceUrl;

    public Advertise(){}

    public Advertise(JSONObject jsonObj){
        this.parse(jsonObj);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
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

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ADVERTISE_TYPE)) {
                    this.type = jsonObj.getString(ApiConstants.KEY_ADVERTISE_TYPE);
                } else if (key.equals(ApiConstants.KEY_ADVERTISE_IMAGE_URL)) {
                    this.imageUrl = jsonObj.getString(ApiConstants.KEY_ADVERTISE_IMAGE_URL);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_CONTENT_URL)) {
                    this.contentUrl = jsonObj.getString(ApiConstants.KEY_ADVERTISE_CONTENT_URL);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_CONTENT_TITLE)) {
                    this.contentTitle = jsonObj.getString(ApiConstants.KEY_ADVERTISE_CONTENT_TITLE);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_SHOP_ID)) {
                    this.shopId = jsonObj.getInt(ApiConstants.KEY_ADVERTISE_SHOP_ID);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_SHOP_NAME)) {
                    this.shopName = jsonObj.getString(ApiConstants.KEY_ADVERTISE_SHOP_NAME);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_SHOP_FACE_URL)) {
                    this.faceUrl = jsonObj.getString(ApiConstants.KEY_ADVERTISE_SHOP_FACE_URL);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_SHOP_ADDRESS)) {
                    this.address = jsonObj.getString(ApiConstants.KEY_ADVERTISE_SHOP_ADDRESS);
                } else if(key.equals(ApiConstants.KEY_ADVERTISE_SHOP_PROMOTION_INFO)) {
                    this.promotionInfo = jsonObj.getString(ApiConstants.KEY_ADVERTISE_SHOP_PROMOTION_INFO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Advertise{" +
                "type=" + type +
                ", imageUrl='" + imageUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", contentTitle='" + contentTitle + '\'' +
                ", shopId=" + shopId +
                ", shopName=" + shopName +
                ", promotionInfo='" + promotionInfo + '\'' +
                ", address='" + address + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                '}';
    }
}
