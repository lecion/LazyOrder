package com.cisoft.lazyorder.core.sureinfo;

import android.content.Context;

import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;

/**
 * Created by comet on 2014/11/8.
 */
public class OrderService extends AbsService {

    public OrderService(Context context) {
        super(context, ApiConstants.MODULE_BUILD);
    }


    /**
     * 提交订单给服务器
     */
    public void submitOrderForServer(){
//        super.asyncUrlGet();
    }





    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode){
            default:
                stateInfo = super.getResponseStateInfo(stateCode);
        }

        return stateInfo;
    }
}
