package com.tarks.transport.core;

import android.content.Context;
import android.database.Cursor;

import com.tarks.transport.db.DbOpenHelper;

/**
 * Created by JHRunning on 11/20/14.
 */
public class flowCore {

public void initflowCore(Context cx){
    //Check this is frist
//    DbOpenHelper mDbOpenHelper = new DbOpenHelper(cx);
//    mDbOpenHelper.open();
//    Cursor csr = mDbOpenHelper.getCountColumn(global.getCountSrl(cx));
//
//
//        mDbOpenHelper.close();


    if(global.getDBCountSrl(cx) < global.getCountSrl(cx)){
        firstFlow();
    }else{
        conFlow();
    }

}

    public void firstFlow(){

    }

    public void  conFlow(){

    }

}
