package com.tarks.transport;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tarks.transport.core.CoreSystem;
import com.tarks.transport.core.global;
import com.tarks.transport.core.noticlass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by panda on 2014-10-25.
 */
public class main extends ActionBarActivity{

    private CoreSystem cs;

    private int progress = 0;
    private SeekBar pb;
    private TextView tv;
    private Context ct;


    private GoogleApiClient mGoogleApiClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layouttest);
       // cs = new CoreSystem();






//



      // retrieveDeviceNode();



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
            //        nodeId = nodes.get(0).getId();
                }
              //  mGoogleApiClient.disconnect();
            }
        }).start();



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








}
