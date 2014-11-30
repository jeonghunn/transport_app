package com.tarks.transport;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.tarks.transport.core.InfoClass;
import com.tarks.transport.core.WakeLock;
import com.tarks.transport.core.global;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.tarks.transport.core.WakeLock.acquireCpuWakeLock;

public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
     //   showToast(messageEvent.getPath());
        global.log(messageEvent.getPath());
checkMessage(messageEvent.getPath(), messageEvent.getData());


    }
    private void checkMessage(String message, byte[] bytes){
        String[] array = message.split("//");
        String action_kind = array[0];
        String data = array[1];

        if(action_kind.matches("notification")) actionNoti(data);


    }

    private void actionNoti(String data) {
        //ID
        // 1 : transport, arrived, next stop

        global.log("Actionnoti");
        Map<String, String> resultmap = null;
        resultmap = global.getJSONArray(data);

        int kind = Integer.parseInt(String.valueOf(resultmap.get("kind")));
        int noti_id = Integer.parseInt(String.valueOf(resultmap.get("noti_id")));
        String title = String.valueOf(resultmap.get("title"));
        String content = String.valueOf(resultmap.get("content"));

        //Normal noti
        if (kind == 1) {
            Intent viewIntent = new Intent(this, StationList.class);
            global.setNotifcation(this, noti_id, viewIntent, title, content,R.drawable.ic_launcher,  R.drawable.bus_background);
        }

        //Bus almost arrived
        if (kind == 2) {

            global.Vibrate(this, 2000);
            Intent viewIntent = new Intent(this, StationList.class);
            global.setNotifcation(this, noti_id, viewIntent, title, content, R.drawable.ic_launcher, R.drawable.almost_background);
//
//            Intent i = new Intent(ListenerService.this, BusArrive.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra("title", title);
//            i.putExtra("content", content);
//            startActivity(i);

        }


        //Bus arrived
        if (kind == 3) {
         //   global.Vibrate(this, 7000);
//            Intent viewIntent = new Intent(this, StationList.class);
//            global.setActiveNoti(this, noti_id, viewIntent, title, content, R.drawable.ic_launcher, R.drawable.flag);

            arrivedAction(title, content);
        }

    }


private void arrivedAction(String title, String content){
   // WakeLock.acquireCpuWakeLock(ListenerService.this);



                Intent i = new Intent(this, BusArrive.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("title", title);
            i.putExtra("content", content);
            startActivity(i);
    global.Vibrate(this, 4000);
   // WakeLock.releaseCpuLock();
    Intent viewIntent = new Intent(this, StationList.class);
    global.setActiveNoti(this, 1, viewIntent, title, content, R.drawable.ic_launcher, R.drawable.flag);
}



    private void showToast(String message)  {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

//        Intent viewIntent = new Intent(this, StationList.class);
//        global.setNotifcation(this, 1, viewIntent, "가나다", "가나다", R.drawable.bus_background);



}

}