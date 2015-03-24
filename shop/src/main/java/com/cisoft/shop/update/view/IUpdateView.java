package com.cisoft.shop.update.view;

import com.cisoft.shop.bean.UpdateInfo;

/**
 * Created by Lecion on 3/24/15.
 */
public interface IUpdateView {
    void showUpdateProgress();

    void hideUpdateProgress();

    void showNoUpdate();

    void showUpdateInfo(UpdateInfo info);

    void showErrorUpdate();
}
