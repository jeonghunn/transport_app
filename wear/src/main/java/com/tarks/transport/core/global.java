package com.tarks.transport.core;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by JHRunning on 11/14/14.
 */
public final class global {

    static ModApplication mod = ModApplication.getInstance();
    static boolean debug_mode = true;

    public static void toast(String str, boolean length) {
        // Log.i("ACCESS", "I can access to toast");
        Toast.makeText(mod, str,
                (length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
    }


    public static void toast(String str) {
        toast(str, false);
    }


    public static void log(String str){
        if(debug_mode) Log.i("Log", str);
    }


}
