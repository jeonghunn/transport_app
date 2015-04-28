package com.tarks.transport.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.tarks.transport.core.global.globalv;

/**
 * Created by JHRunning on 12/2/14.
 */
public class SensorListener implements SensorEventListener {



    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private Sensor accSensor;
    private int count_srl;

    int gyroX;
    int gyroY;
    int gyroZ;


    public void sensorStart(Context cx) {
        //센서 매니저 얻기
        mSensorManager = (SensorManager) cx.getSystemService(Context.SENSOR_SERVICE);
        //자이로스코프 센서(회전)
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //엑셀러로미터 센서(가속)
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        regSensor();
    }


    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {
        Sensor sensor = event.sensor;

//global.log(globalv.moving_now + "aaaaa");

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
           int x = Math.round(event.values[0] * 1000);
           int y = Math.round(event.values[1] * 1000);
           int z = Math.round(event.values[2] * 1000);

            //global.log("X : " + gyroX + " Y : " + gyroY + " Z : " + gyroZ);
            checkMoving(gyroX,gyroY,gyroZ, x, y, z);
        }
    }

    //정확도에 대한 메소드 호출 (사용안함)
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void checkMoving(int X, int Y, int Z, int nowx, int nowy, int nowz){
        int result = (X + Y + Z) - (nowx + nowy + nowz);


        if(count_srl > 20) {
            if (result <= 50 || result >= -50) globalv.moving_now = globalv.STOP_STATE;

            if (result >= 50 || result <= -50) globalv.moving_now = globalv.ACTIVE_STATE;


            gyroX = nowx;
            gyroY = nowy;
            gyroZ = nowz;

            count_srl = 0;
        }
        count_srl++;
    }

    // 주기 설명
    // SENSOR_DELAY_UI 갱신에 필요한 정도 주기
    // SENSOR_DELAY_NORMAL 화면 방향 전환 등의 일상적인  주기
    // SENSOR_DELAY_GAME 게임에 적합한 주기
    // SENSOR_DELAY_FASTEST 최대한의 빠른 주기


    //리스너 등록
    public void regSensor() {
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //리스너 해제
    public void uregSensor() {
        mSensorManager.unregisterListener(this);
    }
}