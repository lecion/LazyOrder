package com.cisoft.shop.login.view;

/**
 * Created by Lecion on 2/16/15.
 */
public interface ILoginView {
    void showLoginProgress();

    void hideLoginProgress();

    void showLoginFailed(String msg);

    void showWrongInput(int... ids);

    void skipToMainActivity();
}
