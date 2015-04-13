package com.tarks.transport.core;

/**
 * Created by JHRunning on 11/16/14.
 */

public class StationClass {
    public int country_srl;
    public String route;
    public int way_srl;
    public int station_srl;



    public StationClass(){}

    public StationClass(int _country_srl, int _route, int _way_srl, int _station_srl){
        this.country_srl = _country_srl;
        this.route = _route;
        this.way_srl = _way_srl;
        this.station_srl = _station_srl;

    }

}
