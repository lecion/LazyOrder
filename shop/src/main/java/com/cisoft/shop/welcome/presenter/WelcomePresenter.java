package com.cisoft.shop.welcome.presenter;

import android.content.Context;
import android.util.Log;

import com.cisoft.shop.SpConstants;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.welcome.view.IWelcomeView;

import org.json.JSONObject;
import org.kymjs.aframe.utils.PreferenceHelper;

/**
 * Created by Lecion on 2/20/15.
 */
public class WelcomePresenter {

    Context ctx;
    ShopModel shopModel;
    IWelcomeView view;

    public WelcomePresenter(Context ctx, IWelcomeView view) {
        this.ctx = ctx;
        this.view = view;
        shopModel = new ShopModel(ctx);
    }


    public void checkAccount() {

        int type = PreferenceHelper.readInt(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_TYPE, -1);
        if (type != -1) {

            String phone = PreferenceHelper.readString(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PHONE);
            String pwd = PreferenceHelper.readString(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PWD);
            if (type == 0) {
                shopModel.merLogin(phone, pwd, new ShopModel.ILoginListener() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        view.skipToMain();
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.skipToLogin();
                    }
                });
            } else if (type == 1) {

            }
        } else {
            view.skipToLogin();
            Log.d("WelcomeActivity", "222222222");
        }
    }

}
