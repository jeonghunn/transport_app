package com.tarks.transport.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tarks.transport.db.DbOpenHelper;
import com.tarks.transport.db.InfoClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by JHRunning on 11/20/14.
 */
public class CoreSystem extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private String nodeId;
    private Context cx;
    private int location_mode;
    private int dbid;
    private int sis;
    private int plm;
    private boolean initcheck = false;
    private int action_count = 0;

    @Override
    public void onCreate() {
        super.onCreate();

global.log("HI service start!");
        cx = CoreSystem.this;
        initCore(CoreSystem.this);
    }

    public void initCore(Context cx){
        setGoogleApiClient(cx);
    }

public void startFlow(Context cx, Location lc){





   // if(global.getDBCountSrl(cx) < global.getCountSrl(cx)){
        firstFlow(cx, lc);

      //  conFlow(cx, lc);

}



    public void firstFlow(Context cx, Location lc){
        global.log("firstFlow");
         global.DBCountSrlUpdate(cx);

        int near_level = 0;

        for (int i = 1; i <= 6; i++) {
            if(getStations(cx,lc,i).size() > 0) {
                near_level = i;
                break;
            }

            if(i == 6) near_level = 7;
        }


        if(near_level == 1) setActionLocationMode(cx, globalv.ACTIVE_MODE);
        if(near_level == 2) setActionLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
        if(near_level == 3) setActionLocationMode(cx, globalv.STANBY_MODE);
        if(near_level == 4) setActionLocationMode(cx, globalv.POWER_SAVED_MODE);
        if(near_level == 5) setActionLocationMode(cx, globalv.HIBERNATION_MODE);
        if(near_level >= 6) setActionLocationMode(cx, globalv.HIBERNATION_MODE);

        initcheck = true;

    }

    public void  conFlow(Context cx, Location lc){
global.log("conFlow");
        action_count++;

        int near_level = 0;
        ArrayList<InfoClass>  ic = null;

        for (int i = 1; i <= 6; i++) {
           ic = getStations(cx,lc,i);
            if(ic.size() > 0) {
                near_level = i;
                global.log("fggg" + i);
                break;
            }

            if(i == 6) near_level = 7;
        }

        //Check same place
        if(dbid == ic.get(0).id && sis == ic.size() && location_mode == plm){
            global.SamePlaceCountUpdate(cx);
        }else{
            dbid = ic.get(0).id;
            sis = ic.size();
            plm = location_mode;
        }

        global.log("id : " + dbid);
        global.log("getlolevel : " + near_level);
        if(location_mode == globalv.LIVE_ACTIVE_MODE){

            if(global.getSamePlaceCount(cx) > 24 && global.getSamePlaceCount(cx) >= action_count - 2 && action_count > 1) setActionLocationMode(cx, globalv.ACTIVE_MODE);

        }

        if(location_mode == globalv.ACTIVE_MODE) {
            sendNoti(1,1,"1,17,12,9", ic.get(0).station_name);

            if (global.getSamePlaceCount(cx) > 1) { //introduce guide}
            }

//
            if (near_level  <= 3 && global.getSamePlaceCount(cx) <= action_count - 2 && action_count > 1) {
                setActionLocationMode(cx, globalv.LIVE_ACTIVE_MODE);

            }
//                if (global.getSamePlaceCount(cx) < 2 && global.getSamePlaceCount(cx) < 3) {
//                    setActionLocationMode(cx, globalv.ACTIVE_MODE);
//
//            }

            //Stanby mode
            if (global.getSamePlaceCount(cx) > 69 || near_level >= 5 || global.getSamePlaceCount(cx) > 29 && global.getSamePlaceCount(cx) >= action_count - 2 && action_count > 1 && near_level >= 3) setActionLocationMode(cx, globalv.STANBY_MODE);

        }


        if (location_mode == globalv.ACTIVE_STANBY_MODE) {

            if (global.getSamePlaceCount(cx) >= 1) { //introduce guide}]}
            }

            if ( near_level  <= 3 && global.getSamePlaceCount(cx) <= action_count - 2 && action_count > 1) {
                setActionLocationMode(cx, globalv.ACTIVE_MODE);

            }

                //Stanby mode
            if (global.getSamePlaceCount(cx) > 5 || global.getSamePlaceCount(cx) > 2 && near_level >= 3 || near_level >= 5) setActionLocationMode(cx, globalv.STANBY_MODE);

            }

        if (location_mode == globalv.STANBY_MODE) {
            if ( near_level  <= 3 && global.getSamePlaceCount(cx) <= action_count - 2 && action_count > 1) {
               if(near_level == 2) setActionLocationMode(cx, globalv.ACTIVE_MODE);
                if(near_level == 3) setActionLocationMode(cx, globalv.ACTIVE_STANBY_MODE);

            }
            if (global.getSamePlaceCount(cx) > 1 || global.getSamePlaceCount(cx) > 2 && near_level >= 3 || near_level >= 5) setActionLocationMode(cx, globalv.POWER_SAVED_MODE);
        }

        if (location_mode == globalv.POWER_SAVED_MODE) {
            if ( near_level  <= 5 && global.getSamePlaceCount(cx) <= action_count - 2 && action_count > 1) {
                setActionLocationMode(cx, globalv.STANBY_MODE);
            }

            //Delete Noti
        }

        if (location_mode == globalv.HIBERNATION_MODE) {
            if(near_level <= 6) setActionLocationMode(cx, globalv.STANBY_MODE);
        }


    }

    public ArrayList<InfoClass> getNearStations(Context cx, int location_level, Double latitude, Double longitude){
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
                    csr.getInt(csr.getColumnIndex("route_srl")),
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



    public ArrayList<InfoClass> getStations(Context cx, Location location, int location_level){


        return getNearStations(cx, location_level ,location.getLatitude(), location.getLongitude());
    }


    public void setGoogleApiClient(Context cx){
        mGoogleApiClient = new GoogleApiClient.Builder(cx)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) cx)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) cx)
                .build();

        mGoogleApiClient.connect();
        retrieveDeviceNode();
    }


    public void LocationRequest(Context cx, int interval, int fastestinterval, int priority){
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
        sendMessage("notification//" + noti_json_result.toString(), null);

    }


//    private void sendstations(Context cx, Location location){
//        ArrayList<InfoClass> result_array = getNearStations(cx, location.getLatitude(), location.getLongitude());
//        Gson gson = new GsonBuilder().create();
//        JsonArray json_array_result = gson.toJsonTree(result_array).getAsJsonArray();
//        global.log(json_array_result.toString());
//        sendMessage("stations//" + json_array_result.toString(), null);
//    }

    public void setLocationMode(Context cx, int level){
        location_mode = level;
        boolean test_mode = false;
        if(!global.debug_mode || !test_mode) {
            if (level == globalv.HIBERNATION_MODE)
                LocationRequest(cx, 5400000, 900000, LocationRequest.PRIORITY_NO_POWER);
            if (level == globalv.POWER_SAVED_MODE)
                LocationRequest(cx, 3600000, 900000, LocationRequest.PRIORITY_LOW_POWER);
            if (level == globalv.STANBY_MODE)
                LocationRequest(cx, 1800000, 900000, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (level == globalv.ACTIVE_STANBY_MODE)
                LocationRequest(cx, 300000, 60000, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (level == globalv.ACTIVE_MODE)
                LocationRequest(cx, 25000, 10000, LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (level == globalv.LIVE_ACTIVE_MODE)
                LocationRequest(cx, 15000, 5000, LocationRequest.PRIORITY_HIGH_ACCURACY);
        }else{
            LocationRequest(cx, 10000, 6000, LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }






    @Override
    public void onConnected(Bundle bundle) {
global.log("Connected");
     setActionLocationMode(cx, globalv.ACTIVE_MODE);


    }

    @Override
    public void onConnectionSuspended(int i) {
        global.log("onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        global.log( "Success." + location.getLatitude() + "," +  location.getLongitude());

        if(initcheck){
            conFlow(cx, location);
        }else{
            firstFlow(cx, location);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        global.log("onConnectionFailed");
    }

    public void setActionLocationMode(Context cx, int level){
        global.log("Location Level : " + level);
        action_count = 0;

        if(level == globalv.LIVE_ACTIVE_MODE){
            setLocationMode(cx, globalv.LIVE_ACTIVE_MODE);
        }

        if(level == globalv.ACTIVE_MODE){
            setLocationMode(cx, globalv.ACTIVE_MODE);
        }


        if(level == globalv.ACTIVE_STANBY_MODE){
            setLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
        }


        if(level == globalv.STANBY_MODE){
            setLocationMode(cx, globalv.STANBY_MODE);
            global.CountSrlUpdate(cx);
        }


        if(level == globalv.POWER_SAVED_MODE){
            setLocationMode(cx, globalv.POWER_SAVED_MODE);
            global.CountSrlUpdate(cx);
        }

        if(level == globalv.HIBERNATION_MODE){
            setLocationMode(cx, globalv.HIBERNATION_MODE);
            global.CountSrlUpdate(cx);

        }

        global.resetSamePlaceCount(cx);

    }








    private void almost_arrived(String this_station, String next_station){
        sendNoti(2,1, this_station, next_station);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
