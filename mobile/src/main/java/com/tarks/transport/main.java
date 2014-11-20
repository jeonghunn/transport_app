package com.tarks.transport;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tarks.transport.core.global;
import com.tarks.transport.core.globalv;
import com.tarks.transport.core.noticlass;
import com.tarks.transport.db.DbOpenHelper;
import com.tarks.transport.db.InfoClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by panda on 2014-10-25.
 */
public class main extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private int progress = 0;
    private SeekBar pb;
    private TextView tv;
    private Context ct;
    private String nodeId;

    private GoogleApiClient mGoogleApiClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layouttest);
        pb = (SeekBar) findViewById(R.id.seekBar);
        tv = (TextView) findViewById(R.id.txt);
        ct = this;
        pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int p, boolean fromUser) {
                // TODO Auto-generated method stub
                progress = p;
             //   String[] elements2 = {"부평구청역", "삭은다리", "대우자동차(동문)", "갈산역", "갈산시장", "삼산사거리"};
               // sendToast(elements2[Integer.parseInt((Math.floor(p/3) + "").substring(0,1))]);
                sendMessage(String.valueOf(p), null);
            }
        });




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

       retrieveDeviceNode();



    }



    private void setLocationMode(int level){
       if(level == globalv.HIBERNATION_MODE) LocationRequest(5400000, 900000 ,LocationRequest.PRIORITY_NO_POWER);
        if(level == globalv.POWER_SAVED_MODE) LocationRequest(3600000, 900000 ,LocationRequest.PRIORITY_LOW_POWER);
        if(level == globalv.STANBY_MODE) LocationRequest(1800000, 900000 ,LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(level == globalv.ACTIVE_STANBY_MODE) LocationRequest(1200000, 60000 ,LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(level == globalv.ACTIVE_MODE) LocationRequest(60000, 5000 ,LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(level == globalv.LIVE_ACTIVE_MODE) LocationRequest(25000, 5000 ,LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onConnected(Bundle bundle) {
        initCore();
    }

    private void initCore(){
        global.log("Connected");
        //Check location
        setLocationMode(globalv.ACTIVE_MODE);
    }

    //Location mode set
   private void setHibernationMode(){
       setLocationMode(globalv.HIBERNATION_MODE);
       global.CountSrlUpdate(this);
   }

    private void setPowerSavedMode(){
        setLocationMode(globalv.POWER_SAVED_MODE);
        global.CountSrlUpdate(this);
    }

    private void setStanbyMode(){
        setLocationMode(globalv.STANBY_MODE);
        global.CountSrlUpdate(this);
    }


    private void setActiveStanbyMode(){
        setLocationMode(globalv.ACTIVE_STANBY_MODE);
    }


    //Location mode set
    private void setActiveMode(){
        setLocationMode(globalv.ACTIVE_MODE);
    }


    //Location mode set
    private void setLiveActiveMode(){
        setLocationMode(globalv.LIVE_ACTIVE_MODE);
    }

    public void LocationRequest(int interval, int fastestinterval, int priority){
        global.log("LocationRequest");
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(priority)
                .setInterval(interval)
                .setFastestInterval(fastestinterval);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, locationRequest, this)
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
                            Toast.makeText(main.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void retrieveDeviceNode() {

        new Thread(new Runnable() {
            @Override
            public void run() {
             //   mGoogleApiClient.blockingConnect(10000, TimeUnit.MILLISECONDS);
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


    private ArrayList<InfoClass> getNearStations(Double latitude, Double longitude){
        // InfoClass mInfoClass;
        ArrayList<InfoClass> mInfoArray = new ArrayList<InfoClass>();



        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
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

    private void sendMessage(final String msg, final byte[] data) {
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


    private void almost_arrived(String this_station, String next_station){
        sendNoti(2,1, this_station, next_station);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi
//                    .removeLocationUpdates(mGoogleApiClient,  this);
//        }
//        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
       // if (Log.isLoggable(TAG, Log.DEBUG)) {
    global.log("connection to location client suspended");
     //   }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        global.log("onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
      //  Toast.makeText(TestActivty.this, "Success." + location.getLatitude() + "," +  location.getLongitude(), Toast.LENGTH_LONG).show();
       // Toast.makeText(TestActivty.this, getNearStations(location.getLatitude(), location.getLongitude()) + "역이 주변에 있는걸 감지했습니다.", Toast.LENGTH_LONG).show();

        ArrayList<InfoClass> result_array =  getStations(location);
        almost_arrived(result_array.get(0).station_name.toString(), String.valueOf(result_array.size()));


global.log( "Success." + location.getLatitude() + "," +  location.getLongitude());

    }

   private void sendNoti(int kind, int noti_id, String title, String content) {
       ArrayList<noticlass> notiarray = new ArrayList<noticlass>();
       noticlass mnoticalss = new noticlass(kind, noti_id, title, content);

       notiarray.add(mnoticalss);
       Gson gson = new GsonBuilder().create();
       JsonArray noti_json_result = gson.toJsonTree(notiarray).getAsJsonArray();
       global.log(noti_json_result.toString());
       sendMessage("notification//" + noti_json_result.toString(), null);

   }

       private ArrayList<InfoClass> getStations(Location location){
           ArrayList<InfoClass> result_array = getNearStations(location.getLatitude(), location.getLongitude());


           return result_array;
       }


    private void sendstations(Location location){
        ArrayList<InfoClass> result_array = getNearStations(location.getLatitude(), location.getLongitude());
        Gson gson = new GsonBuilder().create();
        JsonArray json_array_result = gson.toJsonTree(result_array).getAsJsonArray();
        global.log(json_array_result.toString());
        sendMessage("stations//" + json_array_result.toString(), null);
    }


}
