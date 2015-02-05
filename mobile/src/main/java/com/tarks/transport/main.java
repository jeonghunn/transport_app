package com.tarks.transport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tarks.transport.ui.info;

/**
 * Created by panda on 2014-10-25.
 */
public class main extends ActionBarActivity{





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layouttest);
       // cs = new CoreSystem();






//



      // retrieveDeviceNode();



    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item;



        MenuItemCompat.setShowAsAction(	menu.add(0, 1, 0, getString(R.string.app_info)), MenuItemCompat.SHOW_AS_ACTION_NEVER);



        // item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
        // item.setIcon(R.drawable.ic_menu_add_bookmark);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if (item.getItemId() == 1) {
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
