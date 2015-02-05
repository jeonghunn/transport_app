package com.tarks.transport.core.global;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.tarks.transport.R;
import com.tarks.transport.core.db.InfoClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JHRunning on 11/14/14.
 */
public final class global {

    public static boolean debug_mode = true;


    public static SharedPreferences sp_dev;

    public static void toast(String str, boolean length) {
        // Log.i("ACCESS", "I can access to toast");
//        Toast.makeText(mod, str,
//                (length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show
    }
    public static boolean getBooleanDev(Context ct,String str){
        if(sp_dev == null){
            sp_dev = getSP(ct,"dev");
        }
        return sp_dev.getBoolean(str,false);
    }
    public static void setBooleanDev(Context ct,String str,boolean bl){
        if(sp_dev == null){
            sp_dev = getSP(ct,"dev");
        }
        sp_dev.edit().putBoolean(str,bl).commit();
        sp_dev = getSP(ct,"dev");
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

    public static String arraylistStringtoString(ArrayList<String> data){
        String listString = "";

        for (int i = 0; i < data.size(); i++)
        {
            listString += data.get(i) ;
            if(i < data.size() - 1) listString += ", ";
        }
        return listString;
    }

    public static byte[] objectToBytArray( Object ob ){
        return ((ob.toString()).getBytes());
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

    // Default Connection Error
    public static void ConnectionError(Context cx) {
        if (InternetConnection(cx, 1) == true || InternetConnection(cx, 0) == true) {
            Infoalert(cx, cx.getString(R.string.error),
                    cx.getString(R.string.error_des) ,
                    cx.getString(R.string.yes));

        } else {
            Infoalert(cx, cx.getString(R.string.networkerror),
                    cx.getString(R.string.networkerrord),
                    cx.getString(R.string.yes));

        }
        //
        // if (InternetConnection(1) == true || InternetConnection(0) == true) {
        // toast(mod.getString(R.string.error_des), false);
        // }else{
        // toast(mod.getString(R.string.networkerrord), false);
        // }
    }


    // Show Information alert
    public static void Infoalert(Context context, String title, String message,
                                 String button) {

        try {

            if (globalv.alert_status == true) {
                globalv.alert_status = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(message).setPositiveButton(button, null)
                        .setTitle(title);

                // Check OS
                if (Build.VERSION.SDK_INT >= 17) {
                    // Dialog Dismiss시 Event 받기
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            globalv.alert_status = true;
                        }
                    });

                } else {
                    globalv.alert_status = true;
                }

                builder.show();

            }

        } catch (Exception e) {
            globalv.alert_status = true;
        }
    }

    public static boolean InternetConnection(Context cx, int network) {
        ConnectivityManager cm = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni;
        boolean connect;
        if (network == 1) {
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            // boolean isWifiAvail = ni.isAvailable();
            connect = ni.isConnected();
        } else {
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // boolean isMobileAvail = ni.isAvailable();
            connect = ni.isConnected();
        }
        return connect;
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

    public static long getCurrentTimeStamp() {

        return System.currentTimeMillis() / 1000;
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
    public static String getSetting(Context cx, String setting, String default_value) {
        SharedPreferences prefs = cx.getSharedPreferences("setting",
                cx.MODE_PRIVATE);
        return prefs.getString(setting, default_value);
    }
    public static SharedPreferences getSP(Context cx, String name) {
        SharedPreferences prefs = cx.getSharedPreferences("setting",
                cx.MODE_PRIVATE);
        return prefs;
    }

    public static void CountSrlUpdate(Context cx) {
        int count_srl = Integer.parseInt(getSetting(cx, "count_srl", "1"));
        SaveSetting(cx, "count_srl", String.valueOf(count_srl + 1));
    }

    public static void DBCountSrlUpdate(Context cx) {
        int count_srl = Integer.parseInt(getSetting(cx, "count_srl", "1"));
        SaveSetting(cx, "db_count_srl", String.valueOf(count_srl));
    }

    public static int getDBCountSrl(Context cx){
        return Integer.parseInt(getSetting(cx, "db_count_srl", "0"));
    }

    public static int getCountSrl(Context cx){
        return Integer.parseInt(getSetting(cx, "count_srl", "1"));
    }

    public static int getLocationMode(Context cx){
        return Integer.parseInt(getSetting(cx, "location_mode", "0"));
    }
    public static void setLocationMode(Context cx, int location_mode) {
        int count_srl = Integer.parseInt(getSetting(cx, "location_mode", "0"));
        SaveSetting(cx, "location_mode", String.valueOf(location_mode));
    }

    public static int getGoalID(Context cx){
        return Integer.parseInt(getSetting(cx, "goal_id", "0"));
    }
    public static void setGoalID(Context cx, int goal_id) {
        int count_srl = Integer.parseInt(getSetting(cx, "goal_id", "0"));
        SaveSetting(cx, "goal_id", String.valueOf(goal_id));
    }
//
//    public static void SamePlaceCountUpdate(Context cx) {
//        int same_place_count = Integer.parseInt(getSetting(cx, "same_place_count", "0"));
//        SaveSetting(cx, "same_place_count", String.valueOf(same_place_count + 1));
//        log("asdf" + same_place_count);
//
//    }

//    public static void resetSamePlaceCount(Context cx) {
//        SaveSetting(cx, "same_place_count", String.valueOf(0));
//    }
//    public static int getSamePlaceCount(Context cx){
//        return Integer.parseInt(getSetting(cx, "same_place_count", "0"));
//    }



    public static void SaveSetting(Context cx, String setting, String value){
        SharedPreferences edit = cx.getSharedPreferences("setting",
                cx.MODE_PRIVATE);
        SharedPreferences.Editor editor = edit.edit();
        editor.putString(setting , value);
        editor.commit();
    }

    public static String getStringbyBytes(byte[] bytes){
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getNight(){
       String time = new SimpleDateFormat("HH").format(new Date(System.currentTimeMillis()));
        int hour = Integer.parseInt(time);
       if(hour <= 6 && 0 <= hour) return true;
        return false;
    }

    public static void setActiveNoti(Context cx, int notificationId, Intent viewIntent, String title, String content, int icon, int largeicon){

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
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(5)
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




}
