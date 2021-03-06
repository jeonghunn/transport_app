package com.tarks.transport.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tarks.transport.db.*;
import com.tarks.transport.ui.BusArrive;
import com.tarks.transport.R;
import com.tarks.transport.ui.StationList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ComService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    GoogleApiClient mGoogleApiClient;
    private String nodeId;
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    private Intent intent;


    @Override
    public void onCreate() {
        super.onCreate();

        setGoogleApiClient(this);

    }




    class IncomingHandler extends Handler { // Handler of incoming messages from clients.

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                  //  jsonSendStationData(msg.obj.toString());

                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
     //   showToast(messageEvent.getPath());
        global.log(messageEvent.getPath());
checkMessage(messageEvent.getPath(), messageEvent.getData());


    }
    private void checkMessage(String message, byte[] bytes){
        try {
          //  String[] array = message.split("//");
         //   String action_kind = array[0];
         //   String data = array[1];

            if(message.matches("notification")) actionNoti(global.getStringbyBytes(bytes));
            if(message.matches("stations_data")) StationsDataDBInput(global.getStringbyBytes(bytes));
            if(message.matches("NearByRoute")) NearByRoute(global.getStringbyBytes(bytes));
            if(message.matches("WaysByRoute")) WaysByRoute(global.getStringbyBytes(bytes));
            if(message.matches("checkDataBase")) doCheckDB(global.getStringbyBytes(bytes));

            //To Phone
            if(message.matches("requestLocationMode")) requestLocationMode(global.getStringbyBytes(bytes));
            if(message.matches("setDestination")) setDestination(global.getStringbyBytes(bytes));
            if(message.matches("startBusMode")) startBusMode(global.getStringbyBytes(bytes));
            if(message.matches("getRouteNumbers")) getRouteNumbers(global.getStringbyBytes(bytes));
            if(message.matches("getWays")) getWays(global.getStringbyBytes(bytes));

        } catch (Exception e){

            e.printStackTrace();
        }




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

            String direction_name = String.valueOf(resultmap.get("direction_name"));
            String station_summary = String.valueOf(resultmap.get("station_summary"));
            int route_srl = Integer.parseInt(String.valueOf(resultmap.get("route_srl")));
            int country_srl = Integer.parseInt(String.valueOf(resultmap.get("country_srl")));
            int way_srl = Integer.parseInt(String.valueOf(resultmap.get("way_srl")));
            int station_srl = Integer.parseInt(String.valueOf(resultmap.get("station_srl")));


            Intent viewIntent = new Intent(ComService.this, StationList.class);
            global.BusNoti(ComService.this, noti_id, viewIntent, title, content, direction_name ,country_srl,route_srl, way_srl, station_srl, station_summary, R.drawable.ic_launcher, global.getNight()? R.drawable.ride_bus_background_night : R.drawable.ride_bus_background);

            checkDataDB(country_srl, route_srl, way_srl, station_srl);
        }

        //Bus almost arrived
        if (kind == 2) {

            String direction_name = String.valueOf(resultmap.get("direction_name"));
            String station_summary = String.valueOf(resultmap.get("station_summary"));
            int route_srl = Integer.parseInt(String.valueOf(resultmap.get("route_srl")));
            int country_srl = Integer.parseInt(String.valueOf(resultmap.get("country_srl")));
            int way_srl = Integer.parseInt(String.valueOf(resultmap.get("way_srl")));
            int station_srl = Integer.parseInt(String.valueOf(resultmap.get("station_srl")));

            global.Vibrate(this, 2000);
            Intent viewIntent = new Intent(ComService.this, StationList.class);
            global.BusNoti(ComService.this, noti_id, viewIntent, title, content, direction_name, country_srl,route_srl, way_srl, station_srl, station_summary, R.drawable.ic_launcher, R.drawable.flag);
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

        //waiting bus
        if(kind == 4){
            WaitingBusNoti(title, content);
        }

    }

    //To Phone
    private void getWays(String data){
        sendMessage("Main_getWays", data.getBytes());
    }


    private void WaysByRoute(String data){
        //global.log("NearByRoute Calling");



        Intent messageIntent = new Intent();
        messageIntent.setAction("get-ways-by-route");
        messageIntent.putExtra("message", data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }


    private void NearByRoute(String data){
        //global.log("NearByRoute Calling");



        Intent messageIntent = new Intent();
        messageIntent.setAction("get-route-numbers");
        messageIntent.putExtra("message", data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }

    private void setDestination(String data){
        sendMessage("Main_setDestination", data.getBytes());
    }

    private void startBusMode(String data){
        sendMessage("Main_startBusMode", data.getBytes());
    }


private void arrivedAction(String title, String content){
   // WakeLock.acquireCpuWakeLock(ListenerService.this);
    Intent viewIntent = new Intent(this, StationList.class);
    global.setActiveNoti(this, 1, viewIntent, title, content, R.drawable.ic_launcher, R.drawable.flag);


    global.Vibrate(this, 4000);
   // WakeLock.releaseCpuLock();


}

    private void WaitingBusNoti(String title, String content){
        // WakeLock.acquireCpuWakeLock(ListenerService.this);
      //  Intent viewIntent = new Intent(this, StationList.class);
        global.WaitingBusNoti(this, 1, title, content, R.drawable.ic_launcher, global.getNight()? R.drawable.bus_background_night : R.drawable.bus_background);


     //   global.Vibrate(this, 4000);
        // WakeLock.releaseCpuLock();


    }

    private void setGoogleApiClient(Context cx){
        mGoogleApiClient = new GoogleApiClient.Builder(cx)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) cx)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) cx)
                .build();

        mGoogleApiClient.connect();
        retrieveDeviceNode();
    }


    private void getRouteNumbers(String data){
        sendMessage("Main_getNearByRoutes", data.getBytes());
    }



    private void retrieveDeviceNode() {

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

    public void sendMessage(final String msg, final byte[] data) {
       // if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                        mGoogleApiClient.blockingConnect(10000, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, msg, data);
                }
            }).start();
     //   }



    }


    public void sendMessageDefault(final String msg, String data) {
        sendMessage(msg, data.getBytes());

    }


    private void doCheckDB(String data){
        Map<String, String> resultmap = null;
        resultmap = global.getJSONArray(data);

        int country_srl = Integer.parseInt(String.valueOf(resultmap.get("country_srl")));
        int route_srl = Integer.parseInt(String.valueOf(resultmap.get("route_srl")));
        int way_srl = Integer.parseInt(String.valueOf(resultmap.get("way_srl")));
        int station_srl = 1;

        checkDataDB(country_srl, route_srl, way_srl, station_srl);
    }


    private void requestLocationMode(String mode){
        global.log(mode + "locationmode");
        sendMessageDefault("Main_LocationMode" , mode);
    }

    private void StationsDataDBInput(String data){
         ArrayList<InfoClass> infoArraylist = new ArrayList<InfoClass>();
        infoArraylist = global.getJSONArrayListByInfoClass(data);

     //   global.log(data.toString());

        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        for (int i = 0; i < infoArraylist.size(); i++) {
           // global.log(infoArraylist.get(i).station_name);
            InfoClass get = infoArraylist.get(i);
            mDbOpenHelper.insertColumn(get.id, get.country_srl,get.route_srl,get.station_srl,get.way_srl, get.station_name, get.station_latitude,  get.station_longitude);
        }

        mDbOpenHelper.close();
        broadcastCheckDataDB(true);
    }


    private void broadcastCheckDataDB(boolean result){

        Intent messageIntent = new Intent();
        messageIntent.setAction("Check-Data-Base");
        messageIntent.putExtra("message", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }

    private void checkDataDB(int country_srl, int route_srl, int way_srl, int station_srl){
        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();




       if(mDbOpenHelper.checkStations(country_srl, route_srl, way_srl)){
           broadcastCheckDataDB(true);

       }else{

           ArrayList<StationClass> notiarray = new ArrayList<StationClass>();
           StationClass mnoticalss = new StationClass(country_srl, route_srl, way_srl, station_srl);

           notiarray.add(mnoticalss);
           Gson gson = new GsonBuilder().create();
           JsonArray noti_json_result = gson.toJsonTree(notiarray).getAsJsonArray();
         //  global.log(noti_json_result.toString());
           sendMessageDefault("Main_request_stations_data", noti_json_result.toString());

       }






        mDbOpenHelper.close();
    }


    private void showToast(String message)  {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

//        Intent viewIntent = new Intent(this, StationList.class);
//        global.setNotifcation(this, 1, viewIntent, "가나다", "가나다", R.drawable.bus_background);



}

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



}