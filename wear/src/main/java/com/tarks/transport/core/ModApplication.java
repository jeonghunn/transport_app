package com.tarks.transport.core;

import android.app.Application;

/**
 * Created by JHRunning on 11/14/14.
 */

public class ModApplication extends Application {
    private static ModApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ModApplication getInstance() {
        return instance;
    }
}