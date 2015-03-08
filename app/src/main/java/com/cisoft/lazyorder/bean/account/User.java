package com.cisoft.lazyorder.bean.account;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by comet on 2015/2/3.
 */
public class User extends BaseBean {

    private int userId;
    private String userName;
    private String userPhone;
    private String userPwd;
    private String userEmail;
    private String userFaceUrl;
    private AddressInfo defAddressInfo;

    public User() {}

    public User(String userName, String userPhone, String userPwd, String userEmail, String userFaceUrl) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPwd = userPwd;
        this.userEmail = userEmail;
        this.userFaceUrl = userFaceUrl;
    }

    public User(int userId, String userName, String userPhone, String userPwd, String userEmail, String userFaceUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPwd = userPwd;
        this.userEmail = userEmail;
        this.userFaceUrl = userFaceUrl;
    }

    public User(JSONObject jsonObj) {
        this.parse(jsonObj);
    }

    public String getUserFaceUrl() {
        return userFaceUrl;
    }

    public void setUserFaceUrl(String userFaceUrl) {
        this.userFaceUrl = userFaceUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public AddressInfo getDefAddressInfo() {
        return defAddressInfo;
    }

    public void setDefAddressInfo(AddressInfo defAddressInfo) {
        this.defAddressInfo = defAddressInfo;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ACCOUNT_USER_ID)) {
                    this.userId = jsonObj.getInt(ApiConstants.KEY_ACCOUNT_USER_ID);
                } else if (key.equals(ApiConstants.KEY_ACCOUNT_USER_NAME)) {
                    this.userName = jsonObj.getString(ApiConstants.KEY_ACCOUNT_USER_NAME);
                } else if(key.equals(ApiConstants.KEY_ACCOUNT_USER_PWD)) {
                    this.userPwd = jsonObj.getString(ApiConstants.KEY_ACCOUNT_USER_PWD);
                } else if(key.equals(ApiConstants.KEY_ACCOUNT_USER_PHONE)) {
                    this.userPhone = jsonObj.getString(ApiConstants.KEY_ACCOUNT_USER_PHONE);
                } else if(key.equals(ApiConstants.KEY_ACCOUNT_USER_EMAIL)) {
                    this.userEmail = jsonObj.getString(ApiConstants.KEY_ACCOUNT_USER_EMAIL);
                } else if(key.equals(ApiConstants.KEY_ACCOUNT_USER_FACE_URL)) {
                    this.userFaceUrl = jsonObj.getString(ApiConstants.KEY_ACCOUNT_USER_FACE_URL);
                } else if(key.equals(ApiConstants.KEY_ACCOUNT_USER_ACCOUNT)) {
                    JSONObject tmpJsonObj = jsonObj.getJSONObject(ApiConstants.KEY_ACCOUNT_DEF_ADDRESS);
                    this.defAddressInfo = new AddressInfo(tmpJsonObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userFaceUrl='" + userFaceUrl +
                '}';
    }
}
