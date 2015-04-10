package com.tarks.transport.core.db;

/**
 * Created by JHRunning on 11/16/14.
 */

public class InfoClass {
    public int id;
    public int country_srl;
    public int route;
    public int way_srl;
    public int station_srl;
    public String station_name;
    public Double station_latitude;
    public Double station_longitude;


    public InfoClass(){}

    public InfoClass(int _id, int _country_srl , int _route , int _way_srl, int _station_srl, String _station_name, Double _station_latitude, Double _station_longitude){
        this.id = _id;
        this.country_srl = _country_srl;
        this.route = _route;
        this.way_srl = _way_srl;
        this.station_srl = _station_srl;
        this.station_name = _station_name;
        this.station_latitude = _station_latitude;
        this.station_longitude = _station_longitude;
    }

}
