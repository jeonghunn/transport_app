package com.tarks.transport.db;

/**
 * Created by JHRunning on 11/16/14.
 */

public class InfoClass {
    public int user_srl;
    public String profile_update;
    public String profile_pic;

    public InfoClass(){}

    public InfoClass(int _user_srl , String profile_update , String profile_pic){
        this.user_srl = _user_srl;
        this.profile_update = profile_update;
        this.profile_pic = profile_pic;
    }

}
