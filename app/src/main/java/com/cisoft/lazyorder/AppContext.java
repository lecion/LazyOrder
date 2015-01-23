package com.cisoft.lazyorder;

import android.app.Application;

import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.finals.SPConstants;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.utils.PreferenceHelper;
import org.kymjs.aframe.utils.StringUtils;

/**
 * Created by comet on 2014/10/22.
 */
public class AppContext extends Application {

    /**
     * 获取购物车
     */
    public GoodsCart getGoodsCart() {
        return GoodsCart.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.create(this);
        KJLoger.IS_DEBUG = AppConfig.IS_DEBUG;
    }


    /**
     * 通过手机号码登陆
     * @param phoneNumber
     */
    public void loginByPhone(String phoneNumber) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, phoneNumber);
    }

    /**
     * 手机号码登出
     */
    public void logout() {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, null);
    }

    /**
     * 是否登录
     * @return
     */
    public boolean isLogin() {
        String loginPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, null);

        return !StringUtils.isEmpty(loginPhoneNum);
    }


    public String getLoginPhoneNum() {
        String loginPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, null);

        return loginPhoneNum;
    }
}
