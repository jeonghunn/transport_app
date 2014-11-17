package com.tarks.transport.core;

/**
 * Created by JHRunning on 11/16/14.
 */

public class noticlass {
    public int kind;
    public int noti_id;
    public String title;
    public String content;



    public noticlass(){}

    public noticlass(int _kind, int _noti_id, String _title, String _content){
        this.kind = _kind;
        this.noti_id = _noti_id;
        this.title = _title;
        this.content = _content;

    }

}
