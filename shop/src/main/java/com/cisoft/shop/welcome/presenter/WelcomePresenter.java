package com.cisoft.shop.welcome.presenter;

import android.content.Context;

import com.cisoft.shop.AppConfig;
import com.cisoft.shop.SpConstants;
import com.cisoft.shop.bean.Expmer;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.util.IOUtil;
import com.cisoft.shop.welcome.model.ExpmerModel;
import com.cisoft.shop.welcome.view.IWelcomeView;

import org.json.JSONObject;
import org.kymjs.aframe.utils.PreferenceHelper;

/**
 * Created by Lecion on 2/20/15.
 */
public class WelcomePresenter {

    Context ctx;
    ShopModel shopModel;
    ExpmerModel expmerModel;
    IWelcomeView view;

    public WelcomePresenter(Context ctx, IWelcomeView view) {
        this.ctx = ctx;
        this.view = view;
        shopModel = new ShopModel(ctx);
        expmerModel = new ExpmerModel(ctx);
    }

    public void checkAccount() {
        final int type = PreferenceHelper.readInt(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_TYPE, -1);
        if (type != -1) {

            final String phone = PreferenceHelper.readString(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PHONE);
            final String pwd = PreferenceHelper.readString(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PWD);
            if (type == AppConfig.TYPE_MERCHANT) {
                shopModel.merLogin(phone, pwd, new ShopModel.ILoginListener() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        //更新账户信息
                        IOUtil.saveLoginInfo(ctx, type, phone, pwd, new Shop(data));
                        view.skipToMain();
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.skipToLogin();
                    }
                });
            } else if (type == AppConfig.TYPE_EXPMER) {
                expmerModel.expmerLogin(phone, pwd, new ExpmerModel.ILoginListener() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        //更新账户信息
                        IOUtil.saveLoginInfo(ctx, type, phone, pwd, new Expmer(data));
                        view.skipToMain();
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.skipToLogin();
                    }
                });
            }
        } else {
            view.skipToLogin();
        }
    }

}
