package com.cisoft.lazyorder.core.about;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.cisoft.lazyorder.AppConfig;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.util.Utility;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.utils.FileUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by comet on 2014/12/2.
 */
public class AppDownloadService extends Service{

    /* Intent传递Extra所使用的KEY */
    public static final String VERSION_NAME = "versionName";
    public static final String DOWNLOAD_URL = "downloadUrl";

    /* Intent传递的Extra */
    private String downloadUrl;
    private String versionName;

    /* Notification的相关属性 */
    private NotificationManager notifyManager;
    private NotificationCompat.Builder builder;
    private static final int NOTIFICATION_Id = 1234;
    private static final int DOWN_COMPLETE = 400;
    private static final int DOWN_EXCEPTION = 500;

    private File apkFile;
    private long lastUpdateTime;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case DOWN_EXCEPTION:
                    updateNotification(getString(R.string.app_name),
                            getString(R.string.notification_download_error), new Intent());
                    break;
                case DOWN_COMPLETE:
                    updateNotification(getString(R.string.app_name),
                            getString(R.string.notification_success_download), new Intent());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + apkFile.getPath()), "application/vnd.android.package-archive");
                    startActivity(intent);

                    break;
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        versionName = intent.getStringExtra(VERSION_NAME);
        downloadUrl = intent.getStringExtra(DOWNLOAD_URL);

        apkFile = FileUtils.getSaveFile(AppConfig.FILE_DOWNLOAD_PATH, filterFileName(versionName + ".apk"));
        downFile(downloadUrl, apkFile);

        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.notification_app_downloading));
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setProgress(0, 0, true);
        builder.setOngoing(true);
        notifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(NOTIFICATION_Id, builder.build());

        return Service.START_REDELIVER_INTENT;
    }


    private void updateNotification(String contentTitle, String contentText, Intent intent) {
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setTicker(contentText);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        notifyManager.notify(NOTIFICATION_Id, builder.build());
    }



    /**
     * 下载更新文件
     */
    private void downFile(String downloadUrl, File apkFile) {
        KJHttp kjHttp = new KJHttp();

        kjHttp.download(downloadUrl, apkFile, new HttpCallBack() {
            @Override
            public void onSuccess(File f) {
                builder.setProgress(0, 0, false);
                mHandler.sendEmptyMessage(DOWN_COMPLETE);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                builder.setProgress(0, 0, false);
                mHandler.sendEmptyMessage(DOWN_EXCEPTION);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);

                // 下载中每1秒更新1次进度
                if (System.currentTimeMillis() - lastUpdateTime > 1000l) {
                    int currProgress = (int)(100l * current / count);
                    builder.setProgress(100, currProgress, false);
                    builder.setContentText(currProgress + "%");
                    notifyManager.notify(NOTIFICATION_Id, builder.build());
                    lastUpdateTime = System.currentTimeMillis();
                }
            }
        });
    }


    private String filterFileName(String str) {
        String dest = str;
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n|.");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }
}
