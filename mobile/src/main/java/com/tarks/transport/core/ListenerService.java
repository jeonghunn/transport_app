package com.tarks.transport.core;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.tarks.transport.R;
import java.util.Map;

public class ListenerService extends WearableListenerService {


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
     //   showToast(messageEvent.getPath());
        global.log(messageEvent.getPath());
checkMessage(messageEvent.getPath(), messageEvent.getData());


    }
    private void checkMessage(String message, byte[] bytes){
        String[] array = message.split("//");
        String action_kind = array[0];
        String data = array[1];




    }



}