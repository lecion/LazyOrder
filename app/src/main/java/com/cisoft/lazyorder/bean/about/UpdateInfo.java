package com.cisoft.lazyorder.bean.about;

/**
 * Created by comet on 2014/12/2.
 */

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

public class UpdateInfo extends BaseBean implements Serializable {

    private int versionCode; //当前软件版本号
    private String versionName; //当前软件版本名称
    private String downloadUrl;     //获取到的软件地址
    private String updateContent; //更新内容

    public UpdateInfo(int versionCode, String versionName, String downloadUrl, String updateContent) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.downloadUrl = downloadUrl;
        this.updateContent = updateContent;
    }

    public UpdateInfo(JSONObject jsonObj) {
        this.parse(jsonObj);
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }


    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ABOUT_VERSION_CODE)) {
                    this.versionCode = jsonObj.getInt(ApiConstants.KEY_ABOUT_VERSION_CODE);
                } else if (key.equals(ApiConstants.KEY_ABOUT_VERSION_NAME)) {
                    this.versionName = jsonObj.getString(ApiConstants.KEY_ABOUT_VERSION_NAME);
                } else if(key.equals(ApiConstants.KEY_ABOUT_DOWNLOAD_URL)) {
                    this.downloadUrl = jsonObj.getString(ApiConstants.KEY_ABOUT_DOWNLOAD_URL);
                } else if(key.equals(ApiConstants.KEY_ABOUT_UPDATE_CONTENT)) {
                    this.updateContent = jsonObj.getString(ApiConstants.KEY_ABOUT_UPDATE_CONTENT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", updateContent='" + updateContent + '\'' +
                '}';
    }
}
