package com.tarks.transport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarks.transport.core.CoreSystem;
import com.tarks.transport.core.connect.AsyncHttpTask;
import com.tarks.transport.core.db.InfoClass;
import com.tarks.transport.core.global.global;
import com.tarks.transport.core.db.DbOpenHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    protected int i;
    private SharedPreferences prefs;
    private String db_ver;
    private TextView info;
    private String lastest_dbver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // 자신의 신분 설정값을 불러옵니다.
        prefs = getSharedPreferences("setting", MODE_PRIVATE);
        String frist_use = prefs.getString("first_use_app", "true");
         db_ver = prefs.getString("db_ver", "0");

        info = (TextView) findViewById(R.id.info);

        //Dev
       hiddenMenu();
//
//      if( !db_ver.matches(String.valueOf(DbOpenHelper.DATABASE_VERSION))){
//
//DBInsert();
//
//      }else{
//
////startApp();
//
//      }
        InfoDown();

       // InfoDown();

    }


    private void hiddenMenu(){
        ImageView hello = (ImageView)findViewById(R.id.busload);
        i=0;
        hello.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   global.log("adsfasfadsfkas;fjklasdjflksdjfkladsjklf");
                i += 1;
                if (i >= 7) {
                    global.setBooleanDev(MainActivity.this,"enabled",true);
                }
            }
        });
        if(!global.debug_mode){
            global.debug_mode = global.getBooleanDev(this,"enabled");
        }

    }

    public void InfoDown() {

        // get user_srl
        // 설정 값 불러오기
        // SharedPreferences prefs = getSharedPreferences("setting",
        // MODE_PRIVATE);


        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");


        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("bus_db_ver");


        new AsyncHttpTask(this, getString(R.string.server_api_url),
                mHandler,  Paramname, Paramvalue,  null, 1,0);

    }

    private void setInfotext(String text){
        info.setText(text);
    }

    public void DBDown() {


setInfotext(getString(R.string.db_downloading));

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");


        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("bus_db");


        new AsyncHttpTask(this, getString(R.string.server_api_url),
                mHandler,  Paramname, Paramvalue,  null, 2,0);

    }

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout


            global.log("==========================================================");
//			if (msg.what != 0) {
//				BreakTimeout();
//			}

            if (msg.what == -1) {
                //	BreakTimeout();
                global.toast(getString(R.string.networkerrord));
             startApp();

            }


            if (msg.what == 1) {

                try{
                    lastest_dbver = msg.obj.toString();
                    global.log(lastest_dbver);
                if(!db_ver.matches(lastest_dbver)){
                    setInfotext(getString(R.string.new_bus_db_available));
                    DBDown();

                }else{
                    startApp();
                }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            if (msg.what == 2) {

                try{
                    String result = msg.obj.toString();
                  //  global.log(result);
                    setInfotext(getString(R.string.db_installing));
                    DBInsertAsyncTask dbinsert = new DBInsertAsyncTask(mHandler);
                    dbinsert.execute(lastest_dbver, result);

                } catch (Exception e){
                    e.printStackTrace();
                }

            }


            if (msg.what == 3) {
                startApp();
            }



        }
    };


    public void DBInsert(String ver, String data){

        ArrayList<InfoClass> infoArraylist = new ArrayList<InfoClass>();
        infoArraylist = global.getJSONArrayListByInfoClass(data);



        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        SaveNewVersion(mDbOpenHelper, ver);
mDbOpenHelper.beginTransaction();
        for (int i = 0; i < infoArraylist.size(); i++) {
            // global.log(infoArraylist.get(i).station_name);
            InfoClass get = infoArraylist.get(i);

            mDbOpenHelper.insertColumn(get.country_srl,get.route, get.station_srl,get.way_srl, get.station_name, get.station_latitude,  get.station_longitude);
        }
mDbOpenHelper.TransactionFinish();
        mDbOpenHelper.close();



    }

    public class DBInsertAsyncTask extends AsyncTask<String, String, String> {
        private Handler handler;

        public DBInsertAsyncTask(Handler handler) {
            this.handler = handler;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            DBInsert(params[0], params[1]);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Message msg = handler.obtainMessage();
            msg.what = 3;
            msg.obj = result;
            handler.sendMessage(msg);


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }




    public void startApp(){
        startActivity(new Intent(MainActivity.this,main.class));
        startService(new Intent(MainActivity.this, CoreSystem.class));
        finish();
        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,main.class));
                startService(new Intent(MainActivity.this, CoreSystem.class));
                finish();
            }
        }, (long) (900));
        */
    }

    private void SaveNewVersion(DbOpenHelper dp, String ver){

      //  global.SaveSetting(this, "first_use_app", "false");
        global.SaveSetting(this, "db_ver", ver);
      dp.ResetStationsDB();

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
