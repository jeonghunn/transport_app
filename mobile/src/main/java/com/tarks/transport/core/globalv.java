package com.tarks.transport.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.tarks.transport.R;

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
public final class globalv {


    //Location

    public static final int HIBERNATION_MODE = 1;
    public static final int POWER_SAVED_MODE = 2;
    public static final int STANBY_MODE = 3;
    public static final int ACTIVE_STANBY_MODE= 4;
    public static final int ACTIVE_MODE = 5;
    public static final int LIVE_ACTIVE_MODE = 6;


    //Notification
    public static final int DEFUALT_NOTI = 1;
    public static final int ALMOST_NOTI = 2;
    public static final int ARRIVED_NOTI = 3;
    public static final int WAITING_BUS_NOTI = 4;


    //gyro
    public static int moving_now = 0;
    //gyro int
    public static final int STOP_STATE = 1;
    public static final int ACTIVE_STATE = 2;

    }

