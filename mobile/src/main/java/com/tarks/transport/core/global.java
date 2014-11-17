package com.tarks.transport.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.tarks.transport.R;
import com.tarks.transport.db.InfoClass;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by JHRunning on 11/14/14.
 */
public final class global {

    public static boolean debug_mode = true;

    public static void toast(String str, boolean length) {
        // Log.i("ACCESS", "I can access to toast");
//        Toast.makeText(mod, str,
//                (length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show
    }


    public static void toast(String str) {
        toast(str, false);
    }


    public static void log(String str){
        if(debug_mode) Log.i("Log", str);
    }


    public static void dumpArray(String[] array) {
        for (int i = 0; i < array.length; i++)
            System.out.format("array[%d] = %s%n", i, array[i]);
    }

    //Temp function
    public static void setNotifcation(Context cx, int notificationId, Intent viewIntent, String title, String content, int largeicon){

// Build intent for notification content
        // Intent viewIntent = new Intent(this, StationList.class);
        //  viewIntent.putExtra("1234", eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(cx, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(cx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon))
                        .setContentIntent(viewPendingIntent);



// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(cx);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    public static byte[] convertToBytes(ArrayList<InfoClass> strings) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (InfoClass element : strings) {
            out.writeUTF(String.valueOf(element));
        }
        byte[] bytes = baos.toByteArray();
Log.i("asdf", bytes.toString());
        return bytes;
    }
}
