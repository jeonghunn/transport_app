package com.tarks.transport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tarks.transport.db.DbOpenHelper;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // 자신의 신분 설정값을 불러옵니다.
        SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
        String frist_use = prefs.getString("frist_use_app", "true");

      if(frist_use.matches("true")){
DBInsert();

      }else{
startApp();
      }


    }

    public void DBInsert(){
        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.insertColumn(1,1,1,2, "천안시청", 36.8164823,127.1138453);
        mDbOpenHelper.insertColumn(1,1,2,2, "천안시청보건소",36.8142015,127.1152021);
        mDbOpenHelper.insertColumn(1,1,3,2, "불당동일하이빌", 36.8079758, 127.1134732);
        mDbOpenHelper.insertColumn(1,1,4,2, "불당대동다숲", 36.8056116, 127.1127508);
        mDbOpenHelper.insertColumn(1,1,5,2, "불당한성A", 36.803072, 127.112147);
        mDbOpenHelper.insertColumn(1,1,6,2, "천안교육지청", 36.8004375, 127.1100977);
        mDbOpenHelper.insertColumn(1,1,7,2, "천안북부상공회의소", 36.7985507, 127.1092666);
        mDbOpenHelper.insertColumn(1,1,8,2, "아산패션1광장", 36.796747, 127.1035927);
        mDbOpenHelper.insertColumn(1,1,9,2, "펜타포트", 36.7974116, 127.1014988);
        mDbOpenHelper.insertColumn(1,1,10,2, "서당골", 36.8004764, 127.1000929);
        mDbOpenHelper.insertColumn(1,1,11,2, "갤러리아백화점", 36.8015857, 127.1029344);
        mDbOpenHelper.insertColumn(1,1,12,2, "백석20통", 36.802279, 127.1081098);
        mDbOpenHelper.close();
        finishedfirstTask();
    startApp();
    }

    public void startApp(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,TestActivty.class));
                finish();
            }
        }, (long) (900));
    }

    private void finishedfirstTask(){
        // Setting Editor
        SharedPreferences edit = getSharedPreferences("setting",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = edit.edit();
        editor.putString("frist_use_app", "false");
        editor.commit();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
}
