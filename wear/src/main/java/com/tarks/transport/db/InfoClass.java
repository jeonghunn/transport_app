package com.tarks.transport.db;

/**
 * Created by JHRunning on 11/16/14.
 */

public class InfoClass {
    public int id;
    public int id_srl;
    public int country_srl;
    public int route_srl;
    public int way_srl;
    public int station_srl;
    public String station_name;
    public Double station_latitude;
    public Double station_longitude;


    public InfoClass(){}

    public InfoClass(int _id,int _id_srl, int _country_srl, int _route_srl, int _way_srl, int _station_srl, String _station_name, Double _station_latitude, Double _station_longitude){
        this.id = _id;
        this.id_srl = _id_srl;
        this.country_srl = _country_srl;
        this.route_srl = _route_srl;
        this.way_srl = _way_srl;
        this.station_srl = _station_srl;
        this.station_name = _station_name;
        this.station_latitude = _station_latitude;
        this.station_longitude = _station_longitude;
    }

}
