package com.cisoft.lazyorder.bean.goods;

import com.cisoft.lazyorder.bean.AbsBean;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Lecion on 10/26/14.
 */
public class Comment extends AbsBean{
    private int id;
    private String userName;
    private int userId;
    private String discussContent;
    private String contentTime;


    public Comment(JSONObject obj) {
        this.parse(obj);
    }

    public Comment(int id, String userName, int userId, String discussContent, String contentTime) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.discussContent = discussContent;
        this.contentTime = contentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDiscussContent() {
        return discussContent;
    }

    public void setDiscussContent(String discussContent) {
        this.discussContent = discussContent;
    }

    public String getContentTime() {
        return contentTime;
    }

    public void setContentTime(String contentTime) {
        this.contentTime = contentTime;
    }

    @Override
    public void parse(JSONObject jsonObj) {

        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_CAT_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_CAT_ID);
                } else if (key.equals(ApiConstants.KEY_DIS_USER_NAME)) {
                    this.userName = jsonObj.getString(ApiConstants.KEY_DIS_USER_NAME);
                } else if (key.equals(ApiConstants.KEY_DIS_USER_ID)) {
                    this.userId = jsonObj.getInt(ApiConstants.KEY_DIS_USER_ID);
                } else if (key.equals(ApiConstants.KEY_DIS_CONTENT)) {
                    this.discussContent = jsonObj.getString(ApiConstants.KEY_DIS_CONTENT);
                } else if (key.equals(ApiConstants.KEY_DIS_CONTENT_TIME)) {
                    this.contentTime = jsonObj.getString(ApiConstants.KEY_DIS_CONTENT_TIME);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
