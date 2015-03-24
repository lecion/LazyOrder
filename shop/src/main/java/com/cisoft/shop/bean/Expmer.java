package com.cisoft.shop.bean;

import com.cisoft.shop.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Lecion on 2/21/15.
 */
public class Expmer extends AbsBean implements Serializable{
    private int id;
    private String cID;
    private String address;
    private String openTime;
    private String closeTime;
    private String pic;
    private int operatingState;
    private String sales;
    private String name;

    public Expmer() {}

    public Expmer(JSONObject jsonObject) {
        this.parse(jsonObject);
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getOperatingState() {
        return operatingState;
    }

    public void setOperatingState(int operatingState) {
        this.operatingState = operatingState;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public void setId(int id) {
        this.id = id;

    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    @Override
    public void parse(JSONObject jsonObj) {
        try {
            Iterator<String> iterator =  jsonObj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals(Api.KEY_EXPMER_ID)) {
                    setId(jsonObj.getInt(key));
                } else if(key.equals(Api.KEY_EXPMER_OPENTIME)) {
                    setOpenTime(jsonObj.getString(key));
                } else if(key.equals(Api.KEY_EXPMER_CLOSETIME)) {
                    setCloseTime(jsonObj.getString(key));
                } else if(key.equals(Api.KEY_EXPMER_PIC)) {
                    setPic(jsonObj.getString(key));
                } else if(key.equals(Api.KEY_EXPMER_OPERATING_STATE)) {
                    setOperatingState(jsonObj.getInt(key));
                } else if(key.equals(Api.KEY_EXPMER_SALES)) {
                    setSales(jsonObj.getString(key));
                } else if(key.equals(Api.KEY_EXPMER_ADDRESS)) {
                    setAddress(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_MER_CID)) {
                    setcID(jsonObj.getString(key));
                } else if (key.equals(Api.KEY_EXPMER_EXPMER_NAME)){
                    setName(jsonObj.getString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
