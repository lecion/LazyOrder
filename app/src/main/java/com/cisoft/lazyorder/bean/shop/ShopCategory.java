package com.cisoft.lazyorder.bean.shop;

import org.json.JSONException;
import org.json.JSONObject;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;

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
            if (isContainKey(ApiConstants.KEY_MC_CATEGORY_ID, jsonObj)) {
            	this.id= jsonObj.getInt(ApiConstants.KEY_MC_CATEGORY_ID);
            }

            if (isContainKey(ApiConstants.KEY_MC_CATEGORY_NAME, jsonObj)) {
            	this.name = jsonObj.getString(ApiConstants.KEY_MC_CATEGORY_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
	}
}
