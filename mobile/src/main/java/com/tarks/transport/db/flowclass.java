package com.tarks.transport.db;

/**
 * Created by JHRunning on 11/16/14.
 */

public class flowclass {
    public int id;
    public int count_srl;
    public int action_srl;
    public int id_srl;
    public int country_srl;
    public int route_srl;
    public int station_srl;
    public int way_srl;
    public Double latitude;
    public Double longitude;
    public Double station_latitude;
    public Double station_longitude;
    public long time;
    public int location_mode;
    public int location_level;
    public flowclass(){}

    public flowclass(int _id, int _count_srl, int _action_srl, int _id_srl, int _country_srl, int _route_srl,  int _station_srl, int _way_srl,  Double _latitude, Double _longitude,  Double _station_latitude, Double _station_longitude, long _time, int _location_mode, int _location_level){
        this.id = _id;
        this.count_srl = _count_srl;
        this.action_srl = _action_srl;
        this.id_srl = _id_srl;
        this.country_srl = _country_srl;
        this.route_srl = _route_srl;
        this.station_srl = _station_srl;
        this.way_srl = _way_srl;
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.station_latitude = _station_latitude;
        this.station_longitude = _station_longitude;
        this.time = _time;
        this.location_mode = _location_mode;
        this.location_level = _location_level;
    }

}
