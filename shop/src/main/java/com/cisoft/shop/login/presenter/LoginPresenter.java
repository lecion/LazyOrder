package com.cisoft.shop.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.cisoft.shop.bean.Expmer;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.login.view.ILoginView;
import com.cisoft.shop.util.IOUtil;
import com.cisoft.shop.welcome.model.ExpmerModel;

import org.json.JSONObject;

/**
 * Created by Lecion on 2/16/15.
 */
public class LoginPresenter {
    private ILoginView view;

    private ShopModel shopModel;

    private ExpmerModel expmerModel;

    private Context context;

    public LoginPresenter(Context context, ILoginView view) {
        this.view = view;
        shopModel = new ShopModel(context);
        expmerModel = new ExpmerModel(context);
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
                IOUtil.saveLoginInfo(context, 0, phone, pwd, shop);
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
    public void expressLogin(final String phone, final String pwd) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            view.showWrongInput();
            return;
        }
        view.showLoginProgress();
        expmerModel.expmerLogin(phone, pwd, new ExpmerModel.ILoginListener() {
            @Override
            public void onSuccess(JSONObject data) {
                //登陆成功，保存商店信息
                Expmer shop = new Expmer(data);
                IOUtil.saveLoginInfo(context, 1, phone, pwd, shop);
                view.skipToMainActivity();
            }

            @Override
            public void onFailure(String msg) {
                view.hideLoginProgress();
                view.showLoginFailed(msg);
            }
        });
    }

}
