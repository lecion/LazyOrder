package com.cisoft.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cisoft.shop.MyService;

public class PushReciever extends BroadcastReceiver {
    public PushReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive", "PushReceiver:");
        Intent i = new Intent(context, MyService.class);
        i.putExtras(intent.getExtras());
        context.startService(i);
    }
}
