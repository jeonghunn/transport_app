package com.tarks.transport.core;

/**
 * Created by JHRunning on 11/16/14.
 */

public class BusNotiClass {
    public int kind;
    public int noti_id;
    public String title;
    public String content;
    public int country_srl;
    public int route_srl;
    public int way_srl;
    public int station_srl;




    public BusNotiClass(){}

    public BusNotiClass(int _kind, int _noti_id, String _title, String _content, int _country_srl, int _route_srl, int _way_srl, int _station_srl){
        this.kind = _kind;
        this.noti_id = _noti_id;
        this.title = _title;
        this.content = _content;
        this.country_srl = _country_srl;
        this.route_srl = _route_srl;
        this.way_srl = _way_srl;
        this.station_srl = _station_srl;


    }

}
