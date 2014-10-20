package com.cisoft.lazyorder.bean;

import java.util.Iterator;
import org.json.JSONObject;

public abstract class AbsBean {

    protected boolean isContainKey(String targetKey, JSONObject jsonObj) {
        Iterator<String> iterator =  jsonObj.keys();
        boolean flag = false;
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (targetKey.equals(key)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public abstract void parse(JSONObject jsonObj);
}
