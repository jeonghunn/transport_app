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



        mDbOpenHelper.insertColumn(1,1,1,1, "쌍용고입구", 36.790517, 127.116706);
        mDbOpenHelper.insertColumn(1,1,2,1, "월봉대우A",  36.795300, 127.114736);
        mDbOpenHelper.insertColumn(1,1,3,1, "월봉벽산A", 36.798672, 127.115099);
        mDbOpenHelper.insertColumn(1,1,4,1, "불당 한성A",  36.801750, 127.114672);
        mDbOpenHelper.insertColumn(1,1,5,1, "월봉고등학교", 36.8020733, 127.1072477);
        mDbOpenHelper.insertColumn(1,1,6,1, "갤러리아백화점", 36.801495, 127.104141);
        mDbOpenHelper.insertColumn(1,1,7,1, "펜타포트",  36.797163, 127.100359);
        mDbOpenHelper.insertColumn(1,1,8,1, "아산패션1광장",  36.796265, 127.102140);
        mDbOpenHelper.insertColumn(1,1,9,1, "충남북부상공회의소",  36.798140, 127.109394);
        mDbOpenHelper.insertColumn(1,1,10,1, "불당한성A",  36.802947, 127.112450);
        mDbOpenHelper.insertColumn(1,1,11,1, "불당대동다숲", 36.805122, 127.113161);
        mDbOpenHelper.insertColumn(1,1,12,1, "불당동일하이빌",  36.807842, 127.113964);
        mDbOpenHelper.insertColumn(1,1,13,1, "천안시청 서북구보건소",  36.813976, 127.115818);
        mDbOpenHelper.insertColumn(1,1,14,1, "천안시청", 36.8162684, 127.1146088);


        //반대방향

        mDbOpenHelper.insertColumn(1,1,1,2, "천안시청", 36.8162329,127.1147883);
        mDbOpenHelper.insertColumn(1,1,2,2, "천안시청보건소",36.8139802, 127.1150799);
        mDbOpenHelper.insertColumn(1,1,3,2, "불당동일하이빌",36.8079537,127.1136078);
        mDbOpenHelper.insertColumn(1,1,4,2, "불당대동다숲", 36.80500346879021,127.1128689198951);
        mDbOpenHelper.insertColumn(1,1,5,2, "불당한성A", 36.803072, 127.112147);
        mDbOpenHelper.insertColumn(1,1,6,2, "천안교육지청",   36.800351, 127.110834);
        mDbOpenHelper.insertColumn(1,1,7,2, "천안북부상공회의소", 36.798246, 127.109054);
        mDbOpenHelper.insertColumn(1,1,8,2, "아산패션1광장", 36.796593, 127.102066);
        mDbOpenHelper.insertColumn(1,1,9,2, "펜타포트",   36.797324, 127.100859);
        mDbOpenHelper.insertColumn(1,1,10,2, "서당골", 36.7989987,127.1008625);
        mDbOpenHelper.insertColumn(1,1,11,2, "갤러리아백화점",  36.801047, 127.104215);
        mDbOpenHelper.insertColumn(1,1,12,2, "불당동문화카페거리",36.8017013,127.106574);
        mDbOpenHelper.insertColumn(1,1,13,2, "월봉중학교", 36.801705, 127.111526);
        mDbOpenHelper.insertColumn(1,1,14,2, "한화꿈에그린A", 36.8014789,127.1129834);
        mDbOpenHelper.insertColumn(1,1,15,2, "쌍용도서관", 36.8002801,127.1149504);
        mDbOpenHelper.insertColumn(1,1,16,2, "월봉벽산A", 36.7982145,127.1148165);
        mDbOpenHelper.insertColumn(1,1,17,2, "월봉5차현대",36.7952017,127.1147494);
        mDbOpenHelper.insertColumn(1,1,18,2, "쌍용고입구",  36.790258, 127.116989);


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
