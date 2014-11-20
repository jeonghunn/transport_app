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
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tarks.transport.db.DbOpenHelper;
import com.tarks.transport.db.InfoClass;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by JHRunning on 11/20/14.
 */
public class CoreSystem extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private String nodeId;
    private Context cx;

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





    if(global.getDBCountSrl(cx) < global.getCountSrl(cx)){
        firstFlow(cx, lc);
    }else{
        conFlow();
    }

}



    public void firstFlow(Context cx, Location lc){

        //Location level 1
if(getStations(cx,lc,1).size() > 0) {

}
    }

    public void  conFlow(){

    }

    public ArrayList<InfoClass> getNearStations(Context cx, int location_level, Double latitude, Double longitude){
        // InfoClass mInfoClass;
        ArrayList<InfoClass> mInfoArray = new ArrayList<InfoClass>();



        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getNearStation(latitude, longitude,1);


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
                    mGoogleApiClient.blockingConnect(10000, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, msg, data);
                }
            }).start();
        }



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
        if(level == globalv.HIBERNATION_MODE) LocationRequest(cx, 5400000, 900000 ,LocationRequest.PRIORITY_NO_POWER);
        if(level == globalv.POWER_SAVED_MODE) LocationRequest(cx, 3600000, 900000 ,LocationRequest.PRIORITY_LOW_POWER);
        if(level == globalv.STANBY_MODE) LocationRequest(cx, 1800000, 900000 ,LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(level == globalv.ACTIVE_STANBY_MODE) LocationRequest(cx, 1200000, 60000 ,LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(level == globalv.ACTIVE_MODE) LocationRequest(cx, 60000, 5000 ,LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(level == globalv.LIVE_ACTIVE_MODE) LocationRequest(cx, 25000, 5000 ,LocationRequest.PRIORITY_HIGH_ACCURACY);
    }






    @Override
    public void onConnected(Bundle bundle) {
global.log("Connected");
      setActiveMode(cx);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        global.log( "Success." + location.getLatitude() + "," +  location.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //Location mode set
    private void setHibernationMode(Context cx){
        setLocationMode(cx, globalv.HIBERNATION_MODE);
        global.CountSrlUpdate(cx);
    }

    private void setPowerSavedMode(Context cx){
        setLocationMode(cx, globalv.POWER_SAVED_MODE);
        global.CountSrlUpdate(cx);
    }

    private void setStanbyMode(Context cx){
        setLocationMode(cx, globalv.STANBY_MODE);
        global.CountSrlUpdate(cx);
    }


    private void setActiveStanbyMode(Context cx){
        setLocationMode(cx, globalv.ACTIVE_STANBY_MODE);
    }


    //Location mode set
    private void setActiveMode(Context cx){
        setLocationMode(cx, globalv.ACTIVE_MODE);
    }


    //Location mode set
    private void setLiveActiveMode(Context cx){
        setLocationMode(cx, globalv.LIVE_ACTIVE_MODE);
    }

    private void almost_arrived(String this_station, String next_station){
        sendNoti(2,1, this_station, next_station);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
