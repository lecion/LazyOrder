package com.cisoft.shop.login.presenter;

import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.goods.model.ShopModel;
import com.cisoft.shop.login.view.ILoginView;

import org.json.JSONObject;
import org.kymjs.aframe.ui.ViewInject;

/**
 * Created by Lecion on 2/16/15.
 */
public class LoginPresenter {
    private ILoginView view;

    private ShopModel shopModel;

    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    /**
     * 普通商家登陆
     */
    public void normalLogin(String phone, String pwd) {
        shopModel.merLogin(phone, pwd, new ShopModel.ILoginListener() {
            @Override
            public void onSuccess(JSONObject data) {
                //登陆成功，保存商店信息
                Shop shop = new Shop(data);
            }

            @Override
            public void onFailure(String msg) {
                ViewInject.toast(msg);
            }
        });
    }

    /**
     * 快递商家登陆
     */
    public void expressLogin() {

    }



}
