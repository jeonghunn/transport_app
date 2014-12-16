package com.tarks.transport.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.tarks.transport.R;

import java.util.concurrent.TimeUnit;

public class List extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = {"부평구청역", "삭은다리", "대우자동차(동문)", "갈산역", "갈산시장", "삼산사거리"};
    private MyAsyncTask myAsyncTask;
    Context ct;
    WearableListView listView;

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

    }

    @Override
    public void onTopEmptyRegionClick() {

    }


    public class MyAsyncTask extends AsyncTask<String, String, String> {

        String[] elements2 = {"부평구청역", "삭은다리", "대우자동차(동문)", "갈산역", "갈산시장", "삼산사거리"};

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
                    Toast.makeText(ct, elements2[Integer.parseInt(messageEvent.getPath().substring(0,1))], Toast.LENGTH_SHORT).show();
                }
            });
            client.disconnect();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {


            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listhub);
        ct = this;


        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override public void onLayoutInflated(WatchViewStub stub) {
                // Now you can access your views

                // Get the list component from the layout of the activity
                listView =
                        (WearableListView) findViewById(R.id.wearable_list);

                // Assign an adapter to the list
                listView.setAdapter(new ListAdapter(List.this, elements));
                // Set a click listener
                listView.setClickListener(List.this);

            }
        });



        MyAsyncTask t = new MyAsyncTask();
        t.execute();
    }

}

