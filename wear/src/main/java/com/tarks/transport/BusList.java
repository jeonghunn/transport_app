package com.tarks.transport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

public class BusList extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = { "34" , "721", "555" };

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

   //     if(viewHolder.getItemId() == 0){
            Intent i = new Intent(BusList.this, WayList.class);
            startActivity(i);
     //   }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }



@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        // Get the list component from the layout of the activity
        WearableListView listView =
        (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new ListAdapter(this, elements));

        // Set a click listener
        listView.setClickListener(this);
        }

};