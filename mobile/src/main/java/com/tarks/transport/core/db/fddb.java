package com.tarks.transport.core.db;

import android.provider.BaseColumns;

/**
 * Created by JHRunning on 11/15/14.
 */
public class fddb {


    public static final class CreateDB implements BaseColumns {
        public static final String _TABLENAME = "flow";
        public static final String COUNT_SRL = "count_srl";
        public static final String ACTION_SRL = "action_srl";
        public static final String ID_SRL = "id_srl";
        public static final String COUNTRY_SRL = "country_srl";
        public static final String ROUTE = "route";
        public static final String STATION_SRL = "station_srl";
        public static final String WAY_SRL = "way_srl";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String STATION_LATITUDE = "station_latitude";
        public static final String STATION_LONGITUDE = "station_longitude";
        public static final String TIME = "time";
        public static final String LOCATION_MODE = "location_mode";
        public static final String LOCATION_LEVEL = "location_level";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +COUNT_SRL+" int not null , "
                        +ACTION_SRL+" int not null , "
                        +ID_SRL+" int not null , "
                        +COUNTRY_SRL+" int not null , "
                        +ROUTE+" text not null , "
                        +STATION_SRL+" int not null , "
                        +WAY_SRL+" int not null , "
                        +LATITUDE+" double not null , "
                        +LONGITUDE+" double not null , "
                        +STATION_LATITUDE+" double not null , "
                        +STATION_LONGITUDE+" double not null, "
                        +TIME+" int not null , "
                        +LOCATION_MODE+" int not null , "
                        +LOCATION_LEVEL+" int not null);";
    }

}
