package com.cisoft.shop.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class UpdateManager {
	private static final int NOTIFICATION_UPDATE_INFO = 0;
	private static UpdateManager manager;
	private Context context;
	private UpdateManager(Context context) {
		this.context = context;
	}
	
	public static UpdateManager getInstance(Context context) {
		if (manager == null) {
			manager = new UpdateManager(context);
		}
		return manager;
	}
	
	/**
	 * 判断是否有新版本
	 * @return boolean 是否有更新，有更新为true
	 *
	 * */
//
//	private boolean hasNewVersion() {
//		int curCode = getCurVersionCode();
//		if (info.getVersionCode() > curCode) {
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 获取当前版本信息
	 * @return int 版本号versionCode
	 * */
	public int getCurVersionCode() {
		PackageInfo pkgInfo = null;
		try {
			pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pkgInfo.versionCode;
	}

//	public String getDownLoadUrl() {
//		return info.getUrl();
//	}
	
	/**
	 * 检查更新
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws org.xml.sax.SAXException
	 * @throws java.io.IOException
	 * */
	public boolean checkUpdate() throws ClientProtocolException, IOException, SAXException {
		//如果有新版本，就创建notification
//		if (hasNewVersion()) {
////			createNotification();
//			return true;
//		}
		return false;
	}

//	private void createNotification() {
//		String appName = context.getResources().getString(R.string.app_name);
//		String tickerText = appName + "有新版本啦，点我更新吧~";
//		String title = appName + "更新";
//		String content = "点击更新" + appName;
//		int icon = R.drawable.app_logo;
//		//启动下载的intent
//		Intent intent = new Intent(context, UpdateService.class);
//		intent.putExtra("url", info.getUrl());
//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
//		//新建notification
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		builder.setTicker(tickerText);
//		builder.setContentText(content);
//		builder.setContentTitle(title);
//		builder.setSmallIcon(icon);
//		builder.setWhen(System.currentTimeMillis());
//		builder.setContentIntent(pendingIntent);
//		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		manager.notify(NOTIFICATION_UPDATE_INFO, builder.build());
//	}
	

	
	
}
