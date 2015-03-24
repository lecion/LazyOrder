package com.cisoft.shop.update.presenter;

import android.content.Context;

import com.cisoft.shop.update.model.UpdateModel;
import com.cisoft.shop.update.view.IUpdateView;

/**
 * Created by Lecion on 3/24/15.
 */
public class UpdatePresenter {

    private IUpdateView view;
    private UpdateModel model;

    public UpdatePresenter(Context context, IUpdateView view) {
        this.view = view;
        //TODO modulename
        model = new UpdateModel(context, "");
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {

    }
}
