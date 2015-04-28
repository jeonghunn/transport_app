package com.tarks.transport.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tarks.transport.core.global.globalv;


/**
 * Created by JHRunning on 4/20/15.
 */
public class DevReciever extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Hi", "HIHI");
        sendBroadcast(new Intent(globalv.broadcast_CLICK));
        finish();


    }
}
