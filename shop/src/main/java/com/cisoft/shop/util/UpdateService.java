package com.cisoft.shop.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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
	private Notification notification = null;
	private NotificationCompat.Builder builder = null;
	private NotificationManager manager = null;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String url = intent.getStringExtra(Api.KEY_SETTING_DOWNLOAD_URL);
        download(url);
		return super.onStartCommand(intent, flags, startId);
	}

    private void download(String url) {
        DownloadTask task = new DownloadTask();
        task.execute(url);
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

    private class DownloadTask extends AsyncTask<String,  Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            createNotification();
            ViewInject.toast("已进入后台开始下载...");
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return downloadFile(params[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int percent = values[0];
            notification.contentView.setProgressBar(R.id.pb_notification, 100, percent, false);
            notification.contentView.setTextViewText(R.id.tv_notification, "已下载" + percent + "%");
            manager.notify(NOTIFICATION_DOWNLOAD, notification);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                File apkfile = new File(FileHelper.getInstance().getApkDir() + File.separator + "lazyorder_shop.apk");
                if (!apkfile.exists()) {
                    ViewInject.toast("下载出现异常，请重试！");
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                startActivity(i);
            } else {
                ViewInject.toast("下载失败，请重试！");
            }
            super.onPostExecute(success);
        }

        private boolean downloadFile(String downloadUrl) {
            URL url = null;
            try {
                url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(new File(FileHelper.getInstance().getApkDir() + File.separator + "lazyorder_shop.apk"));
                int totalLength = conn.getContentLength();
                int readLength = 0;
                int totalReadLength = 0;
                int percent = 0;
                byte[] buffer = new byte[500];
                while ((readLength = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readLength);
                    totalReadLength += readLength;
                    percent = totalReadLength * 100 / totalLength;
                    if (percent % 10 == 0) {
                        publishProgress(percent);
                    }
                }
                if (percent == 100) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
