package com.tarks.transport.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.R;
import com.tarks.transport.core.WayClass;
import com.tarks.transport.core.global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WayList extends Activity
        implements WearableListView.ClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Sample dataset for the list
    String[] elements = { "동춘동종점 방향" , "무지개아파트 방향"};

    GoogleApiClient mGoogleApiClient;
    ProgressBar ps;
    MessageReceiver messageReceiver;

    ArrayList<WayClass> wayarray = new ArrayList<WayClass>();

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
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //unregister our receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
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
    int country_srl = intent.getIntExtra("country_srl", 0); // 인텐트로 부터 데이터 가져오고

    JSONArray jsonArrayList = new JSONArray();   // JSONArray 생성
    JSONObject obj = new JSONObject();
    try {

        obj.put("country_srl", country_srl);
        obj.put("route_srl", route_srl);
    }catch (Exception e){
        e.printStackTrace();
    }

    jsonArrayList.put(obj);

    global.log(jsonArrayList.toString());
    new SendMessage("getWays", jsonArrayList.toString() ).start();


    // Register a local broadcast receiver, defined is Step 3.
    IntentFilter messageFilter = new IntentFilter("get-ways-by-route");
    messageReceiver = new MessageReceiver();
    LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);


    // Get the list component from the layout of the activity

        }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            String message = intent.getStringExtra("message");

            global.log( message);

            //   if(routes == null) {

            wayarray = global.getJSONArrayListByWayClass(message);

            // Get the list component from the layout of the activity
            WearableListView listView =
                    (WearableListView) findViewById(R.id.wearable_list);

            // Assign an adapter to the list
            listView.setAdapter(new ListAdapter(WayList.this, wayarray));

            // Set a click listener
            listView.setClickListener(WayList.this);

            ps.setVisibility(View.INVISIBLE);



            //  }

        }
    }


    public final class ListAdapter extends WearableListView.Adapter {
        private ArrayList<WayClass> mDataset;
        private final Context mContext;
        private final LayoutInflater mInflater;
        private int selected_pos;

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListAdapter(Context context, ArrayList<WayClass> dataset) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDataset = dataset;
        }

        // Provide a reference to the type of views you're using
        public class ItemViewHolder extends WearableListView.ViewHolder {
            private TextView textView;
            private ImageView img;

            public ItemViewHolder(View itemView) {
                super(itemView);
                // find the text view within the custom item's layout
                textView = (TextView) itemView.findViewById(R.id.name);
                img = (ImageView) itemView.findViewById(R.id.circle);

            }

            public void Sizebig() {
                textView.setTextSize(21);
            }
        }


        // Create new views for list items
        // (invoked by the WearableListView's layout manager)
        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            // Inflate our custom layout for list items
            return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
        }

        // Replace the contents of a list item
        // Instead of creating new views, the list tries to recycle existing ones
        // (invoked by the WearableListView's layout manager)
        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder,
                                     int position) {
            // retrieve the text view
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            TextView view = itemHolder.textView;
            ImageView img = itemHolder.img;
            // replace text contents
            view.setText(mDataset.get(position).station_name + " " + getString(R.string.direction));
            img.setImageResource(R.drawable.arrow_right_red);

            view.setTextSize(17);
//            if (position == now_station) {
//                view.setTextSize(21);
//                view.setTypeface(null, Typeface.BOLD);
//            } else {
//
//                view.setTypeface(null, Typeface.NORMAL);
//            }

//       if(int) view.setTextSize(21);
            //view.isFocusable()
            // replace list item's metadata
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

    }



    };