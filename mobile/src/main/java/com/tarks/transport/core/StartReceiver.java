package com.tarks.transport.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tarks.transport.MainActivity;

/**
 * Created by JHRunning on 11/21/14.
 */
public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("android.intent.action.BOOT_COMPLETED")) {

            context.startService(new Intent(context, CoreSystem.class));


        }
    }
}