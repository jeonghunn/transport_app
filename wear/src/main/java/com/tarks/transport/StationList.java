package com.tarks.transport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.core.global;

import java.util.concurrent.TimeUnit;

public class StationList extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = {"부평구청역", "삭은다리", "대우자동차(동문)", "갈산역", "갈산시장", "삼산사거리"};
    private MyAsyncTask myAsyncTask;
    Context ct;
    WearableListView listView;
    private int now_station = 0;


    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Intent i = new Intent(StationList.this, almost.class);
        startActivity(i);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }


    public class MyAsyncTask extends AsyncTask<String, String, String> {

    //    String[] elements2 = {"부평구청역", "삭은다리", "대우자동차(동문)", "갈산역", "갈산시장", "삼산사거리"};

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            GoogleApiClient client = new GoogleApiClient.Builder(ct)
                    .addApi(Wearable.API)
                    .build();
            client.blockingConnect(10000, TimeUnit.MILLISECONDS);
            Wearable.MessageApi.addListener(client, new MessageApi.MessageListener() {
                @Override
                public void onMessageReceived(MessageEvent messageEvent) {
//                   Toast.makeText(ct, elements2[Integer.parseInt(messageEvent.getPath().substring(0,1))], Toast.LENGTH_SHORT).show();
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.obj = messageEvent.getPath();
                    // msg.arg1 = DataContent;
                    mHandler.sendMessage(msg);

                }
            });
            client.disconnect();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // Log.i("called", "asdf");

            if (result != null) {



            }


        }
    }

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
         //   hideProgressBar();
            // IF Sucessfull no timeout

            if (msg.what == -1) {
         //       Global.ConnectionError(PageActivity.this);
            }

            if (msg.what == 1) {
            listView.smoothScrollToPosition(Integer.parseInt(msg.obj.toString()));
                now_station = Integer.parseInt(msg.obj.toString());
             listView.getAdapter().notifyDataSetChanged();


            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ct = this;

        // Get the list component from the layout of the activity
        listView =
                (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new ListAdapter(this, elements));
        // Set a click listener
        listView.setClickListener(this);
        MyAsyncTask t = new MyAsyncTask();
        t.execute();



    }


    public final class ListAdapter extends WearableListView.Adapter {
        private String[] mDataset;
        private final Context mContext;
        private final LayoutInflater mInflater;
        private int selected_pos;

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListAdapter(Context context, String[] dataset) {
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
            view.setText(mDataset[position]);

           if(position == now_station) {
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
            return mDataset.length;
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



}


