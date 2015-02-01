package com.cisoft.lazyorder.bean.express;

import java.io.Serializable;

/**
 * Created by comet on 2014/12/3.
 */
public class SmsInfo implements Serializable{

    //短信内容
    private String smsbody;

    //发送短信的电话号码
    private String address;

    //发送短信的日期和时间
    private String date;

    //发送短信人的姓名
    private String person;

    //短信类型 1是接收到的，2是已发出
    private String type;


    public String getSmsbody() {
        return smsbody;
    }

    public void setSmsbody(String smsbody) {
        this.smsbody = smsbody;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
