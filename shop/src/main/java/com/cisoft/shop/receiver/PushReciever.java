package com.cisoft.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

public class PushReciever extends BroadcastReceiver {
    public PushReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Log.d("onReceive", data.getInt("action") + "");
        switch (data.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                byte[] payload = data.getByteArray("payload");
                if (payload != null) {
                    String rs = new String(payload);
                    Log.d("GET_MESSAGE", rs);
                }
                break;
            case PushConsts.GET_CLIENTID:
                String clientId = data.getString("clientid");
                Log.d("GET_CLIENTID", clientId);
                break;
        }
    }
}
