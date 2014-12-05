package com.tarks.transport.db;

import android.provider.BaseColumns;

/**
 * Created by JHRunning on 11/15/14.
 */
public class DataBases {


    public static final class CreateDB implements BaseColumns {
        public static final String _TABLENAME = "stations";
        public static final String COUNTRY_SRL = "country_srl";
        public static final String ROUTE_SRL = "route_srl";
        public static final String STATION_SRL = "station_srl";
        public static final String WAY_SRL = "way_srl";
        public static final String STATION_NAME = "station_name";
        public static final String STATION_LATITUDE = "station_latitude";
        public static final String STATION_LONGITUDE = "station_longitude";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +COUNTRY_SRL+" int not null , "
                        +ROUTE_SRL+" int not null , "
                        +STATION_SRL+" int not null , "
                        +WAY_SRL+" int not null , "
                        +STATION_NAME+" text not null , "
                        +STATION_LATITUDE+" double not null , "
                        +STATION_LONGITUDE+" double not null);";
    }

}
