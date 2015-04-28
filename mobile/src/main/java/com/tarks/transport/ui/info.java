package com.tarks.transport.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tarks.transport.R;
import com.tarks.transport.core.CoreSystem;
import com.tarks.transport.core.global.global;

import java.io.IOException;

/**
 * Created by JHRunning on 12/9/14.
 */
public class info extends ActionBarActivity {

    private Context c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item;
        c = this;


        MenuItemCompat.setShowAsAction(menu.add(0, 1, 0, getString(R.string.developers)), MenuItemCompat.SHOW_AS_ACTION_NEVER);

        if(global.getBooleanDev(this,"enabled")){
            MenuItemCompat.setShowAsAction(menu.add(0, 2, 0, "Hidden setting"), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        }

        // item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
        // item.setIcon(R.drawable.ic_menu_add_bookmark);

        return true;
    }

    //빽백키 상단액션바
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case 1:
                setContentView(R.layout.developers);
                return true;
            case 2:
                setContentView(R.layout.dev_menu);
                registerCheckboxListener();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void registerCheckboxListener(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.jibing);
        if(ll != null){
            int childcount = ll.getChildCount();
            for (int i=0; i < childcount; i++){
                View v = ll.getChildAt(i);
                if(v instanceof CheckBox){
                    final CheckBox cb = (CheckBox)v;
                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            global.setBooleanDev(c,buttonView.getTag().toString(),isChecked);
                        }
                    });
                    cb.setChecked(global.getBooleanDev(c,cb.getTag().toString()));
                }
                if(v.getId() == R.id.button){
                    final Button bt = (Button)v;
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            stopService(new Intent(info.this,CoreSystem.class));
                            try {
                                Runtime.getRuntime().exec("su -c killall com.tarks.transport");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

}
