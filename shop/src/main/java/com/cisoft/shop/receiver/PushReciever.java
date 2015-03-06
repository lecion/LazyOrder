package com.cisoft.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cisoft.shop.MyService;

public class PushReciever extends BroadcastReceiver {
    public PushReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        i.putExtras(intent.getExtras());
        context.startService(i);
    }
}
