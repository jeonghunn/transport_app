package com.tarks.transport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tarks.transport.ui.MainAdapter;
import com.tarks.transport.ui.info;
import com.tarks.transport.ui.setting.setting;

/**
 * Created by panda on 2014-10-25.
 */
public class main extends ActionBarActivity{


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
       // cs = new CoreSystem();

        String names[] = { getString(R.string.wbus_on_android_wear)};


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainAdapter(names);
        mRecyclerView.setAdapter(mAdapter);


//



      // retrieveDeviceNode();



    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item;



        MenuItemCompat.setShowAsAction(	menu.add(0, 1, 0, getString(R.string.main_settings)), MenuItemCompat.SHOW_AS_ACTION_NEVER);


        MenuItemCompat.setShowAsAction(	menu.add(0, 2, 0, getString(R.string.app_info)), MenuItemCompat.SHOW_AS_ACTION_NEVER);



        // item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
        // item.setIcon(R.drawable.ic_menu_add_bookmark);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId() == 1){
            Intent intent1 = new Intent(main.this, setting.class);
            startActivity(intent1);
            return true;
        }


        if (item.getItemId() == 2) {
            Intent intent1 = new Intent(main.this, info.class);
        startActivity(intent1);
            return true;

        }


        return super.onOptionsItemSelected(item);
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
