package com.cisoft.shop.util;

import android.content.Context;
import android.content.Intent;

import com.cisoft.shop.Api;
import com.cisoft.shop.bean.UpdateInfo;

public class UpdateManager {
	private static final int NOTIFICATION_UPDATE_INFO = 0;
	private static UpdateManager manager;
    private final UpdateInfo info;
    private Context context;
	private UpdateManager(Context context, UpdateInfo info) {
		this.context = context;
        this.info = info;
	}
	
	public static UpdateManager getInstance(Context context, UpdateInfo info) {
		if (manager == null) {
			manager = new UpdateManager(context, info);
		}
		return manager;
	}
	
	/**
	 * 判断是否有新版本
	 * @return boolean 是否有更新，有更新为true
	 *
	 * */

	public boolean hasNewVersion() {
		int curCode = getCurVersionCode();
		if (info.getVersionCode() > curCode) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当前版本信息
	 * @return int 版本号versionCode
	 * */
	public int getCurVersionCode() {
		return DeviceUtil.getVersionCode(context);
	}

    /**
     * 获取下载地址
     * @return
     */
	public String getDownLoadUrl() {
		return info.getUrl();
	}

    public void startUpdate() {
        Intent intent = new Intent(context, UpdateService.class);
		intent.putExtra(Api.KEY_SETTING_DOWNLOAD_URL, getDownLoadUrl());
        context.startService(intent);
    }

}
