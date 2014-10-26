package com.cisoft.lazyorder.bean.goods;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lecion on 10/26/14.
 */
public class GoodsCategory extends AbsBean{
    private int id;
    private String cateName;

    public GoodsCategory(int id, String cateName) {
        this.id = id;
        this.cateName = cateName;
    }

    public GoodsCategory(JSONObject obj) {
        this.parse(obj);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }


    @Override
    public void parse(JSONObject jsonObj) {
        try {
            if (isContainKey(ApiConstants.KEY_CAT_ID, jsonObj)) {
                setId(jsonObj.getInt(ApiConstants.KEY_CAT_ID));
            }
            if (isContainKey(ApiConstants.KEY_CAT_NAME, jsonObj)) {
                setCateName(jsonObj.getString(ApiConstants.KEY_CAT_NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
