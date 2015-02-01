package com.cisoft.lazyorder.core.about;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.about.AboutUsFragment;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.SystemTool;

import java.io.File;

/**
 * Created by comet on 2014/12/2.
 */
public class UpdateService extends Service implements Handler.Callback{

    /* Intent传递Extra所使用的KEY */
    public static final String VERSION_NAME = "versionName";
    public static final String DOWNLOAD_URL = "downloadUrl";

    /* Intent传递的Extra */
    private String downloadUrl;
    private String versionName;

    /* Notification的相关属性 */
    private NotificationManager notifyManager;
    private Notification notification;
    private RemoteViews views;
    private static final int NOTIFICATION_Id = 1234;

    /* 不同状态下的handler处理标记 */
    private Handler myHandler;
    private static final int WHAT_INIT_STATE_BAR = 100;
    private static final int WHAT_INIT_FILE_COUNT = 200;
    private static final int WHAT_DOWNLOADING = 300;
    private static final int WHAT_DOWNLOAD_FINISH = 400;
    private static final int WHAT_DOWNLOAD_FAILURE = 500;

    /* 下载文件大小标记（单位：bit） */
    private long lastCount = 0; //上次更新下载进度时已下载的文件大小
    private long currentCount = 0;  //当前已下载的文件大小
    private long apkCount = 0;  //所需要下载的文件的大小

    private boolean flag = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent,int startId){
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        /* 获取Intent传递过来的更新的相关数据 */
        versionName = intent.getStringExtra(VERSION_NAME);
        downloadUrl = intent.getStringExtra(DOWNLOAD_URL);

        /* 初始化Notification */
        notifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = android.R.drawable.stat_sys_download;
        notification.tickerText = getString(R.string.app_name) + "正在更新";
        notification.when = System.currentTimeMillis();
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        views = new RemoteViews(getPackageName(), R.layout.state_update_download);
        notification.contentView = views;
        notification.contentIntent = PendingIntent.getBroadcast(this, 0, new Intent(AboutUsFragment.BR_CLRER_STATE_BAR), 0);
        notifyManager.notify(NOTIFICATION_Id, notification);

        //初始化下载任务内容views
        myHandler = new Handler(this);
        Message message = myHandler.obtainMessage(WHAT_INIT_STATE_BAR);
        myHandler.sendMessage(message);

        //启动线程开始执行下载任务
        downFile(downloadUrl);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy(){
        notifyManager.cancel(NOTIFICATION_Id);
        flag = false;

        super.onDestroy();
    }


    /**
     * 下载更新文件
     */
    private void downFile( String url) {
        KJHttp kjHttp = new KJHttp();

        kjHttp.download(url, FileUtils.getSaveFile("CISoft/LazyOrder", versionName + ".apk"), new HttpCallBack() {
            @Override
            public void onSuccess(File f) {
                Message message = myHandler.obtainMessage(WHAT_DOWNLOAD_FINISH, f);
                myHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Message message = myHandler.obtainMessage(WHAT_DOWNLOAD_FAILURE);
                myHandler.sendMessage(message);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);

                if (apkCount == 0) {
                    apkCount = count;
                    Message message = myHandler.obtainMessage(WHAT_INIT_FILE_COUNT);
                    myHandler.sendMessage(message);
                } else {
                    if(!flag) return;

                    //每下载完0.01MB(10KB),更新下进度
                    if (current - lastCount > 1024 * 10) {
                        Log.i("wanghong", "downloading");

                        currentCount = current;
                        Message message = myHandler.obtainMessage(WHAT_DOWNLOADING);
                        myHandler.sendMessage(message);

                        lastCount = current;
                    }
                }
            }
        });
    }


    /**
     * 安装下载后的apk文件
     */
    private void Instanll(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


    /**
     * 处理更新的几种状态
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {

        if(msg != null){
            String apkCountMB = "";

            switch(msg.what){
                case WHAT_INIT_STATE_BAR:
                    views.setImageViewBitmap(R.id.iv_logo,
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    views.setTextViewText(R.id.tv_point_process, "已下载0%");
                    views.setTextViewText(R.id.tv_count_process, "0MB/未知");
                    views.setTextViewText(R.id.tv_when, SystemTool.getDataTime());
                    notification.contentView = views;
                    notifyManager.notify(NOTIFICATION_Id, notification);

                    break;
                case WHAT_INIT_FILE_COUNT:
                    apkCountMB = String.format("%.2fMB", (double)apkCount / 1024 /1024) + "MB";
                    views.setTextViewText(R.id.tv_count_process, "0MB/" + apkCountMB);
                    notification.contentView = views;
                    notifyManager.notify(NOTIFICATION_Id, notification);

                    break;
                case WHAT_DOWNLOADING:
                    int downloadPrecent = (int)(((double)currentCount / apkCount) * 100);
                    //将bit单位转化为MB单位
                    String currentCountMB = String.format("%.2fMB", (double)currentCount / 1024 /1024) + "MB";

                    views.setTextViewText(R.id.tv_point_process, "已下载" + downloadPrecent + "%");
                    views.setTextViewText(R.id.tv_count_process, currentCountMB + "/" + apkCountMB);
                    notification.contentView = views;
                    notifyManager.notify(NOTIFICATION_Id, notification);

                    break;
                case WHAT_DOWNLOAD_FAILURE:
                    notifyManager.cancel(NOTIFICATION_Id);

                    stopSelf();
                    break;
                case WHAT_DOWNLOAD_FINISH:
                    //下载完成后清除所有下载信息，执行安装提示
                    apkCount = 0;
                    currentCount = 0;
                    lastCount = 0;
                    notifyManager.cancel(NOTIFICATION_Id);

                    Instanll((File)msg.obj);
                    stopSelf();
                    break;
            }
        }

        return true;
    }

}
