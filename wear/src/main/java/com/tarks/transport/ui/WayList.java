package com.tarks.transport.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.R;

public class WayList extends Activity
        implements WearableListView.ClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Sample dataset for the list
    String[] elements = { "동춘동종점 방향" , "무지개아파트 방향"};

    GoogleApiClient mGoogleApiClient;
    ProgressBar ps;

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {




       // if(viewHolder.getItemId() == 0){
            Intent i = new Intent(WayList.this, StationList.class);
        i.putExtra("kind", "adsfafdsfa");
            startActivity(i);
     //   }
    }

    @Override
    public void onTopEmptyRegionClick() {

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


    class SendMessage extends Thread {
        String path;
        String message;

        // Constructor to send a message to the data layer
        SendMessage(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            NodeApi.GetLocalNodeResult nodes = Wearable.NodeApi.getLocalNode(mGoogleApiClient).await();
            Node node = nodes.getNode();
            Log.v("actiona", "Activity Node is : " + node.getId() + " - " + node.getDisplayName());
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, message.getBytes()).await();
            if (result.getStatus().isSuccess()) {
                Log.v("actiona", "Activity Message: {" + message + "} sent to: " + node.getDisplayName());
            } else {
                // Log an error
                Log.v("actiona", "ERROR: failed to send Activity Message");
            }

        }

    }
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    ps = (ProgressBar) findViewById(R.id.progressBar);

    if(null == mGoogleApiClient) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    if(!mGoogleApiClient.isConnected()){
        mGoogleApiClient.connect();
        //  Log.v(TAG, "Connecting to GoogleApiClient..");
    }

    Intent intent = getIntent(); // 인텐트 받아오고

   // int country_srl = intent.getIntExtra("country_srl", 0);
    int route_srl = intent.getIntExtra("route_srl", 0); // 인텐트로 부터 데이터 가져오고


    new SendMessage("getWays",String.valueOf(route_srl)).start();

        // Get the list component from the layout of the activity
        WearableListView listView =
        (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new ListAdapter(this, elements));

        // Set a click listener
        listView.setClickListener(this);
        }

};