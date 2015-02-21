package com.cisoft.shop.bean;

import com.cisoft.shop.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Lecion on 2/21/15.
 */
public class Expmer extends AbsBean{
    private int id;
    private String cID;
    private String address;
    private String openTime;
    private String closeTime;
    private String pic;
    private int operatingState;
    private String sales;

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
                if (key.equals(ApiConstants.KEY_EXPMER_ID)) {
                    setId(jsonObj.getInt(ApiConstants.KEY_EXPMER_ID));
                } else if(key.equals(ApiConstants.KEY_EXPMER_OPENTIME)) {
                    setOpenTime(jsonObj.getString(ApiConstants.KEY_EXPMER_OPENTIME));
                } else if(key.equals(ApiConstants.KEY_EXPMER_CLOSETIME)) {
                    setCloseTime(jsonObj.getString(ApiConstants.KEY_EXPMER_CLOSETIME));
                } else if(key.equals(ApiConstants.KEY_EXPMER_PIC)) {
                    setPic(jsonObj.getString(ApiConstants.KEY_EXPMER_PIC));
                } else if(key.equals(ApiConstants.KEY_EXPMER_OPERATING_STATE)) {
                    setOperatingState(jsonObj.getInt(ApiConstants.KEY_EXPMER_OPERATING_STATE));
                } else if(key.equals(ApiConstants.KEY_EXPMER_SALES)) {
                    setSales(jsonObj.getString(ApiConstants.KEY_EXPMER_SALES));
                } else if(key.equals(ApiConstants.KEY_EXPMER_ADDRESS)) {
                    setAddress(jsonObj.getString(ApiConstants.KEY_EXPMER_ADDRESS));
                } else if (key.equals(ApiConstants.KEY_MER_CID)) {
                    setcID(ApiConstants.KEY_MER_CID);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
