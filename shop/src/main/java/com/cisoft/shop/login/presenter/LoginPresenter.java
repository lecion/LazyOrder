package com.cisoft.shop.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.cisoft.shop.SpConstants;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.login.view.ILoginView;
import com.cisoft.shop.util.IOUtil;

import org.json.JSONObject;
import org.kymjs.aframe.utils.PreferenceHelper;

/**
 * Created by Lecion on 2/16/15.
 */
public class LoginPresenter {
    private ILoginView view;

    private ShopModel shopModel;

    private Context context;

    public LoginPresenter(Context context, ILoginView view) {
        this.view = view;
        shopModel = new ShopModel(context);
        this.context = context;
    }

    /**
     * 普通商家登陆
     */
    public void normalLogin(final String phone, final String pwd) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            view.showWrongInput();
            return;
        }
        view.showLoginProgress();
        shopModel.merLogin(phone, pwd, new ShopModel.ILoginListener() {
            @Override
            public void onSuccess(JSONObject data) {
                //登陆成功，保存商店信息
                Shop shop = new Shop(data);
                saveLoginInfo(1, phone, pwd, shop);
                view.skipToMainActivity();
            }

            @Override
            public void onFailure(String msg) {
                view.hideLoginProgress();
                view.showLoginFailed(msg);
            }
        });
    }

    /**
     * 快递商家登陆
     */
    public void expressLogin() {

    }

    /**
     * 保存登陆信息
     * @param phone
     * @param pwd
     * @param shop
     */
    private void saveLoginInfo(int type, String phone, String pwd, Shop shop) {
        PreferenceHelper.write(context, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_TYPE, type);
        PreferenceHelper.write(context, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PHONE, phone);
        PreferenceHelper.write(context, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PWD, pwd);
        PreferenceHelper.write(context, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_OBJ, IOUtil.encode(shop));
    }





}
