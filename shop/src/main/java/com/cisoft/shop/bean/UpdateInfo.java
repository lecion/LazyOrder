package com.cisoft.shop.bean;

import com.cisoft.shop.Api;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Lecion on 3/24/15.
 */


public class UpdateInfo extends AbsBean implements Serializable{
    private static final long serialVersionUID = -2010973549400726800L;
    private int versionCode;
    private String versionName;
    private String updateInfo;
    private String url;

    public UpdateInfo(JSONObject jsonObject) {
        this.parse(jsonObject);
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(Api.KEY_SETTING_UPDATE_CONTENT)) {
                    setUpdateInfo(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_SETTING_DOWNLOAD_URL)) {
                    setUrl(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_SETTING_VERSION_CODE)) {
                    setVersionCode(jsonObj.getInt(key));
                } else if (key.equals(Api.KEY_SETTING_VERSION_NAME)) {
                    setVersionName(jsonObj.getString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
