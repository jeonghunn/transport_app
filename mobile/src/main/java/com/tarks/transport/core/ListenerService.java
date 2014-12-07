package com.tarks.transport.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.tarks.transport.R;
import java.util.Map;

public class ListenerService extends WearableListenerService {

    Messenger mService = null;
    boolean mIsBound;
    final Messenger mMessenger = new Messenger(new IncomingHandler());
String send_data;
    int handlernumber;

    @Override
    public void onCreate() {
        super.onCreate();

        global.log("HI Listener Service start!");


    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //   showToast(messageEvent.getPath());
        global.log(messageEvent.getPath());
        checkMessage(messageEvent.getPath(), messageEvent.getData());


    }

    private void checkMessage(String message, byte[] bytes) {
        try {
            String[] array = message.split("//");
            String action_kind = array[0];
            String data = array[1];
            if (action_kind.matches("request_stations_data")) requsetStationsData(data);
            if (action_kind.matches("LocationMode")) setLocationMode(data);
        } catch (Exception e) {
        }


    }

    private void setLocationMode(String data){
        handlernumber = 2 ;
        send_data = data;
        doBindService();
    }

    private void requsetStationsData(String data) {
        handlernumber = 1 ;
        send_data = data;
        doBindService();



    }

    void doBindService() {
        if (!mIsBound)  bindService(new Intent(this, CoreSystem.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, handlernumber);
                msg.replyTo = mMessenger;
                msg.obj = send_data;
                mService.send(msg);


            }
            catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
         global.log("disconnected");
        }
    };


    void doUnbindService() {
        send_data = null;

        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, 2);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
        }
        catch (Throwable t) {
            Log.e("MainActivity", "Failed to unbind from the service", t);
        }
    }

}