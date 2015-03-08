package com.cisoft.lazyorder;

import android.app.Application;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.finals.SPConstants;

import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.Properties;

/**
 * Created by comet on 2014/10/22.
 */
public class AppContext extends Application {

    private boolean mIsLogin = false;	//登录状态
    private int mLoginUserId = 0;	//登录用户的id
    private String mLoginAccount = "";  //登录用户的帐户名
    private int mSchoolId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化登录信息
        initLoginInfo();
    }

    /**
     * 获取购物车
     */
    public GoodsCart getGoodsCart() {
        return GoodsCart.getInstance();
    }

    /**
     * 用户是否登录
     * @return
     */
    public boolean isLogin() {
        return mIsLogin;
    }

    /**
     * 获取登录用户id
     * @return
     */
    public int getLoginUid() {
        return mLoginUserId;
    }

    /**
     * 获取登录帐户名
     * PS：当用户名为空时，就以手机号作为帐户名
     * @return
     */
    public String getLoginAccount() {
        return mLoginAccount;
    }

    /**
     * 获取用户所在学校的id
     * @return
     */
    public int getSchoolId() {
        return mSchoolId;
    }

    /**
     * 用户注销
     */
    public void logout() {
        mIsLogin = false;
        mLoginUserId = 0;
        mLoginAccount = "";
        cleanLoginInfo();
    }

    /**
     * 初始化用户登录信息
     */
    public void initLoginInfo() {
        User loginUser = getLoginInfo();
        if(loginUser != null && loginUser.getUserId() > 0){
            mLoginUserId = loginUser.getUserId();
            String loginUserName = loginUser.getUserName();
            if (StringUtils.isEmpty(loginUserName)) {
                mLoginAccount = loginUser.getUserPhone();
            } else {
                mLoginAccount = loginUserName;
            }
            mIsLogin = true;
        }else{
            logout();
        }
    }

    /**
     * 保存登录信息
     * @param user
     */
    public void saveLoginInfo(final User user) {
        mLoginUserId = user.getUserId();
        String loginUserName = user.getUserName();
        if (StringUtils.isEmpty(loginUserName)) {
            mLoginAccount = user.getUserPhone();
        } else {
            mLoginAccount = loginUserName;
        }
        mIsLogin = true;
        setProperties(new Properties() {{
            setProperty(ApiConstants.KEY_ACCOUNT_USER_ID, String.valueOf(user.getUserId()));
            setProperty(ApiConstants.KEY_ACCOUNT_USER_NAME, user.getUserName());
            setProperty(ApiConstants.KEY_ACCOUNT_USER_EMAIL, user.getUserEmail());
            setProperty(ApiConstants.KEY_ACCOUNT_USER_PHONE, user.getUserPhone());
            setProperty(ApiConstants.KEY_ACCOUNT_USER_PWD, user.getUserPwd());

            if (user.getDefAddressInfo() != null
                    && !StringUtils.isEmpty(user.getDefAddressInfo().getName())
                    && !StringUtils.isEmpty(user.getDefAddressInfo().getPhone())
                    && !StringUtils.isEmpty(user.getDefAddressInfo().getAddress())) {
                setProperty(ApiConstants.KEY_ADDRESS_NAME, user.getDefAddressInfo().getName());
                setProperty(ApiConstants.KEY_ADDRESS_PHONE, user.getDefAddressInfo().getPhone());
                setProperty(ApiConstants.KEY_ADDRESS_ADDRESS, user.getDefAddressInfo().getAddress());
            }
        }});
    }

    /**
     * 获取登录信息
     * @return
     */
    public User getLoginInfo() {
        User lu = new User();
        lu.setUserId(StringUtils.toInt(getProperty(ApiConstants.KEY_ACCOUNT_USER_ID)));
        lu.setUserName(getProperty(ApiConstants.KEY_ACCOUNT_USER_NAME));
        lu.setUserPhone(getProperty(ApiConstants.KEY_ACCOUNT_USER_PHONE));
        lu.setUserEmail(getProperty(ApiConstants.KEY_ACCOUNT_USER_EMAIL));
        lu.setUserPwd(getProperty(ApiConstants.KEY_ACCOUNT_USER_PWD));

        if (!StringUtils.isEmpty(getProperty(ApiConstants.KEY_ADDRESS_NAME))
                && !StringUtils.isEmpty(getProperty(ApiConstants.KEY_ADDRESS_PHONE))
                && !StringUtils.isEmpty(getProperty(ApiConstants.KEY_ADDRESS_ADDRESS))) {
            AddressInfo defAddress = new AddressInfo(
                    getProperty(ApiConstants.KEY_ADDRESS_NAME),
                    getProperty(ApiConstants.KEY_ADDRESS_PHONE),
                    getProperty(ApiConstants.KEY_ADDRESS_ADDRESS), 1);
            lu.setDefAddressInfo(defAddress);
        }

        return lu;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        removeProperty(ApiConstants.KEY_ACCOUNT_USER_ID, ApiConstants.KEY_ACCOUNT_USER_NAME,
                ApiConstants.KEY_ACCOUNT_USER_PHONE, ApiConstants.KEY_ACCOUNT_USER_EMAIL,
                ApiConstants.KEY_ACCOUNT_USER_PWD);
    }


    /**
     * 得到最近一次登录的账户
     */
    public String getRecentAccount() {
        String recentName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ACCOUNT, "");
        return recentName;
    }

    /**
     * 设置最近一次登录的账户
     */
    public void setRecentAccount(String recentAccount) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ACCOUNT, recentAccount);
    }

    /**
     * 得到最近一次输入的姓名
     */
    public String getRecentName() {
        String recentName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_NAME, "");
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
    public String getRecentPhone() {
        String recentPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, "");

        return recentPhoneNum;
    }

    /**
     * 设置最近一次输入的手机号码
     *
     * @return
     */
    public void setRecentPhone(String recentPhone) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_PHONE_NUM, recentPhone);
    }

    /**
     * 得到最近一次输入的地址
     *
     * @return
     */
    public String getRecentAddress() {
        String recentPhoneNum = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ADDRESS, "");

        return recentPhoneNum;
    }

    /**
     * 设置最近一次输入的地址
     *
     * @return
     */
    public void setRecentAddress(String recentAddress) {
        PreferenceHelper.write(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_RECENT_ADDRESS, recentAddress);
    }


    public void setProperties(Properties ps){
        AppConfig.getAppConfig(this).setProps(ps);
    }

    public Properties getProperties(){
        return AppConfig.getAppConfig(this).getProps();
    }

    public void setProperty(String key, String value){
        AppConfig.getAppConfig(this).setValue(key, value);
    }

    public String getProperty(String key){
        return AppConfig.getAppConfig(this).getValue(key);
    }

    public void removeProperty(String...key){
        AppConfig.getAppConfig(this).remove(key);
    }

}
