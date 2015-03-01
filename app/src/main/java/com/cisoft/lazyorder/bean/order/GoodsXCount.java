package com.cisoft.lazyorder.bean.order;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by comet on 2014/11/15.
 */
public class GoodsXCount extends BaseBean implements Serializable{

    private int goodsId;
    private String goodsName;
    private int count;

    public GoodsXCount(){}

    public GoodsXCount(int goodsId, String goodsName, int count) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.count = count;
    }

    public GoodsXCount(String goodsName, int count) {
        this.goodsName = goodsName;
        this.count = count;
    }

    public GoodsXCount(JSONObject jsonObj){
        this.parse(jsonObj);
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();

                if (key.equals(ApiConstants.KEY_ORDER_COM_ID)) {
                    this.goodsId = jsonObj.getInt(ApiConstants.KEY_ORDER_COM_ID);

                } else if(key.equals(ApiConstants.KEY_ORDER_COM_NAME)) {
                    this.goodsName = jsonObj.getString(ApiConstants.KEY_ORDER_COM_NAME);

                } else if (key.equals(ApiConstants.KEY_ORDER_GOODS_COUNT)) {
                    this.count =  jsonObj.getInt(ApiConstants.KEY_ORDER_GOODS_COUNT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "GoodsXCount{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", count=" + count +
                '}';
    }
}
