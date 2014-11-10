package com.cisoft.lazyorder.bean.sureinfo;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by comet on 2014/11/4.
 */
public class Build extends AbsBean {


    public int id;
    public String name;

    public Build(){};

    public Build(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Build(String name) {
        this.name = name;
    }


    public Build(JSONObject jsonObj){
        this.parse(jsonObj);
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }




    public void parse(JSONObject jsonObj){
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_BUILD_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_BUILD_ID);
                } else if (key.equals(ApiConstants.KEY_BUILD_NAME)) {
                    this.name = jsonObj.getString(ApiConstants.KEY_BUILD_NAME);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {
        return "Build{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
