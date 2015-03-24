package com.cisoft.shop.update.presenter;

import android.content.Context;

import com.cisoft.shop.bean.UpdateInfo;
import com.cisoft.shop.update.model.UpdateModel;
import com.cisoft.shop.update.view.IUpdateView;
import com.cisoft.shop.util.UpdateManager;

/**
 * Created by Lecion on 3/24/15.
 */
public class UpdatePresenter {

    private IUpdateView view;
    private UpdateModel model;
    private Context context;

    public UpdatePresenter(Context context, IUpdateView view) {
        this.view = view;
        //TODO modulename
        model = new UpdateModel(context);
        this.context = context;
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        model.checkUpdate(new UpdateModel.UpdateLisnter() {
            @Override
            public void onSuccess(UpdateInfo info) {
                //有新版本
                if (UpdateManager.getInstance(context, info).hasNewVersion()) {
                    view.showUpdateInfo(info);
                } else {
                    view.showNoUpdate();
                }
            }

            @Override
            public void onFailure(String msg) {
                view.showErrorUpdate();
            }
        });
    }

    /**
     * 启动服务进行下载
     * @param info
     */
    public void startUpdate(UpdateInfo info) {
        UpdateManager.getInstance(context, info).startUpdate();
    }

}
