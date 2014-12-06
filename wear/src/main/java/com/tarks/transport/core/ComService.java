package com.tarks.transport.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.tarks.transport.ui.BusArrive;
import com.tarks.transport.R;
import com.tarks.transport.ui.StationList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ComService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    GoogleApiClient mGoogleApiClient;
    private String nodeId;

    @Override
    public void onCreate() {
        super.onCreate();

        setGoogleApiClient(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
     //   showToast(messageEvent.getPath());
        global.log(messageEvent.getPath());
checkMessage(messageEvent.getPath(), messageEvent.getData());


    }
    private void checkMessage(String message, byte[] bytes){
        try {
            String[] array = message.split("//");
            String action_kind = array[0];
            String data = array[1];

            if(action_kind.matches("notification")) actionNoti(data);

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
            Intent viewIntent = new Intent(ComService.this, StationList.class);
            global.BusNoti(ComService.this, noti_id, viewIntent, title, content, R.drawable.ic_launcher,  R.drawable.ride_bus_background);
            sendMessage("request_stations_data//ㄹㅇ허ㅏㅣㅇㄹ너하ㅣㅇ허ㅏㅣㅇㄴ하ㅣㄴㅇㄹ", null);
        }

        //Bus almost arrived
        if (kind == 2) {

            global.Vibrate(this, 2000);
            Intent viewIntent = new Intent(ComService.this, StationList.class);
            global.BusNoti(ComService.this, noti_id, viewIntent, title, content, R.drawable.ic_launcher, R.drawable.flag);
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
    private void setGoogleApiClient(Context cx){
        mGoogleApiClient = new GoogleApiClient.Builder(cx)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) cx)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) cx)
                .build();

        mGoogleApiClient.connect();
        retrieveDeviceNode();
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
                    //    mGoogleApiClient.blockingConnect(10000, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, msg, data);
                }
            }).start();
     //   }



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