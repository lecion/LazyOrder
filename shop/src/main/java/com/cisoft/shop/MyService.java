package com.cisoft.shop;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent i = new Intent();
        i.setAction("com.cisoft.receivemsg");
        i.putExtras(intent.getExtras());
        sendBroadcast(i);
        return super.onStartCommand(intent, flags, startId);
    }
}
