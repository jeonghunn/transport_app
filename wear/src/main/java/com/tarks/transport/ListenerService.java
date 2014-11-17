package com.tarks.transport;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.tarks.transport.core.global;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast(messageEvent.getPath(), messageEvent.getData());

    }

    private void showToast(String message, byte[] bytenear) {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        global.log(message);
//        Intent viewIntent = new Intent(this, StationList.class);
//        global.setNotifcation(this, 1, viewIntent, "가나다", "가나다", R.drawable.bus_background);

    }




}