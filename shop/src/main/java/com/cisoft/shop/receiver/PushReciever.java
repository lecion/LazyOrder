package com.cisoft.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushReciever extends BroadcastReceiver {
    public PushReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("onReceive", "PushReceiver:");
        Intent i = new Intent();
        i.setAction("com.cisoft.shop.receivemsg");
        i.putExtras(intent.getExtras());
        context.sendBroadcast(i);
    }
}
