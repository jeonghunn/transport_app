package com.tarks.transport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tarks.transport.core.CoreSystem;
import com.tarks.transport.core.global;
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
        String frist_use = prefs.getString("first_use_app", "true");

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
        mDbOpenHelper.insertColumn(1,1,4,2, "불당대동다숲", 36.80500346879021,127.1128689198951);
        mDbOpenHelper.insertColumn(1,1,5,2, "불당한성A", 36.803072, 127.112147);
        mDbOpenHelper.insertColumn(1,1,6,2, "천안교육지청", 36.8004375, 127.1100977);
        mDbOpenHelper.insertColumn(1,1,7,2, "천안북부상공회의소", 36.7985507, 127.1092666);
        mDbOpenHelper.insertColumn(1,1,8,2, "아산패션1광장", 36.796747, 127.1035927);
        mDbOpenHelper.insertColumn(1,1,9,2, "펜타포트", 36.7974116, 127.1014988);
        mDbOpenHelper.insertColumn(1,1,10,2, "서당골", 36.8004764, 127.1000929);
        mDbOpenHelper.insertColumn(1,1,11,2, "갤러리아백화점", 36.8015857, 127.1029344);
        mDbOpenHelper.insertColumn(1,1,12,2, "불당동문화카페거리", 36.802279, 127.1081098);
        mDbOpenHelper.insertColumn(1,1,13,2, "월봉중학교", 36.8018939, 127.1130329);
        mDbOpenHelper.insertColumn(1,1,14,2, "한화꿈에그린A", 36.8014164,127.1103562);
        mDbOpenHelper.insertColumn(1,1,15,2, "쌍용도서관", 36.8004488, 127.1121659);
        mDbOpenHelper.insertColumn(1,1,16,2, "월봉벽산A", 36.7984795, 127.1136778);
        mDbOpenHelper.insertColumn(1,1,17,2, "월봉5차현대", 36.796316, 127.1137111);
        mDbOpenHelper.insertColumn(1,1,18,2, "쌍용고입구", 36.7918909,127.114036);
//        mDbOpenHelper.insertColumn(1,1,12,2, "삼일원앙", 36.7891173,127.1177261);
//        mDbOpenHelper.insertColumn(1,1,12,2, "쌍용역해누리아이파크", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "쌍용고가교", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "E마트", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "충무병원", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "다가동신성A", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "일봉초등학교", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "남부오거리", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "우성VIP A", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "천안여중입구", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "원성극동A", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "교보사거리", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "교보생명", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "중앙고등학교", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "법원검찰청", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "종합터미널", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "가구웨딩특화거리", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "다우리병원", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "백석종합상가", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "백석초등학교", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "주공그린빌3차아파트", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "백석11단지", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "천안시청", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "천안시청시의회", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "종합운동장", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "벽산블루밍A", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "브라운스톤A", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "한국더첼", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "미코씨엔씨", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "영진기계", 36.802279, 127.1081098);
//        mDbOpenHelper.insertColumn(1,1,12,2, "백석농공단지", 36.802279, 127.1081098);



        mDbOpenHelper.insertColumn(1,1,1,1, "쌍용고입구", 36.7907086, 127.1192201);
        mDbOpenHelper.insertColumn(1,1,2,1, "월봉대우A", 36.7954986, 127.117403);
        mDbOpenHelper.insertColumn(1,1,3,1, "월봉벽산A", 36.7987517, 127.1156213);
        mDbOpenHelper.insertColumn(1,1,4,1, "불당 한성A", 36.801894, 127.1158354);
        mDbOpenHelper.insertColumn(1,1,5,1, "월봉고등학교", 36.8022479, 127.111019);
        mDbOpenHelper.insertColumn(1,1,6,1, "갤러리아백화점", 36.8015877, 127.1035229);
        mDbOpenHelper.insertColumn(1,1,7,1, "펜타포트", 36.7979571, 127.1028635);
        mDbOpenHelper.insertColumn(1,1,8,1, "아산패션1광장", 36.7965266, 127.1037228);
        mDbOpenHelper.insertColumn(1,1,9,1, "충남북부상공회의소", 36.7980592, 127.1064724);
        mDbOpenHelper.insertColumn(1,1,10,1, "불당한성A", 36.8028656, 127.1124451);
        mDbOpenHelper.insertColumn(1,1,11,1, "불당대동다숲", 36.8056494, 127.1133111);
        mDbOpenHelper.insertColumn(1,1,12,1, "불당동일하이빌", 36.8079505, 127.1140226);
        mDbOpenHelper.insertColumn(1,1,13,1, "천안시청 서북구보건소", 36.8145837, 127.1163326);
        mDbOpenHelper.insertColumn(1,1,14,1, "천안시청", 36.8162684, 127.1146088);


        //반대방향


        mDbOpenHelper.close();
        finishedfirstTask();
    startApp();
    }

    public void startApp(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,main.class));
                startService(new Intent(MainActivity.this, CoreSystem.class));
                finish();
            }
        }, (long) (900));
    }

    private void finishedfirstTask(){

        global.SaveSetting(this, "first_use_app", "false");

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
