package com.cisoft.lazyorder.bean.address;

import com.cisoft.lazyorder.bean.BaseBean;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

public class AddressInfo extends BaseBean implements Serializable{

    private int id;
    private String name;
    private String phone;
    private String address;
    private int isDefault;

    public AddressInfo() {}

    public AddressInfo(int id, String name, String phone, String address,
                       int isDefault) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public AddressInfo(String name, String phone, String address, int isDefault) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public AddressInfo(JSONObject jsonObj) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int isDefault() {
        return isDefault;
    }

    public void setDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator = jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(ApiConstants.KEY_ADDRESS_ID)) {
                    this.id = jsonObj.getInt(ApiConstants.KEY_ADDRESS_ID);
                } else if (key.equals(ApiConstants.KEY_ADDRESS_NAME)) {
                    this.name = jsonObj.getString(ApiConstants.KEY_ADDRESS_NAME);
                } else if (key.equals(ApiConstants.KEY_ADDRESS_PHONE)) {
                    this.phone = jsonObj.getString(ApiConstants.KEY_ADDRESS_PHONE);
                } else if (key.equals(ApiConstants.KEY_ADDRESS_ADDRESS)) {
                    this.address = jsonObj
                            .getString(ApiConstants.KEY_ADDRESS_ADDRESS);
                } else if (key.equals(ApiConstants.KEY_ADDRESS_IS_DEFAULT)) {
                    this.isDefault = jsonObj
                            .getInt(ApiConstants.KEY_ADDRESS_IS_DEFAULT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Address [id=" + id + ", name=" + name + ", phone=" + phone
                + ", address=" + address + ", isDefault=" + isDefault + "]";
    }

}

