package com.tarks.transport.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.tarks.transport.R;

/**
 * Created by JHRunning on 12/9/14.
 */
public class info extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    //빽백키 상단액션바
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

}
