package com.cisoft.shop;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("MyService", "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand");
        Intent i = new Intent();
        i.setAction("com.cisoft.receivemsg");
        i.putExtras(intent.getExtras());
        sendBroadcast(i);
        return super.onStartCommand(intent, flags, startId);
    }
}
