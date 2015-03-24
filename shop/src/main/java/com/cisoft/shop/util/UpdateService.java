package com.cisoft.shop.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.cisoft.shop.Api;
import com.cisoft.shop.R;

import org.kymjs.aframe.ui.ViewInject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateService extends Service {
	private final int NOTIFICATION_DOWNLOAD = 1;
	private final int DOWNLOAD_COMPLETED = 1;
	private final int DOWNLOAD_FAILED = 2;
	private Notification notification = null;
	private NotificationCompat.Builder builder = null;
	private NotificationManager manager = null;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETED:
				notification.contentView.setTextViewText(R.id.tv_notification, "下载完成，点击安装");
				File apkfile = new File(FileHelper.getInstance().getApkDir() + File.separator + "cyxbs.apk");
		        if (!apkfile.exists()) {  
		            return;
		        }  
		        Intent i = new Intent(Intent.ACTION_VIEW);  
		        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
		        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, i, 0);
				notification.contentIntent = pendingIntent;
		        manager.notify(NOTIFICATION_DOWNLOAD, notification);
		        
				break;
			case DOWNLOAD_FAILED:
                ViewInject.toast("下载失败...");
				break;
			}
			stopSelf();
		};
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("下载地址", intent.getStringExtra(Api.KEY_SETTING_DOWNLOAD_URL));
		final String url = intent.getStringExtra(Api.KEY_SETTING_DOWNLOAD_URL);
		createNotification();
		new Thread(new Runnable() {
			@Override
			public void run() {
				downloadFile(url);
			}
			
			@SuppressWarnings("deprecation")
			public void downloadFile(String downloadUrl) {
				URL url = null;
				try {
					url = new URL(downloadUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					InputStream is = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(new File(FileHelper.getInstance().getApkDir() + File.separator + "cyxbs.apk"));
					int totalLength = conn.getContentLength();
					int readLength = 0;
					int totalReadLength = 0;
					int percent = 0;
					byte[] buffer = new byte[500];
					while ((readLength = is.read(buffer)) > 0) {
						fos.write(buffer, 0, readLength);
						totalReadLength += readLength;
						//Log.v("已下载", totalReadLength + "");
						percent = totalReadLength * 100 / totalLength;
						if (percent % 10 == 0) {
							notification.contentView.setProgressBar(R.id.pb_notification, 100, percent, false);
							notification.contentView.setTextViewText(R.id.tv_notification, "已下载" + percent + "%");
							manager.notify(NOTIFICATION_DOWNLOAD, notification);
						}
					}
					if (percent == 100) {
						handler.sendEmptyMessage(DOWNLOAD_COMPLETED);
					} else {
						handler.sendEmptyMessage(DOWNLOAD_FAILED);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(0);
	}
	
	private void createNotification() {
		manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(this);
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.view_notification_download);
		String appName = this.getResources().getString(R.string.app_name);
		int icon = R.drawable.ic_launcher;
		builder.setSmallIcon(icon);
		builder.setWhen(System.currentTimeMillis());
		contentView.setProgressBar(R.id.pb_notification, 100, 0, false);
		contentView.setTextViewText(R.id.tv_notification, "正在下载");
		builder.setContent(contentView);
		notification = builder.build();
		manager.notify(NOTIFICATION_DOWNLOAD, notification);
	}
}
