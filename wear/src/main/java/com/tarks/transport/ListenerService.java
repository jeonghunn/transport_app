package com.tarks.transport;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast(messageEvent.getPath());
    }

    private void showToast(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }


    //Temp function
    private void noti(){

        int notificationId = 001;
// Build intent for notification content
        Intent viewIntent = new Intent(this, StationList.class);
        //  viewIntent.putExtra("1234", eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("천안역")
                        .setContentText("다음 역 : 갈산역")
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.drawable.bus_background))
                        .setContentIntent(viewPendingIntent);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}