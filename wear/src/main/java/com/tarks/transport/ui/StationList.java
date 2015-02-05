package com.tarks.transport.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.R;
import com.tarks.transport.core.global;
import com.tarks.transport.core.db.DbOpenHelper;
import com.tarks.transport.core.db.InfoClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationList extends Activity
        implements WearableListView.ClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Sample dataset for the list
   // String[][] elements = {{"4545","44545"},{""}};
   // private MyAsyncTask myAsyncTask;
    Context ct;
  //  WearableListView listView;
    private int country_srl = 0;
    private int route_srl = 0;
    private int way_srl = 0;
    private int station_srl = 0;
    GoogleApiClient mGoogleApiClient;
    private String nodeId;
    private ArrayList<InfoClass> stations;
    MessageReceiver messageReceiver;
    ProgressBar ps;
    WearableListView listView;

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
//        Intent i = new Intent(StationList.this, BusArrive.class);
//        startActivity(i);

        if(viewHolder.getPosition() >=  station_srl ) {
            new SendMessage("setDestination", String.valueOf(stations.get(viewHolder.getPosition()).id_srl)).start();
            finish();
            global.cancelNoti(this, 1);
            Intent intent = new Intent(this, ConfirmationActivity.class);
            intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    ConfirmationActivity.SUCCESS_ANIMATION);
            intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                    getString(R.string.set_to_destination));
            startActivity(intent);
        }else{
            global.toast(this, getString(R.string.missed_the_stop));
        }

      //  global.log(viewHolder.getPosition() + "asdfaf");
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


    public ArrayList<InfoClass> getStations(int country_srl, int route_srl, int way_srl) {
        // InfoClass mInfoClass;
        ArrayList<InfoClass> mInfoArray = new ArrayList<InfoClass>();


        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        Cursor csr = mDbOpenHelper.getStations(country_srl, route_srl, way_srl);


        while (csr.moveToNext()) {

            // global.log(csr.getString(csr.getColumnIndex("station_name")));

            InfoClass mInfoClass = new InfoClass(
                    csr.getInt(csr.getColumnIndex("_id")),
                    csr.getInt(csr.getColumnIndex("id_srl")),
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

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            boolean message = intent.getBooleanExtra("message", false);

            global.log("PAYBACK");
            //   if(routes == null) {
            // Get the list component from the layout of the activity




            stations = getStations(country_srl,route_srl,way_srl);
            // Assign an adapter to the list
            listView.setAdapter(new ListAdapter(StationList.this,stations));

            // Set a click listener
            listView.setClickListener(StationList.this);




            ps.setVisibility(View.INVISIBLE);

            global.log(station_srl + "station srl number");
            listView.scrollToPosition(station_srl);

            //  }

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listhub);
        ct = this;



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

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override public void onLayoutInflated(WatchViewStub stub) {
                // Now you can access your views
                // Get the list component from the layout of the activity

                ps = (ProgressBar) findViewById(R.id.progressBar);


                listView =
                        (WearableListView) findViewById(R.id.wearable_list);



            }
        });

        Intent intent = getIntent(); // 인텐트 받아오고

         country_srl = intent.getIntExtra("country_srl", 0);
         route_srl = intent.getIntExtra("route_srl", 0); // 인텐트로 부터 데이터 가져오고
         way_srl = intent.getIntExtra("way_srl", 0); // 인텐트로 부터 데이터 가져오고
         station_srl = intent.getIntExtra("station_srl", 0); // 인텐트로 부터 데이터 가져오고

        global.log(station_srl + "s srl number");
        JSONArray jsonArrayList = new JSONArray();   // JSONArray 생성
        JSONObject obj = new JSONObject();
        try {

            obj.put("country_srl", country_srl);
            obj.put("route_srl", route_srl);
            obj.put("way_srl", way_srl);
            obj.put("station_srl", station_srl);
        }catch (Exception e){
            e.printStackTrace();
        }

        jsonArrayList.put(obj);
        new SendMessage("checkDataBase",jsonArrayList.toString()).start();



        // Register a local broadcast receiver, defined is Step 3.
        IntentFilter messageFilter = new IntentFilter("Check-Data-Base");
        messageReceiver= new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

     //   String kind = intent.getStringExtra("kind");

     // if(intent != null)  global.log("stationlist" + kind +  country_srl + " : " + route_srl + ":" + way_srl);

        //Set now station




    }


    public final class ListAdapter extends WearableListView.Adapter {
        private  ArrayList<InfoClass> mDataset;
        private final Context mContext;
        private final LayoutInflater mInflater;
        private int selected_pos;

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListAdapter(Context context, ArrayList<InfoClass> dataset) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDataset = dataset;
        }

        // Provide a reference to the type of views you're using
        public class ItemViewHolder extends WearableListView.ViewHolder {
            private TextView textView;
            public ItemViewHolder(View itemView) {
                super(itemView);
                // find the text view within the custom item's layout
                textView = (TextView) itemView.findViewById(R.id.name);

            }

            public void Sizebig(){
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
            // replace text contents
            view.setText(mDataset.get(position).station_name);

           if(position == station_srl) {
               view.setTextSize(21);
               view.setTypeface(null,Typeface.BOLD);
           }else{
               view.setTextSize(16);
               view.setTypeface(null,Typeface.NORMAL);
           }

//       if(int) view.setTextSize(21);
            //view.isFocusable()
            // replace list item's metadata
            holder.itemView.setTag(position);
        }

        // Return the size of your dataset
        // (invoked by the WearableListView's layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        if (v == null) {
//            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = vi.inflate(R.layout.profile_list, null);
//        }
//        final List p = items.get(position);
//        if (p != null) {
//            TextView tt = (TextView) v.findViewById(R.id.titre);
//            TextView bt = (TextView) v.findViewById(R.id.description);
//            ImageView image = (ImageView) v.findViewById(R.id.img);
//
//            if (tt != null) {
//                tt.setText(p.getTitle());
//                // Log.i("test", p.getStatus() + "kk" +p.getTitle() );
//                // Status not public
//                if (p.getStatus() > 1) {
//                    tt.setTextColor(Color.GRAY);
//                } else {
//                    tt.setTextColor(Color.BLACK);
//                }
//
//            }
//
//
//        }
//        return v;
//    }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //unregister our receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);

    }

}


