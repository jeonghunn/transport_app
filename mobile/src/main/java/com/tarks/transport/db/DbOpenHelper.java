package com.tarks.transport.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JHRunning on 11/16/14.
 */
public class DbOpenHelper {

    private static final String DATABASE_NAME = "transport.db";
    private static final int DATABASE_VERSION = 2;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        // 생성자
        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn(int country_srl ,int route_srl, int station_srl, int way_srl,  String station_name, Double station_latitude, Double station_longitude){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.COUNTRY_SRL, country_srl);
        values.put(DataBases.CreateDB.ROUTE_SRL, route_srl);
        values.put(DataBases.CreateDB.STATION_SRL, station_srl);
        values.put(DataBases.CreateDB.WAY_SRL, way_srl);
        values.put(DataBases.CreateDB.STATION_NAME, station_name);
        values.put(DataBases.CreateDB.STATION_LATITUDE, station_latitude);
        values.put(DataBases.CreateDB.STATION_LONGITUDE, station_longitude);

        Log.i("asdf",station_latitude  +"Fdf");

        return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);


    }

    // Update DB
//    public boolean updateColumn(String user_srl, String profile_update, String profile_update_thumbnail, String profile_pic){
//        ContentValues values = new ContentValues();
//        values.put(DataBases.CreateDB.USER_SRL, user_srl);
//        values.put(DataBases.CreateDB.PROFILE_UPDATE, profile_update);
//        values.put(DataBases.CreateDB.PROFILE_UPDATE_THUMBNAIL, profile_update_thumbnail);
//        values.put(DataBases.CreateDB.PROFILE_PIC, profile_pic);
//        return mDB.update(DataBases.CreateDB._TABLENAME, values, "user_srl="+user_srl, null) > 0;
//    }

    // Update Profile update DB
//    public boolean updateProfileUpdate(String user_srl, String profile_update, String profile_pic){
//        ContentValues values = new ContentValues();
//        values.put(DataBases.CreateDB.USER_SRL, user_srl);
//        values.put(DataBases.CreateDB.PROFILE_UPDATE, profile_update);
//        values.put(DataBases.CreateDB.PROFILE_PIC, profile_pic);
//        return mDB.update(DataBases.CreateDB._TABLENAME, values, "user_srl="+user_srl, null) > 0;
//    }

    // Update Profile update DB
//    public boolean updateProfileUpdateThumbnail(String user_srl, String profile_update_thumbnail, String profile_pic){
//        ContentValues values = new ContentValues();
//        values.put(DataBases.CreateDB.USER_SRL, user_srl);
//        values.put(DataBases.CreateDB.PROFILE_UPDATE_THUMBNAIL, profile_update_thumbnail);
//        values.put(DataBases.CreateDB.PROFILE_PIC, profile_pic);
//        return mDB.update(DataBases.CreateDB._TABLENAME, values, "user_srl="+user_srl, null) > 0;
//    }

    // Delete ID
//    public boolean deleteColumn(long id){
//        return mDB.delete(DataBases.CreateDB._TABLENAME, "_id="+id, null) > 0;
//    }
//
//    // Delete Contact
//    public boolean deleteColumn(String number){
//        return mDB.delete(DataBases.CreateDB._TABLENAME, "contact="+number, null) > 0;
//    }

    // Select All
    public Cursor getAllColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
    }

    // ID 컬럼 얻어 오기
    public Cursor getColumn(long id){
        Cursor c = mDB.query(DataBases.CreateDB._TABLENAME, null,
                "_id="+id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // ID 컬럼 얻어 오기
    public Cursor getUserInfo(String user_srl){
        Cursor c = mDB.query(DataBases.CreateDB._TABLENAME, null,
                "user_srl="+user_srl, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // 이름 검색 하기 (rawQuery)
    public Cursor getUser(String user_srl){
        Cursor c = mDB.rawQuery( "select * from users where user_srl=" + "'" + user_srl + "'" , null);
        return c;
    }

    // 이름 검색 하기 (rawQuery)
    public Cursor getNearStation(Double latitude, Double longitude){
        Double max_latitude = latitude + 0.002;
        Double min_latitude = latitude - 0.002;
        Double max_longitude = longitude + 0.002;
        Double min_longitude = longitude - 0.002;
        Cursor c = mDB.rawQuery( "select * from stations where station_latitude > '" +  min_latitude + "' AND station_latitude < '" + max_latitude + "' AND station_longitude > '" + min_longitude + "' AND station_longitude < '" + max_longitude + "'" , null);
        return c;
    }


    // ID 컬럼 얻어 오기
//    public Cursor getNearStation(Double latitude, Double longitude){
//        Cursor c = mDB.query(DataBases.CreateDB._TABLENAME, null,
//                "user_srl="+user_srl, null, null, null, null);
//        if(c != null && c.getCount() != 0)
//            c.moveToFirst();
//        return c;
//    }

}





