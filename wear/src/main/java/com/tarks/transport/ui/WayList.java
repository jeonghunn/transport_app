package com.tarks.transport.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import com.tarks.transport.R;

public class WayList extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = { "동춘동종점 방향" , "무지개아파트 방향"};

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