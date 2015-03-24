package com.cisoft.shop.bean;

import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by Lecion on 3/24/15.
 */


public class UpdateInfo extends AbsBean implements Serializable{
    private static final long serialVersionUID = -2010973549400726800L;
    private int versionCode;
    private String versionName;
    private String updateInfo;
    private String url;

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

    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
