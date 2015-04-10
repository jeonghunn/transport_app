package com.tarks.transport.core;

/**
 * Created by JHRunning on 11/16/14.
 */

public class BusNotiClass {
    public int kind;
    public int noti_id;
    public String title;
    public String content;
    public String direction_name;
    public String station_summary;
    public int country_srl;
    public String route;
    public int way_srl;
    public int station_srl;




    public BusNotiClass(){}

    public BusNotiClass(int _kind, int _noti_id, String _title, String _content,  String _direction_name, String _StationSummary, int _country_srl, String _route, int _way_srl, int _station_srl){
        this.kind = _kind;
        this.noti_id = _noti_id;
        this.title = _title;
        this.content = _content;
        this.direction_name = _direction_name;
        this.station_summary = _StationSummary;
        this.country_srl = _country_srl;
        this.route = _route;
        this.way_srl = _way_srl;
        this.station_srl = _station_srl;


    }

}
