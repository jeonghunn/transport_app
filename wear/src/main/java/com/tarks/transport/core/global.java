package com.tarks.transport.core;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tarks.transport.R;
import com.tarks.transport.db.InfoClass;
import com.tarks.transport.ui.RouteList;
import com.tarks.transport.ui.StationList;
import com.tarks.transport.ui.WayList;

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

    static boolean debug_mode = true;

    public static void toast(Context cx, String str, boolean length) {
        // Log.i("ACCESS", "I can access to toast");
       Toast.makeText(cx, str, (length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
    }


    public static void toast(Context cx, String str) {
        toast(cx, str, false);
    }


    public static void log(String str){
        if(debug_mode) Log.i("Log", str);
    }


    //Temp function
    public static void setNotifcation(Context cx, int notificationId, Intent viewIntent, String title, String content, int icon, int largeicon){

// Build intent for notification content
        // Intent viewIntent = new Intent(this, StationList.class);
        //  viewIntent.putExtra("1234", eventId);






        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                       .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon));

        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(cx, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(cx)
                        .setSmallIcon(icon)
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
    public static void BusNoti(Context cx, int notificationId, Intent viewIntent, String title, String content, String direction_name,  int country_srl, int route_srl, int way_srl, int station_srl ,  String station_summary, int icon, int largeicon){



        PendingIntent viewPendingIntent = PendingIntent.getActivity(cx, 0, viewIntent, 0);


        Intent stationIntent = new Intent(cx, StationList.class);
        stationIntent.putExtra("country_srl" , country_srl);
        stationIntent.putExtra("route_srl" , route_srl);
        stationIntent.putExtra("way_srl" , way_srl);
        stationIntent.putExtra("station_srl" , station_srl);

        PendingIntent pendingStationIntent = PendingIntent.getActivity(cx, 0, stationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent wayIntent = new Intent(cx, WayList.class);
        wayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingWayIntent = PendingIntent.getActivity(cx, 0, wayIntent, 0);

        Intent routeIntent = new Intent(cx, RouteList.class);
        routeIntent.putExtra("country_srl" , country_srl);
        PendingIntent pendingRouteIntent = PendingIntent.getActivity(cx, 0, routeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Delete
        Intent deleteIntent = new Intent(cx, ActionActivity.class);
        deleteIntent.putExtra("action_kind", "delete_noti");
        PendingIntent pendingDeleteIntent = PendingIntent.getActivity(cx, 0, deleteIntent, 0);


        // Create a big text style for the second page
        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle(direction_name + " " + cx.getString(R.string.direction) + "(" + route_srl  + ")")
                .bigText(station_summary);


// Create second page notification
        Notification secondPageNotification =
                new NotificationCompat.Builder(cx)
                        .setStyle(secondPageStyle)
                        .build();




        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                                //.setDisplayIntent(pendingdetailintent)
                        .addPage(secondPageNotification)
                        .setBackground(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon));


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(cx)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setDeleteIntent(pendingDeleteIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon))
                        .extend(wearableExtender)
                        .addAction(R.drawable.ic_directions_bus_white,
                                cx.getString(R.string.stations), pendingStationIntent)
                        .addAction(R.drawable.ic_place_white,
                                cx.getString(R.string.destination_setting), pendingRouteIntent);





// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(cx);



// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    //Temp function
    public static void WaitingBusNoti(Context cx, int notificationId, String title, String content, int icon, int largeicon){





        Intent rideintent = new Intent(cx, RouteList.class);
        PendingIntent ridePendingIntent = PendingIntent.getActivity(cx, 0, rideintent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Delete
        Intent deleteIntent = new Intent(cx, ActionActivity.class);
        deleteIntent.putExtra("action_kind", "delete_noti");
        PendingIntent pendingDeleteIntent = PendingIntent.getActivity(cx, 0, deleteIntent, 0);





        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                                //.setDisplayIntent(pendingdetailintent)
                        .setBackground(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon));


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(cx)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setDeleteIntent(pendingDeleteIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                cx.getResources(), largeicon))
                        .extend(wearableExtender)
                        .addAction(R.drawable.ic_ride_bus_white,
                                cx.getString(R.string.riding), ridePendingIntent);






// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(cx);



// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }



    public static String getStringbyBytes(byte[] bytes){
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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

    public static ArrayList<InfoClass> getJSONArrayListByInfoClass(String content) {
        ArrayList<InfoClass> yourArray = null;
        try {
            JSONArray array = new JSONArray(content);
            yourArray   = new Gson().fromJson(array.toString(), new TypeToken<List<InfoClass>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

return yourArray;
    }

    public static ArrayList<WayClass> getJSONArrayListByWayClass(String content) {
        ArrayList<WayClass> yourArray = null;
        try {
            JSONArray array = new JSONArray(content);
            yourArray   = new Gson().fromJson(array.toString(), new TypeToken<List<WayClass>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return yourArray;
    }


    public static ArrayList<String> getJSONArrayListByString(String content) {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list.add(jsonArray.get(i).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
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

    public static boolean getNight(){
        String time = new SimpleDateFormat("HH").format(new Date(System.currentTimeMillis()));
        int hour = Integer.parseInt(time);
        if((hour <= 6 && 0 <= hour) || (19 <= hour &&  hour <= 23)) return true;
        return false;
    }

    public static void Vibrate(Context cx, int time) {
        Vibrator v = (Vibrator) cx.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(time);
    }

    public static Bitmap drawTextToBitmap(Context gContext,
                                          int gResId,
                                          String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }



}
