package com.tarks.transport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.core.global;
import com.tarks.transport.ui.RouteList;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

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



        new SendMessage("startBusMode", "true").start();


        finish();

        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.bus_mode_started));
        startActivity(intent);


    //    global.BusNoti(this, 1, intent, "펜타포트", "서당골", "ㅁㅇㄴㄹ", 1,1,1,1,"ㅇㅁㄴㄹ",R.drawable.ic_launcher, R.drawable.bus_background_night);
//        Intent intent2 = new Intent(this, List.class);
//        startActivity(intent2);



//        setContentView(R.layout.activity_main);
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
////                FragmentManager fragmentManager = getFragmentManager();
////                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                CardFragment cardFragment = CardFragment.create("WhereBus", "asdf", R.drawable.ic_launcher);
////                fragmentTransaction.add(R.id.frame_layout, cardFragment);
////                fragmentTransaction.commit();
//
//
//              //  cardFragment.getView().setClickable(true);
//
//                CardScrollView cardScrollView =
//                        (CardScrollView) findViewById(R.id.card_scroll_view);
//                cardScrollView.setCardGravity(Gravity.BOTTOM);
//
//                cardScrollView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(MainActivity.this, RouteList.class);
//                        startActivity(i);
//                    }
//                });

//                cardFragment.getView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////
//                    }
//                });

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


    // });


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

}
