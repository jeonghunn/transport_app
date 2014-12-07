package com.tarks.transport.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.R;

public class ActionActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

   //     startService(new Intent(this, ComService.class));

        Intent intent = getIntent(); // 인텐트 받아오고
        String action_kind = intent.getStringExtra("action_kind"); // 인텐트로 부터 데이터 가져오고


        if(action_kind.matches("delete_noti")){
          //  requsetToMain("delete_noti//1");
          //  global.log("dfsafdsafsdfsd");
            new SendMessage("requestLocationMode//3", null).start();
        }


        finish();

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
    public void onDestroy() {
        super.onDestroy();

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
