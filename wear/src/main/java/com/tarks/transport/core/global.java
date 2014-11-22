package com.tarks.transport.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.tarks.transport.R;
import com.tarks.transport.StationList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JHRunning on 11/14/14.
 */
public final class global {

    static boolean debug_mode = true;

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


    //Temp function
    public static void setNotifcation(Context cx, int notificationId, Intent viewIntent, String title, String content, int largeicon){

// Build intent for notification content
        // Intent viewIntent = new Intent(this, StationList.class);
        //  viewIntent.putExtra("1234", eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(cx, 0, viewIntent, 0);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                     //   .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon));


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(cx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon))
                        .extend(wearableExtender)
                        .setContentIntent(viewPendingIntent);




// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(cx);



// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    //Temp function
    public static void cancelNoti(Context cx, int notificationId){

// Build intent for notification content
        // Intent viewIntent = new Intent(this, StationList.class);
        //  viewIntent.putExtra("1234", eventId);



// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(cx);


        notificationManager.cancel(notificationId);
    }


    public static Map getJSONArray(String content) {
        // JSONArray array = Global.jsonParserList(content);

        // JSONObject jsonRoot = null;

        try {

            JSONArray array = new JSONArray(content);
            Map<String, Object> mp = new HashMap<String, Object>();

            JSONObject jsonRoot = null;
            for (int i = 0; i < array.length(); i++) {
                jsonRoot = array.getJSONObject(i);
                mp.putAll(jsonToMap(jsonRoot, mp));
                //map.put(mp.get(0).toString(), mp.get(1).toString());
            }
            Log.i("Result",  mp.toString());
            return mp;
        } catch (Exception e) {
            Log.e("JSON Combine", ":::::array Error " + e.toString());
        }
        return null;

    }

    public static Map getJSON(JSONArray array) {
        // JSONArray array = Global.jsonParserList(content);

        // JSONObject jsonRoot = null;

        try {

            Map<String, Object> mp = new HashMap<String, Object>();

            JSONObject jsonRoot = null;
            for (int i = 0; i < array.length(); i++) {
                jsonRoot = array.getJSONObject(i);
                mp.putAll(jsonToMap(jsonRoot, mp));
                //map.put(mp.get(0).toString(), mp.get(1).toString());
            }
            Log.i("Result",  mp.toString());
            return mp;
        } catch (Exception e) {
            Log.e("JSON Combine", ":::::array Error " + e.toString());
        }
        return null;

    }

    public static Map jsonToMap(JSONObject json, Map retMap) throws JSONException {
//		Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }


    public static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


    public static void Vibrate(Context cx, int time) {
        Vibrator v = (Vibrator) cx.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(time);
    }


}
