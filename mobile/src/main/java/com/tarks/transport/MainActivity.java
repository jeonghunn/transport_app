package com.tarks.transport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tarks.transport.core.CoreSystem;
import com.tarks.transport.core.global;
import com.tarks.transport.db.DbOpenHelper;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    protected int i;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // 자신의 신분 설정값을 불러옵니다.
        prefs = getSharedPreferences("setting", MODE_PRIVATE);
        String frist_use = prefs.getString("first_use_app", "true");
        String db_ver = prefs.getString("db_ver", "0");

        //Dev
       hiddenMenu();

      if( !db_ver.matches(String.valueOf(DbOpenHelper.DATABASE_VERSION))){

DBInsert();

      }else{

startApp();
      }


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



        mDbOpenHelper.insertColumn(1,1,1,1, "쌍용고입구", 36.79051255938356,  127.11670571250683);
        mDbOpenHelper.insertColumn(1,1,2,1, "월봉대우A",  36.79520471744649, 127.11479125873149);
        mDbOpenHelper.insertColumn(1,1,3,1, "월봉벽산A", 36.798592555369275, 127.11514927133419);
        mDbOpenHelper.insertColumn(1,1,4,1, "불당 한성A",  36.80183714989931, 127.11453782816889);
        mDbOpenHelper.insertColumn(1,1,5,1, "월봉고등학교", 36.80216816714903,  127.10942412542681);
        mDbOpenHelper.insertColumn(1,1,6,1, "갤러리아백화점", 36.801495, 127.104141);
        mDbOpenHelper.insertColumn(1,1,7,1, "펜타포트",  36.797163, 127.100359);
        mDbOpenHelper.insertColumn(1,1,8,1, "아산패션1광장",  36.796265, 127.102140);
        mDbOpenHelper.insertColumn(1,1,9,1, "충남북부상공회의소",  36.797899820869,  127.10918163034225);
        mDbOpenHelper.insertColumn(1,1,10,1, "불당한성A",  36.80279422074784, 127.1125558616927);
        mDbOpenHelper.insertColumn(1,1,11,1, "불당대동다숲", 36.80555984739637, 127.11338355075031);
        mDbOpenHelper.insertColumn(1,1,12,1, "불당동일하이빌",  36.80778497844565, 127.11402559238209);
        mDbOpenHelper.insertColumn(1,1,13,1, "천안시청 서북구보건소",  36.81390982473249,  127.11589283335526);
        mDbOpenHelper.insertColumn(1,1,14,1, "천안시청", 36.81632424179095,  127.11460872282024);


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

        //14번

        mDbOpenHelper.insertColumn(1,14,1,1, "종합터미널", 36.81905934468649,  127.15563845810532);
        mDbOpenHelper.insertColumn(1,14,2,1, "신부청광A", 36.820978657021044,  127.15219580813906);
        mDbOpenHelper.insertColumn(1,14,3,1, "동아태조아파트", 36.82266769980785,  127.15261386715243);
        mDbOpenHelper.insertColumn(1,14,4,1, "역말오거리", 36.826113774157065, 127.1531307200375);
        mDbOpenHelper.insertColumn(1,14,5,1, "이안더센트럴아파트", 36.82896652921417, 127.15254791087818);
        mDbOpenHelper.insertColumn(1,14,6,1, "두정역푸르지오", 36.83235696446898,  127.15076666211763);
        mDbOpenHelper.insertColumn(1,14,7,1, "두정역",  36.833050637604074, 127.14731534029511);
        mDbOpenHelper.insertColumn(1,14,8,1, "한성3차A",36.834787267053905, 127.14569318217076);
        mDbOpenHelper.insertColumn(1,14,9,1, "두정우남A", 36.83801762998045,  127.14577778420514);
        mDbOpenHelper.insertColumn(1,14,10,1, "한기대(두정)", 36.83924299078902, 127.14217580080846);
        mDbOpenHelper.insertColumn(1,14,11,1, "부성2동주민센터,두정계룡A", 36.83750872144176,  127.13804706721888);
        mDbOpenHelper.insertColumn(1,14,12,1, "두정도서관", 36.83390292832935,  127.13531652456335);
        mDbOpenHelper.insertColumn(1,14,13,1, "두정고등학교", 36.83394665333928,  127.13249725065131);
        mDbOpenHelper.insertColumn(1,14,14,1, "두정대우5차A", 36.83403604372322, 127.1290558868368);
        mDbOpenHelper.insertColumn(1,14,15,1, "계룡리슈빌", 36.832487611626064, 127.12342590041854);
        mDbOpenHelper.insertColumn(1,14,16,1, "백석푸르지오", 36.830536321064564, 127.12380389377864);
        mDbOpenHelper.insertColumn(1,14,17,1, "두정세광A", 36.82937929626617,  127.12722092014029);
        mDbOpenHelper.insertColumn(1,14,18,1, "두정부경A", 36.82691481248818, 127.12717760797821);
        mDbOpenHelper.insertColumn(1,14,19,1, "한방병원", 36.824528114887016,  127.12601355053656);
        mDbOpenHelper.insertColumn(1,14,20,1, "그린빌A", 36.82346089884094, 127.12110242751308);
        mDbOpenHelper.insertColumn(1,14,21,1, "백석초등학교", 36.82054226576798,  127.12015634091328);
        mDbOpenHelper.insertColumn(1,14,22,1, "주공그린빌3차아파트", 36.81910990007103,  127.11976182249755);
        mDbOpenHelper.insertColumn(1,14,23,1, "백석11단지", 36.81870181624591, 127.11783902822859);
        mDbOpenHelper.insertColumn(1,14,24,1, "천안시청 서북구보건소", 36.814084938710245,  127.11558601872773);
        mDbOpenHelper.insertColumn(1,14,25,1, "트윈팰리스",  36.81170919398989, 127.11225960061137);
        mDbOpenHelper.insertColumn(1,14,26,1, "불당아이파크", 36.80873247669883, 127.11070876206753);
        mDbOpenHelper.insertColumn(1,14,27,1, "불당대동108동앞",  36.806433323566765, 127.11211742490335);
        mDbOpenHelper.insertColumn(1,14,28,1, "불당대원A", 36.80478889963442,  127.11669263506853);
        mDbOpenHelper.insertColumn(1,14,29,1, "쌍용3동주민센터", 36.80306571570392, 127.11877426996514);

        //반대
        mDbOpenHelper.insertColumn(1,14,1,2, "쌍용3동주민센터", 36.80313302449671, 127.11904891238021);
        mDbOpenHelper.insertColumn(1,14,2,2, "쌍용마을뜨란채",36.80449416002755, 127.11859157947404);
        mDbOpenHelper.insertColumn(1,14,3,2, "불당호반A",36.80555538075787, 127.11614470385437);
        mDbOpenHelper.insertColumn(1,14,4,2, "불당동일하이빌3단지", 36.80678072527221,  127.11161365311186);
        mDbOpenHelper.insertColumn(1,14,5,2, "불당동일하이빌2단지 ", 36.80847989959284, 127.11099416243687);
        mDbOpenHelper.insertColumn(1,14,6,2, "트윈팰리스",   36.81163772230328, 127.11160389307832);
        mDbOpenHelper.insertColumn(1,14,7,2, "천안시청보건소",36.81389991690382,  127.11588833553128);
        mDbOpenHelper.insertColumn(1,14,8,2, "백석호반1차아파트",36.818301564374664, 127.11890428238367);
        mDbOpenHelper.insertColumn(1,14,9,2, "백석호반2차아파트",36.81919253531256,  127.12002645987037);
        mDbOpenHelper.insertColumn(1,14,10,2, "그린빌A",36.82313824992404, 127.12115347851403);
        mDbOpenHelper.insertColumn(1,14,11,2, "두정부경A",36.82677224450245, 127.12735783848939);
        mDbOpenHelper.insertColumn(1,14,12,2, "두정세광A",36.829232204715225, 127.1274179630213);
        mDbOpenHelper.insertColumn(1,14,13,2, "백석푸르지오",36.830984385346994, 127.12359835706175);
        mDbOpenHelper.insertColumn(1,14,14,2, "백석아이파크",36.832471163438484, 127.12364558859986);
        mDbOpenHelper.insertColumn(1,14,15,2, "두정대우5차A",36.8338260788729, 127.12906226019669);
        mDbOpenHelper.insertColumn(1,14,16,2, "두정도서관",36.8336928929839,  127.1353845362591);
        mDbOpenHelper.insertColumn(1,14,17,2, "두정중학교",36.83532947118263, 127.13836937199855);
        mDbOpenHelper.insertColumn(1,14,18,2, "부성2동주민센터,두정경남A",36.837591165512315,  127.13844071004597);
        mDbOpenHelper.insertColumn(1,14,19,2, "두정대우3차A",36.83874915083778, 127.14370292454623);
        mDbOpenHelper.insertColumn(1,14,20,2, "두정우남A",36.83795398392874, 127.14550636384504);
        mDbOpenHelper.insertColumn(1,14,21,2, "주공8단지A",36.835115614958525, 127.14541130503017);
        mDbOpenHelper.insertColumn(1,14,22,2, "두정역",36.83270667943508, 127.14782809693216);
        mDbOpenHelper.insertColumn(1,14,23,2, "두정역입구",36.83137705531945, 127.15179379569238);
        mDbOpenHelper.insertColumn(1,14,24,2, "이안더센트럴아파트",36.828763291738156, 127.15222579806877);
        mDbOpenHelper.insertColumn(1,14,25,2, "역말오거리",36.82679835030096, 127.15263664210643);
        mDbOpenHelper.insertColumn(1,14,26,2, "신부우방A",36.82335622685553, 127.15255134539998);
        mDbOpenHelper.insertColumn(1,14,27,2, "신부청광A",36.82150953345438, 127.15209934693218);
        mDbOpenHelper.insertColumn(1,14,28,2, "종합터미널",36.81871211770526, 127.15586751735663);


        //인천

        mDbOpenHelper.insertColumn(2,30,1,1, "갈현동", 37.58193037571998, 126.72713326553604);
        mDbOpenHelper.insertColumn(2,30,2,1, "신동아APT", 37.57947388147346, 126.73227401220723);
        mDbOpenHelper.insertColumn(2,30,3,1, "장기동", 37.57861445704755, 126.73472238436146);
        mDbOpenHelper.insertColumn(2,30,4,1, "장기119", 37.57602494890269, 126.73329727391061);
        mDbOpenHelper.insertColumn(2,30,5,1, "다남식당", 37.57063407560347, 126.73002249410013);
        mDbOpenHelper.insertColumn(2,30,6,1, "계양역", 37.57057585505509, 126.73615226375787);
        mDbOpenHelper.insertColumn(2,30,7,1, "계양중", 37.570066127347495, 126.73991214146753);
        mDbOpenHelper.insertColumn(2,30,8,1, "귤현역", 37.56612933859696, 126.74221791128059);


        //반대
        mDbOpenHelper.insertColumn(2,30,1,2, "귤현역", 37.566179485712446, 126.74248939278544);
        mDbOpenHelper.insertColumn(2,30,2,2, "계양중", 37.57034127713906, 126.74006965850128);
        mDbOpenHelper.insertColumn(2,30,3,2, "계양역", 37.570908561651414, 126.73585677933298);
        mDbOpenHelper.insertColumn(2,30,4,2, "다남식당", 37.570687875805255, 126.72990910381135);
        mDbOpenHelper.insertColumn(2,30,5,2, "장기119", 37.57595774577468, 126.73346165950142);
        mDbOpenHelper.insertColumn(2,30,6,2, "장기동", 37.5779260824122, 126.73531689945989);
        mDbOpenHelper.insertColumn(2,30,7,2, "신동아APT", 37.579639542778246, 126.73202209361193);
        mDbOpenHelper.insertColumn(2,30,8,2, "갈현동", 37.581713642281, 126.72691895228147);

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

      //  global.SaveSetting(this, "first_use_app", "false");
        global.SaveSetting(this, "db_ver", String.valueOf(DbOpenHelper.DATABASE_VERSION));

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
