package com.cisoft.lazyorder;

import android.app.Application;

import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.finals.SPConstants;

import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by comet on 2014/10/22.
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.create(this);
    }

    /**
     * 获取购物车
     */
    public GoodsCart getGoodsCart() {
        return GoodsCart.getInstance();
    }

    /**
     * 通过手机号码登陆
     *
     * @param phoneNumber
     */
    public void loginByPhone(String phoneNumber) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, phoneNumber);

        LoginStateObserver.getInstance().notifyStateChanged();
    }

    /**
     * 手机号码登出
     */
    public void logout() {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, "");

        LoginStateObserver.getInstance().notifyStateChanged();
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        String loginPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, null);

        return !StringUtils.isEmpty(loginPhoneNum);
    }

    /**
     * 得到登录的手机号码
     *
     * @return
     */
    public String getLoginPhoneNum() {
        String loginPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LOGIN_PHONE_NUM, null);

        return loginPhoneNum;
    }



    /**
     * 得到最近一次输入的姓名
     */
    public String getRecentName() {
        String recentName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_NAME, null);

        return recentName;
    }

    /**
     * 设置最近一次输入的姓名
     */
    public void setRecentName(String recentName) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_NAME, recentName);
    }

    /**
     * 得到最近一次输入的手机号码
     *
     * @return
     */
    public String getRecentPhoneNum() {
        String recentPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, null);

        return recentPhoneNum;
    }

    /**
     * 设置最近一次输入的手机号码
     *
     * @return
     */
    public void setRecentPhoneNum(String recentPhone) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, recentPhone);
    }


    /**
     * 得到最近一次选择的楼栋id
     */
    public int getRecentBuidingId() {
        int recentBuildingId = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_ID, 0);

        return recentBuildingId;
    }

    /**
     * 设置最近一次选择的楼栋id
     */
    public void setRecentBuidingId(int recentBuildingId) {
        PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_ID, recentBuildingId);
    }

    /**
     * 得到最近一次选择的楼栋名
     */
    public String getRecentBuidingName() {
        String recentBuildingName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_NAME, null);

        return recentBuildingName;
    }

    /**
     * 设置最近一次选择的楼栋名
     */
    public void setRecentBuidingName(String recentBuidingName) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_BUILDING_NAME, recentBuidingName);
    }

    /**
     * 得到最近一次输入的寝室号
     */
    public String getRecentRoomNum() {
        String rentRoomNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ROOM_NUM, null);

        return rentRoomNum;
    }

    /**
     * 设置最近一次输入的寝室号
     */
    public void setRecentRoomNum(String recentRoomNum) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ROOM_NUM, recentRoomNum);
    }

    /**
     * 得到保存的学校id
     * @return
     */
    public int getSchoolId() {
        int schoolId = PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_SCHOOL_ID, 1);

        return schoolId;
    }

    /**
     * 设置选择的学校id
     * @return
     */
    public void setSchoolId(int schoolId) {
        PreferenceHelper.readInt(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_SCHOOL_ID, schoolId);
    }

}
