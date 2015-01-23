package com.cisoft.shop.bean;


import com.cisoft.shop.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by comet on 2014/10/17.
 */
public class ShopCategory extends AbsBean{
    private int id;
    private String name;

    public ShopCategory() {}

    public ShopCategory(String name) {
        this.name = name;
    }

    public ShopCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShopCategory(JSONObject jsonObj) {
    	this.parse(jsonObj);
    }
    
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

	@Override
	public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_MC_CATEGORY_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_MC_CATEGORY_ID);
                } else if (key.equals(ApiConstants.KEY_MC_CATEGORY_NAME)) {
                    this.name = jsonObj.getString(ApiConstants.KEY_MC_CATEGORY_NAME);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}
}
