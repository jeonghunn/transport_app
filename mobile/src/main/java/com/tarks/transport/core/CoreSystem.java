package com.tarks.transport.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tarks.transport.R;
import com.tarks.transport.core.global.global;
import com.tarks.transport.core.global.globalv;
import com.tarks.transport.core.db.DbOpenHelper;
import com.tarks.transport.core.db.InfoClass;
import com.tarks.transport.core.db.flowclass;
import com.tarks.transport.main;
import com.tarks.transport.ui.DevReciever;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by JHRunning on 11/20/14.
 */
public class CoreSystem extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private String nodeId;
    private Context cx;
    private int location_mode;
    private boolean initcheck = false;
    private boolean waitingbus = true;
    private int action_count = 0;
    private int same_place_count = 0;
    private int same_place_id = 0;

    private int last_station_id = 0;
    private int last_nearby_sration_id = 0;
    private boolean same_place = false;

    //dev
    private boolean useFg = false;
    private boolean useBt = false;
    private boolean runningFg;
    private BusActiveReceiver devReceiver;


    private ArrayList<String> routes = new ArrayList<String>();
    ;

    SensorListener sl;
    private Intent intent;

    final Messenger mMessenger = new Messenger(new IncomingHandler());


    @Override
    public void onCreate() {
        super.onCreate();


        global.log("HI service start!");
        cx = CoreSystem.this;
        initCore(CoreSystem.this);
//
//        ArrayList<InfoClass> stations = getStations(1, 1, 1);
//        Gson gson = new GsonBuilder().create();
//        JsonArray json_result = gson.toJsonTree(stations).getAsJsonArray();
//        global.log(json_result.toString());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hiddenDev();
        return super.onStartCommand(intent, flags, startId);
    }

    public void initCore(Context cx) {
        setGoogleApiClient(cx);
    }

    public void startFlow(Context cx, Location lc) {


        // if(global.getDBCountSrl(cx) < global.getCountSrl(cx)){
        firstFlow(cx, lc);

        //  conFlow(cx, lc);

    }


    public void firstFlow(Context cx, Location lc) {
        global.log("firstFlow");
        global.DBCountSrlUpdate(cx);

//dbver check
        sendMessageDefault("db_ver",  global.getSetting(this, "db_ver", "0"));



        if (global.getLocationMode(cx) > globalv.HIBERNATION_MODE) {
            setActionLocationMode(cx, global.getLocationMode(cx));
        } else {


            int near_level = 0;


            for (int i = 1; i <= 6; i++) {
                if (getNearStations(cx, lc, i).size() > 0) {
                    near_level = i;
                    break;
                }

                if (i == 6) near_level = 7;
            }


            if (near_level == 1) setActionLocationMode(cx, globalv.ACTIVE_MODE);
            if (near_level == 2) setActionLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
            if (near_level == 3) setActionLocationMode(cx, globalv.STANBY_MODE);
            if (near_level == 4) setActionLocationMode(cx, globalv.POWER_SAVED_MODE);
            if (near_level == 5) setActionLocationMode(cx, globalv.HIBERNATION_MODE);
            if (near_level >= 6) setActionLocationMode(cx, globalv.HIBERNATION_MODE);



        }
        initcheck = true;
    }

    public void conFlow(Context cx, Location lc) {
        global.log("conFlow");
        action_count++;




        int near_level = 0;
        final int count_srl = global.getCountSrl(cx);
        final int goal_id = global.getGoalID(cx);

        ArrayList<InfoClass> ic = null;
        for (int i = 1; i <= 6; i++) {
            ic = getNearStations(cx, lc, i);
            if (ic.size() > 0) {
                near_level = i;
                break;
            }

            if (i == 6) near_level = 7;
        }

        //Make Array of  near routes
        for (int i = 1; i <= ic.size(); i++) {
            if (!routes.contains(String.valueOf(ic.get(i - 1).route)))
                routes.add(String.valueOf(ic.get(i - 1).route));
        }
        try {
            DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
            mDbOpenHelper.open();

            if (near_level < 3) {
                for (int i = 0; i < ic.size(); i++) {
                    //Check it has a destination

                    //No Destination
                    global.log("ok" + ic.size());
                    InfoClass get = ic.get(i);
                    mDbOpenHelper.insertFdColumn(count_srl, action_count, get.id, get.country_srl, get.route, get.station_srl, get.way_srl, lc.getLatitude(), lc.getLongitude(), get.station_latitude, get.station_longitude, global.getCurrentTimeStamp(), location_mode, near_level);
                    //     if (goal_id == get.id)

                }
            } else {

            }

            mDbOpenHelper.close();

            flowclass flowget = null;
            ArrayList<flowclass> mflow = selectflowStation(cx, count_srl);
            if (mflow.size() > 0 && mflow != null) flowget = mflow.get(0);
            ArrayList<InfoClass> stations = null;
            //   if(mflow.size() > 0){
            int station_left = 0;
            String stationListString = null;

            if (mflow.size() > 0)
                stations = getStations(flowget.country_srl, flowget.route, flowget.way_srl);
            if (mflow.size() > 0 && stations.size() > 0) {
                for (int i = 0; i < stations.size(); i++) {
                    //Calculate stations left
                    if (stations.get(i).id == goal_id && goal_id != 0)
                        station_left = stations.get(i).station_srl - flowget.station_srl;

                    // Make Stations List to show wear
                    if (flowget.station_srl <= stations.get(i).station_srl)
                        stationListString = stationListString == null ? getString(R.string.currunt) + "  : " + stations.get(i).station_name : stationListString + "\n\n" + stations.get(i).station_name;

                }
            }
            //Check same place
//        if(dbid == ic.get(0).id && sis == ic.size()){
//            same_place_count++;
//        }else{
//            dbid = ic.get(0).id;
//            sis = ic.size();
//            plm = location_mode;
//            same_place_count = 0;
//        }


            //Check nearest station
            if (ic.size() > 0 && last_nearby_sration_id != ic.get(0).id) {
                last_nearby_sration_id = ic.get(0).id;
                same_place = false;
            } else {
                same_place = true;
            }

            global.log("same_place : " + same_place_count);
            //   global.log("id : " + dbid);
            global.log("getlolevel : " + near_level);
            //  global.log(global.getNight() + "asdf");
            //  sendNoti(1,1,ic.get(0).station_name,String.valueOf(location_mode));

            //Check same place from previous history


        //    global.log(stations.size() + "LOCATIONMODE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            if ((mflow.size() > 0 && stations.size() > 0) && (last_station_id != flowget.id_srl)) {


                last_station_id = flowget.id_srl;

                //  global.log(mflow.get(0).station_srl + "/" + stations.size());


                //Next Station noti
                String next_name = mflow.get(0).station_srl < stations.size() ? "▶ " + stations.get(mflow.get(0).station_srl).station_name : " : " + getString(R.string.terminal);
                String direction_name = stations.get(stations.size() - 1).station_name;

                if (global.getGoalID(cx) == 0) {

                    //Checking waiting bus
                    if (waitingbus || action_count < 2 || location_mode < globalv.ACTIVE_MODE) {
                          //Check it is acitve mode
                        if(location_mode >= globalv.ACTIVE_MODE){
                            sendBusNoti(globalv.DEFUALT_NOTI, 1, getString(R.string.app_name), getString(R.string.bus_mode_started), direction_name,getString(R.string.nearby_bus_routes) + "\n\n" +  global.arraylistStringtoString(routes) ,  flowget.country_srl, flowget.route , flowget.way_srl, flowget.station_srl - 1);
                        }else{
                            sendNoti(globalv.WAITING_BUS_NOTI, 1, getString(R.string.nearby_bus_routes),  global.arraylistStringtoString(routes) );
                        }

                    } else {
                        if (mflow.size() > 0 && next_name != null)
                            sendBusNoti(globalv.DEFUALT_NOTI, 1, next_name, stations.get(flowget.station_srl - 1).station_name, direction_name, direction_name + " " + getString(R.string.direction) + "(" + flowget.route  + ")\n\n"  + stationListString, flowget.country_srl, flowget.route, flowget.way_srl, flowget.station_srl - 1);
                    //

                    }


                } else {

                    if (mflow.size() > 0 && next_name != null && station_left > 2) {


                        sendBusNoti(globalv.DEFUALT_NOTI, 1, station_left +  getString(R.string.item) + " " + getString(R.string._stations_left), next_name +"\n" + stations.get(flowget.station_srl - 1).station_name , direction_name, direction_name + " " + getString(R.string.direction) + "(" + flowget.route  + ")\n\n"  + stationListString, flowget.country_srl, flowget.route, flowget.way_srl, flowget.station_srl - 1);


                    } else {


                        //Check destination
                        if (station_left < 0) {

                            sendBusNoti(1, 1, getString(R.string.missed_the_stop),next_name + "\n" +  stations.get(flowget.station_srl - 1).station_name  , direction_name,  direction_name + " " + getString(R.string.direction) + "(" + flowget.route  + ")\n\n"  + stationListString, flowget.country_srl, flowget.route, flowget.way_srl, flowget.station_srl - 1);

                        } else {
                            if (station_left != 0)
                                sendBusNoti(2, 1, getString(R.string.almost_arrived) + " (" + station_left + ")", station_left + getString(R.string.item) + " " + getString(R.string._stations_left) + "\n\n" + next_name + "\n" + stations.get(flowget.station_srl - 1).station_name , direction_name,direction_name + " " + getString(R.string.direction) + "(" + flowget.route  + ")\n\n"  +  stationListString, flowget.country_srl, flowget.route, flowget.way_srl, flowget.station_srl - 1);
                            if (station_left == 0 && global.getGoalID(cx) == flowget.id_srl) {
                                arrivedAction(cx, getString(R.string.destinaton_arrived), stations.get(flowget.station_srl - 1).station_name);
                            } else if (station_left == 0) {
                                sendBusNoti(1, 1, next_name, stations.get(flowget.station_srl - 1).station_name + "\n\n" + getString(R.string.calculating_station_left), direction_name, direction_name + " " + getString(R.string.direction) + "(" + flowget.route  + ")\n\n"  + stationListString, flowget.country_srl, flowget.route, flowget.way_srl, flowget.station_srl - 1);
                            }
                        }

                    }

                }

            }
            if (globalv.moving_now == globalv.ACTIVE_STATE) same_place_count = 0;


            //  sendNoti(globalv.ALMOST_NOTI,1,"목적지 거의 도착","3 정거장 남음");
            if (location_mode == globalv.LIVE_ACTIVE_MODE) {

                if ((same_place_count > 3 && action_count > 0 && goal_id == 0) || (same_place_count > 14 && action_count > 0 && goal_id != 0))
                    setActionLocationMode(cx, globalv.ACTIVE_MODE);

            }

            if (location_mode == globalv.ACTIVE_MODE) {


                if (same_place_count > 1) { //introduce guide}
                }

//
                if (near_level <= 3 && same_place_count == 0 && action_count > 1) {
                    setActionLocationMode(cx, globalv.LIVE_ACTIVE_MODE);

                }
//                if (global.getSamePlaceCount(cx) < 2 && global.getSamePlaceCount(cx) < 3) {
//                    setActionLocationMode(cx, globalv.ACTIVE_MODE);
//
//            }

                //ACTIVE STANBY
                if ((same_place_count > 10 && goal_id == 0) || (same_place_count > 21 && goal_id != 0))
                    setActionLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
                //Stanby mode


            }


            if (location_mode == globalv.ACTIVE_STANBY_MODE) {


                //Stanby mode
                if (same_place_count > 2 && action_count > 0)
                    setActionLocationMode(cx, globalv.STANBY_MODE);

            }

            if (location_mode == globalv.STANBY_MODE) {
                // globalv.moving_now =globalv.STOP_STATE;
                //  global.log(same_place +  "adf");
                if (!same_place && action_count > 0) {
                    if (near_level == 2) setActionLocationMode(cx, globalv.ACTIVE_STANBY_MODE);

                }
                if (same_place && action_count > 3)
                    setActionLocationMode(cx, globalv.POWER_SAVED_MODE);

                //disconnect

            }

            if (location_mode == globalv.POWER_SAVED_MODE) {
                if (!same_place && action_count > 0) {
                    setActionLocationMode(cx, globalv.STANBY_MODE);
                }

                //Delete Noti
            }

            if (location_mode == globalv.HIBERNATION_MODE) {
                if (near_level <= 6) setActionLocationMode(cx, globalv.STANBY_MODE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void flowStation(Context cx){
//        //Get array from flow
//        ArrayList<flowclass> mInfoArray = getCurrentCountStations(cx, global.getCountSrl(cx));
//
//
//
//    }

    public ArrayList<flowclass> getCurrentCountStations(Context cx, int count_srl) {
        // InfoClass mInfoClass;
        ArrayList<flowclass> mInfoArray = new ArrayList<flowclass>();


        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getCurrentCountStation(count_srl);


        while (csr.moveToNext()) {

            //   global.log(csr.getString(csr.getColumnIndex("station_name")));

            flowclass mflowClass = new flowclass(
                    csr.getInt(csr.getColumnIndex("_id")),
                    csr.getInt(csr.getColumnIndex("count_srl")),
                    csr.getInt(csr.getColumnIndex("action_srl")),
                    csr.getInt(csr.getColumnIndex("id_srl")),
                    csr.getInt(csr.getColumnIndex("country_srl")),
                    csr.getString(csr.getColumnIndex("route")),
                    csr.getInt(csr.getColumnIndex("station_srl")),
                    csr.getInt(csr.getColumnIndex("way_srl")),
                    csr.getDouble(csr.getColumnIndex("latitude")),
                    csr.getDouble(csr.getColumnIndex("longitude")),
                    csr.getDouble(csr.getColumnIndex("station_latitude")),
                    csr.getDouble(csr.getColumnIndex("station_longitude")),
                    csr.getInt(csr.getColumnIndex("time")),
                    csr.getInt(csr.getColumnIndex("location_mode")),
                    csr.getInt(csr.getColumnIndex("location_level"))
            );

            mInfoArray.add(mflowClass);

        }


        csr.close();
        mDbOpenHelper.close();
        return mInfoArray;
    }

    public ArrayList<flowclass> selectflowStation(Context cx, int count_srl) {

        // InfoClass mInfoClass;
        ArrayList<flowclass> mInfoArray = new ArrayList<flowclass>();


        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getCurrentActiveCountStation(count_srl);


        //Save if 1- dozen
//        if(csr.getCount() == 1){
//
//            flowclass mflowClass = new flowclass(
//                    csr.getInt(csr.getColumnIndex("_id")),
//                    csr.getInt(csr.getColumnIndex("count_srl")),
//                    csr.getInt(csr.getColumnIndex("id_srl")),
//                    csr.getInt(csr.getColumnIndex("country_srl")),
//                    csr.getInt(csr.getColumnIndex("route_srl")),
//                    csr.getInt(csr.getColumnIndex("station_srl")),
//                   ,
//                    csr.getDouble(csr.getColumnIndex("latitude")),
//                    csr.getDouble(csr.getColumnIndex("longitude")),
//                    csr.getDouble(csr.getColumnIndex("station_latitude")),
//                    csr.getDouble(csr.getColumnIndex("station_longitude")),
//                    csr.getInt(csr.getColumnIndex("time")),
//                    csr.getInt(csr.getColumnIndex("location_mode")),
//                    csr.getInt(csr.getColumnIndex("location_level"))
//            );
//
//            mInfoArray.add(mflowClass);
//
//
//        }
        //dozen - dozen
        //   if(csr.getCount() > 1){
        //0 position 1 counti
        int pos = 0;
        int best_count = 0;
        int station_srl_count = 0;


        int station_srl_temp = 0;
        int station_srl_sub_temp = 0;

        int timestamp_best = 0;
        int station_srl_best = 0;

        csr.moveToLast();
        if (csr.getCount() != 0) {
            timestamp_best = csr.getInt(csr.getColumnIndex("time"));
            station_srl_best = csr.getInt(csr.getColumnIndex("station_srl"));
        }
        csr.moveToFirst();
        while (csr.moveToNext()) {
            station_srl_temp = 0;
            station_srl_sub_temp = 0;
            station_srl_count = 0;
            //     station_srl_temp = 0;
            global.log(timestamp_best + " : timestmap best, " + csr.getInt(csr.getColumnIndex("time")) + ": time");

            Cursor csrc = mDbOpenHelper.getDirectionRows(count_srl, csr.getInt(csr.getColumnIndex("country_srl")), csr.getString(csr.getColumnIndex("route")), csr.getInt(csr.getColumnIndex("way_srl")), csr.getInt(csr.getColumnIndex("station_srl")));
            //if(csr.getInt(csr.getColumnIndex("time")) < timestamp_temp) break;
            //  if(csrc.getCount() > count){
            while (csrc.moveToNext()) {
                global.log("station_srl :" + csrc.getInt(csrc.getColumnIndex("station_srl")) + "station_srl_sub_temp :" + station_srl_sub_temp + "station_srl_temp" + station_srl_temp);
                //time prevent strange number or prevant strange number if more bigger than last number
                if ((csr.getInt(csr.getColumnIndex("station_srl")) >= station_srl_temp + 3 && station_srl_temp != 0) || (timestamp_best > csr.getInt(csr.getColumnIndex("time")) + 220) && csr.getCount() > 3)
                    break;
                if ((csrc.getInt(csrc.getColumnIndex("station_srl")) > station_srl_sub_temp) && (csrc.getInt(csrc.getColumnIndex("station_srl")) <= station_srl_sub_temp + 3 || station_srl_sub_temp == 0)) {

                    station_srl_temp = csr.getInt(csr.getColumnIndex("station_srl"));
                    station_srl_sub_temp = csrc.getInt(csrc.getColumnIndex("station_srl"));
                    //    timestamp_temp = csr.getInt(cs7r.getColumnIndex("time"));
                    global.log(csrc.getInt(csrc.getColumnIndex("station_srl")) + "station");


                    station_srl_count++;
                    //prevent if csr number is strange
                }
                //    global.log(  "count : " + csrc.getCount() + "posistion : " + csrc.getPosition());

                //DROP NON NEED FLOW
                global.log("station_srl_best : " + station_srl_best + "station_srl" + csrc.getInt(csrc.getColumnIndex("station_srl")));
                global.log("id_srl : " + csrc.getInt(csrc.getColumnIndex("id_srl")));
                if ((csrc.getInt(csrc.getColumnIndex("station_srl")) > station_srl_sub_temp) && (station_srl_best == csrc.getInt(csrc.getColumnIndex("station_srl")) - 1)) {

                    mDbOpenHelper.DeleteFlowRow(csrc.getInt(csrc.getColumnIndex("id_srl")));
                }
                //Trash csr row
                if (csrc.getPosition() + 1 == csrc.getCount() && csr.getInt(csr.getColumnIndex("station_srl")) >= station_srl_sub_temp + 3)
                    station_srl_count = 0;

//               if(
//   csrc.getInt(csrc.getColumnIndex("station_srl")) < station_srl_sub_temp + 3  ||  station_srl_sub_temp == 0){
//                   global.log("true");
//               }else{
//                   global.log("false");
//               }


//global.log(csr.getInt(csr.getColumnIndex("station_srl")) + "AND" + csrc.getCount() );
                // global.log( csr.getInt(csr.getColumnIndex("station_srl")) + "VS" +csrc.getInt(csrc.getColumnIndex("station_srl")));
                //
            }
            global.log(csr.getInt(csr.getColumnIndex("way_srl")) + "========");


            //   global.log("best_count : " + best_count + "station_srl_count : " + station_srl_count);
            if (best_count <= station_srl_count) {

                pos = csr.getPosition();
                best_count = station_srl_count;
                waitingbus = best_count == 1 ? true : false;
                global.log(" : " + pos + "best count  : " + best_count);
            }

            // count = csrc.getCount();
            //      global.log(pos + "," + count);
            // }

            csrc.close();


        }


        csr.moveToPosition(pos);

        if (csr.getCount() != 0) {
            flowclass mflowClass = new flowclass(
                    csr.getInt(csr.getColumnIndex("_id")),
                    csr.getInt(csr.getColumnIndex("count_srl")),
                    csr.getInt(csr.getColumnIndex("action_srl")),
                    csr.getInt(csr.getColumnIndex("id_srl")),
                    csr.getInt(csr.getColumnIndex("country_srl")),
                    csr.getString(csr.getColumnIndex("route")),
                    csr.getInt(csr.getColumnIndex("station_srl")),
                    csr.getInt(csr.getColumnIndex("way_srl")),
                    csr.getDouble(csr.getColumnIndex("latitude")),
                    csr.getDouble(csr.getColumnIndex("longitude")),
                    csr.getDouble(csr.getColumnIndex("station_latitude")),
                    csr.getDouble(csr.getColumnIndex("station_longitude")),
                    csr.getInt(csr.getColumnIndex("time")),
                    csr.getInt(csr.getColumnIndex("location_mode")),
                    csr.getInt(csr.getColumnIndex("location_level"))
            );


            if (csr.getInt(csr.getColumnIndex("id_srl")) == same_place_id) {
                same_place_count++;
            } else {
                same_place_id = csr.getInt(csr.getColumnIndex("id_srl"));
            }

            global.log(csr.getInt(csr.getColumnIndex("id_srl")) + " : ID");
            mInfoArray.add(mflowClass);
        }

        //   }

        csr.close();
        mDbOpenHelper.close();
        return mInfoArray;
    }


    public ArrayList<InfoClass> getNearStations(Context cx, int location_level, Double latitude, Double longitude) {
        // InfoClass mInfoClass;
        ArrayList<InfoClass> mInfoArray = new ArrayList<InfoClass>();


        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getNearStation(latitude, longitude, location_level);


        while (csr.moveToNext()) {

            global.log(csr.getString(csr.getColumnIndex("station_name")));

            InfoClass mInfoClass = new InfoClass(
                    csr.getInt(csr.getColumnIndex("_id")),
                    csr.getInt(csr.getColumnIndex("country_srl")),
                    csr.getString(csr.getColumnIndex("route")),
                    csr.getInt(csr.getColumnIndex("way_srl")),
                    csr.getInt(csr.getColumnIndex("station_srl")),
                    csr.getString(csr.getColumnIndex("station_name")),
                    csr.getDouble(csr.getColumnIndex("station_latitude")),
                    csr.getDouble(csr.getColumnIndex("station_longitude"))
            );

            mInfoArray.add(mInfoClass);

        }


        csr.close();
        mDbOpenHelper.close();
        return mInfoArray;
    }


    public ArrayList<WayClass> getWaysByRoute(Context cx, int country_srl, String route) {
        // InfoClass mInfoClass;
        ArrayList<WayClass> mInfoArray = new ArrayList<WayClass>();


        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getWays(country_srl, route);


        while (csr.moveToNext()) {


            WayClass mWayClass = new WayClass(
                    csr.getInt(csr.getColumnIndex("way_srl")),
                    csr.getString(csr.getColumnIndex("station_name"))
            );

            mInfoArray.add(mWayClass);

        }

        global.log(csr.getCount() + "WAYSCOuNT");
        csr.close();
        mDbOpenHelper.close();
        return mInfoArray;
    }


    public ArrayList<InfoClass> getStations(int country_srl, String route, int way_srl) {
        // InfoClass mInfoClass;
        ArrayList<InfoClass> mInfoArray = new ArrayList<InfoClass>();


        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getStations(country_srl, route, way_srl);



        while (csr.moveToNext()) {

            // global.log(csr.getString(csr.getColumnIndex("station_name")));

            InfoClass mInfoClass = new InfoClass(
                    csr.getInt(csr.getColumnIndex("_id")),
                    csr.getInt(csr.getColumnIndex("country_srl")),
                    csr.getString(csr.getColumnIndex("route")),
                    csr.getInt(csr.getColumnIndex("way_srl")),
                    csr.getInt(csr.getColumnIndex("station_srl")),
                    csr.getString(csr.getColumnIndex("station_name")),
                    csr.getDouble(csr.getColumnIndex("station_latitude")),
                    csr.getDouble(csr.getColumnIndex("station_longitude"))
            );

            mInfoArray.add(mInfoClass);

        }


        csr.close();
        mDbOpenHelper.close();
        return mInfoArray;
    }


    public ArrayList<InfoClass> getNearStations(Context cx, Location location, int location_level) {


        return getNearStations(cx, location_level, location.getLatitude(), location.getLongitude());
    }


    public void setGoogleApiClient(Context cx) {
        mGoogleApiClient = new GoogleApiClient.Builder(cx)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) cx)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) cx)
                .build();

        mGoogleApiClient.connect();
        retrieveDeviceNode();
    }


    public void LocationRequest(Context cx, int interval, int fastestinterval, int priority) {
        global.log("LocationRequest");
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(priority)
                .setInterval(interval)
                .setFastestInterval(fastestinterval);


        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, locationRequest, (LocationListener) cx)
                .setResultCallback(new ResultCallback() {

                    public static final String TAG = "asdfasfdsf";

                    @Override
                    public void onResult(Result result) {
//                        Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE,
//                                Settings.Secure.LOCATION_MODE_OFF);

                        if (result.getStatus().isSuccess()) {
                            //if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "Successfully requested location updates");
                            //  }
                        } else {
                            Log.i("Failed", "Fail");

                        }
                    }
                });
    }

    public void LocationTimeout() {

        LocationTimeoutThread ltt = new LocationTimeoutThread();

        ltt.start();
    }

        class LocationTimeoutThread extends Thread {
            Handler mHandler = new Handler();


            @Override
            public void run() {
                super.run();
                Looper.prepare();
                try {
                    Thread.sleep(40000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!globalv.location_checked && location_mode >= globalv.ACTIVE_MODE) {
                    global.log("LocationTimeout");
                    setActionLocationMode(cx, globalv.POWER_SAVED_MODE);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }

            }

        }

    public void sendMessageDefault(final String msg, String data) {
sendMessage(msg, data.getBytes());

    }


    public void sendMessage(final String msg, final byte[] data) {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //    mGoogleApiClient.blockingConnect(10000, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, msg, data);
                }
            }).start();
        }


    }

    public void retrieveDeviceNode() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.blockingConnect(10000, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }
                //  mGoogleApiClient.disconnect();
            }
        }).start();


    }

    public void sendNoti(int kind, int noti_id, String title, String content) {
        ArrayList<noticlass> notiarray = new ArrayList<noticlass>();
        noticlass mnoticalss = new noticlass(kind, noti_id, title, content);

        notiarray.add(mnoticalss);
        Gson gson = new GsonBuilder().create();
        JsonArray noti_json_result = gson.toJsonTree(notiarray).getAsJsonArray();
        global.log(noti_json_result.toString());
        sendMessageDefault("notification" , noti_json_result.toString());

        if(useFg){
            doDev_Noti(kind,content + ";" + title);
        }

    }


    public void sendBusNoti(int kind, int noti_id, String title, String content, String direction_name, String station_summary,  int country_srl, String route, int way_srl, int station_srl) {
        ArrayList<BusNotiClass> notiarray = new ArrayList<BusNotiClass>();
        BusNotiClass mnoticalss = new BusNotiClass(kind, noti_id, title, content, direction_name, station_summary,   country_srl, route, way_srl, station_srl);

        notiarray.add(mnoticalss);
        Gson gson = new GsonBuilder().create();
        JsonArray noti_json_result = gson.toJsonTree(notiarray).getAsJsonArray();
        global.log(noti_json_result.toString());
        sendMessageDefault("notification" , noti_json_result.toString());

        if(useFg){
            doDev_Noti(kind,content + ";" + title);
        }

    }



    public void arrivedAction(Context cx, String title, String content) {
global.log("ARRIVED!");
        sendNoti(globalv.ARRIVED_NOTI, 1, title, content);
       // global.setGoalID(cx, 0);
        setActionLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
        // Intent viewIntent = new Intent(this, main.class);
        // global.setActiveNoti(cx, 1, viewIntent, title, content, R.drawable.ic_launcher, R.drawable.ic_launcher);
    }

    public void sendStationsData(int country_srl, String route, int way_srl){
        ArrayList<InfoClass> stations = getStations(country_srl, route, way_srl);
        Gson gson = new GsonBuilder().create();
        JsonArray json_result = gson.toJsonTree(stations).getAsJsonArray();
        global.log(json_result.toString());
        sendMessageDefault("stations_data", json_result.toString());
    }

    public void jsonSendStationData(String data){
        Map<String, String> resultmap = null;
        resultmap = global.getJSONArray(data);

        int country_srl = Integer.parseInt(String.valueOf(resultmap.get("country_srl")));
        String route = String.valueOf(resultmap.get("route"));
        int way_srl = Integer.parseInt(String.valueOf(resultmap.get("way_srl")));

        sendStationsData(country_srl, route, way_srl);


    }

    public void initInfo(){
        global.CountSrlUpdate(cx);
        global.setGoalID(cx, 0);
    }

    private void ActiveBusnoti(){
        sendNoti(globalv.ACTIVE_BUS_NOTI,1, getString(R.string.app_name), getString(R.string.bus_mode_started) );
    }


    public void initSamePlaceCount(){
        same_place_count = 0;
    }

    public void initLastStationId(){
        last_station_id = 0;
    }

//    private void sendstations(Context cx, Location location){
//        ArrayList<InfoClass> result_array = getNearStations(cx, location.getLatitude(), location.getLongitude());
//        Gson gson = new GsonBuilder().create();
//        JsonArray json_array_result = gson.toJsonTree(result_array).getAsJsonArray();
//        global.log(json_array_result.toString());
//        sendMessage("stations//" + json_array_result.toString(), null);
//    }


    public void setLocationChanged(boolean a){
        globalv.location_checked = a;
    }

    private boolean getLocationChanged(){
        return globalv.location_checked;
    }
    public void setLocationMode(Context cx, int level) {
        global.log("setLocationMode");
        location_mode = level;
        boolean test_mode = false;
        setLocationChanged(false);
        LocationTimeout();

        if (!global.debug_mode || !test_mode) {
            if (level == globalv.HIBERNATION_MODE)
                LocationRequest(cx, 5400000, 900000, LocationRequest.PRIORITY_NO_POWER);
            if (level == globalv.POWER_SAVED_MODE)
                LocationRequest(cx, 3600000, 900000, LocationRequest.PRIORITY_LOW_POWER);
            if (level == globalv.STANBY_MODE)
                LocationRequest(cx, 1800000, 900000, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (level == globalv.ACTIVE_STANBY_MODE)
                LocationRequest(cx, 300000, 60000, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (level == globalv.ACTIVE_MODE)
                LocationRequest(cx, 25000, 7000, LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (level == globalv.LIVE_ACTIVE_MODE)
                LocationRequest(cx, 15000, 3000, LocationRequest.PRIORITY_HIGH_ACCURACY);
            //Check if it has to send bus noti
            if(level  >= globalv.ACTIVE_MODE &&  action_count == 0) ActiveBusnoti();
        } else {
            LocationRequest(cx, 5000, 1000, LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


    }


    @Override
    public void onConnected(Bundle bundle) {
        global.log("Connected");
        //Send DB Version to Wear

        //SetLocationMode
        setLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
      //  LocationTimeout();

    }

    @Override
    public void onConnectionSuspended(int i) {
        global.log("onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        global.log("Success." + location.getLatitude() + "," + location.getLongitude());
        global.log("LOCATION!!!!!!!!!!!!!!!!!");
        setLocationChanged(true);
        if (initcheck) {
            conFlow(cx, location);
        } else {
            firstFlow(cx, location);

        }

    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //   showToast(messageEvent.getPath()); oversupply Launchpad" Pad
        global.log(messageEvent.getPath());
        checkMessage(messageEvent.getPath(), messageEvent.getData());


    }


    private void checkMessage(String message, byte[] bytes) {
        try {
            if (message.matches("Main_request_stations_data")) requsetStationsData(global.getStringbyBytes(bytes));
            if (message.matches("Main_LocationMode")) setRequestLocationMode(global.getStringbyBytes(bytes));
            if(message.matches("Main_setDestination")) setDestination(global.getStringbyBytes(bytes));
            if(message.matches("Main_startBusMode")) startBusMode(global.getStringbyBytes(bytes));
            if(message.matches("Main_getNearByRoutes")) getNearByRoutesToSend();
            if(message.matches("Main_getWays")) getWaysToSend(global.getStringbyBytes(bytes));
        } catch (Exception e) {
        }


    }

    private void sendOKMessage(String data){
        sendMessageDefault("okMessage" , data);
    }

    public void getWaysToSend(String data){

        Map<String, String> resultmap = null;
        resultmap = global.getJSONArray(data);
        int country_srl = Integer.parseInt(String.valueOf(resultmap.get("country_srl")));
        String route = (String.valueOf(resultmap.get("route")));

         ArrayList<WayClass> ways = getWaysByRoute(cx, country_srl, route);
        Gson gson = new GsonBuilder().create();
        JsonArray json_result = gson.toJsonTree(ways).getAsJsonArray();
        global.log("WAYS" + json_result.toString());
        sendMessageDefault("WaysByRoute" ,json_result.toString());
    }

    public void getNearByRoutesToSend(){

        Gson gson = new GsonBuilder().create();
        JsonArray json_result = gson.toJsonTree(routes).getAsJsonArray();
        global.log(json_result.toString());
        sendMessageDefault("NearByRoute" ,json_result.toString());
    }

    public void requsetStationsData(String data){
        jsonSendStationData(data);
        global.log(data);
     }

    public void setRequestLocationMode(String data){
        setActionLocationMode(CoreSystem.this,Integer.parseInt(data));
       sendOKMessage("requestLocationMode");
    }

    public void setDestination(String data){
        global.setGoalID(this, Integer.parseInt(data));
        initLastStationId();
        setActionLocationMode(cx, globalv.LIVE_ACTIVE_MODE);
        sendOKMessage("setDestination");
    }

    public void startBusMode(String data){
        boolean mode = Boolean.valueOf(data);

        if (mode) {
            //Start bus mode
            initLastStationId();
            initInfo();
            setActionLocationMode(this, 6);
        } else {
            setActionLocationMode(this, 3);

        }
        sendOKMessage("startBusMode");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(useFg){
            stopForeground(true);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        global.log("onConnectionFailed");
    }


    public void gyroSensorStart() {
        sl = new SensorListener();
        sl.sensorStart(CoreSystem.this);
    }

    public void setActionLocationMode(Context cx, int level) {
        global.log("Location Mode : " + level);
        action_count = 0;
        same_place_count = 0;

        //Sensor
        gyroSensorStart();

        if (level == globalv.LIVE_ACTIVE_MODE) {
            setLocationMode(cx, globalv.LIVE_ACTIVE_MODE);
        }

        if (level == globalv.ACTIVE_MODE) {
            setLocationMode(cx, globalv.ACTIVE_MODE);
        }


        if (level == globalv.ACTIVE_STANBY_MODE) {
            setLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
            sl.uregSensor();
        }


        if (level == globalv.STANBY_MODE) {
            setLocationMode(cx, globalv.STANBY_MODE);
            initInfo();
            sl.uregSensor();
        }


        if (level == globalv.POWER_SAVED_MODE) {
            setLocationMode(cx, globalv.POWER_SAVED_MODE);
           initInfo();
            sl.uregSensor();
        }

        if (level == globalv.HIBERNATION_MODE) {
            setLocationMode(cx, globalv.HIBERNATION_MODE);
          initInfo();
            sl.uregSensor();

        }

        global.setLocationMode(cx, level);

    }


//    @Override
//    public IBinder onBind(Intent intent) {
//        return mMessenger.getBinder();
//    }

    class IncomingHandler extends Handler { // Handler of incoming messages from clients.

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //Stations
                    jsonSendStationData(msg.obj.toString());
                    global.log(msg.obj.toString());
                    break;

                case 2:
                    //Set location mode
if(msg.obj != null)     setActionLocationMode(CoreSystem.this,Integer.parseInt( msg.obj.toString()));
                    break;

                case 3:
                        //Start bus mode
                        global.setGoalID(CoreSystem.this, 0);
                        global.CountSrlUpdate(cx);
                        setActionLocationMode(CoreSystem.this, 6);

                default:
                    super.handleMessage(msg);
            }
        }
    }


    private void almost_arrived(String this_station, String next_station) {
        sendNoti(2, 1, this_station, next_station);
    }

    private void hiddenDev(){
        useFg = global.getBooleanDev(this,globalv.DEV_FOREGROUND);
        useBt = global.getBooleanDev(this,globalv.DEV_CLICK_ON);
        if(useBt){
            IntentFilter filter = new IntentFilter();
            filter.addAction(globalv.broadcast_CLICK);
            devReceiver = new BusActiveReceiver();
            registerReceiver(devReceiver, filter);
        }
        if(global.getBooleanDev(this,globalv.DEV_START_F)){
            doDev_Noti(32767,"sc");
        }
    }
    private void doDev_Noti(int i,String s){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(cx).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("WBUS")
                .setLargeIcon(BitmapFactory.decodeResource(
                        cx.getResources(), R.drawable.ic_launcher))
                .setContentText("Running")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true);
        if(useBt){
            // Creates an Intent for the Activity
            Intent notifyIntent = new Intent(cx,DevReciever.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Creates the PendingIntent
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            notificationBuilder.setContentIntent(notifyPendingIntent);
        }
        if(s != null){
            if(s.equalsIgnoreCase("sc")){
                runningFg = true;
                startForeground(9997, notificationBuilder.build());
                return;
            }
        }
        if(i == globalv.WAITING_BUS_NOTI){
            if(!runningFg){
                runningFg = true;
                startForeground(9997, notificationBuilder.build());
                return;
            }
        }
        if(i == globalv.DEFUALT_NOTI){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        if(i == globalv.ALMOST_NOTI){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        }
        if(runningFg){
            String[] sp = s.split(";");
            notificationBuilder.setContentTitle(sp[0]);
            notificationBuilder.setContentText(sp[1]);
            NotificationManager mNotifyMgr =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(9997, notificationBuilder.build());
        }
    }
    public class BusActiveReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(globalv.broadcast_CLICK)){
                //globalv.
                if(location_mode == 6 || location_mode == 5){
                    setActionLocationMode(context,globalv.STANBY_MODE);
                }else if(location_mode <= 4){
                    setActionLocationMode(context,globalv.LIVE_ACTIVE_MODE);
                }
            }
        }
    }

}
