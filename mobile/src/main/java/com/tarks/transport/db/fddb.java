package com.tarks.transport.db;

import android.provider.BaseColumns;

/**
 * Created by JHRunning on 11/15/14.
 */
public class fddb {


    public static final class CreateDB implements BaseColumns {
        public static final String _TABLENAME = "flow";
        public static final String COUNT_SRL = "count_srl";
        public static final String COUNTRY_SRL = "country_srl";
        public static final String ROUTE_SRL = "route_srl";
        public static final String STATION_SRL = "station_srl";
        public static final String WAY_SRL = "way_srl";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LOCATION_LEVEL = "location_level";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +COUNT_SRL+" int not null , "
                        +COUNTRY_SRL+" int not null , "
                        +ROUTE_SRL+" int not null , "
                        +STATION_SRL+" int not null , "
                        +WAY_SRL+" int not null , "
                        +LATITUDE+" double not null , "
                        +LONGITUDE+" double not null , "
                        +LOCATION_LEVEL+" int not null);";
    }

}
