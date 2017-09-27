package com.mhacks.app.data.network.gcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Omkar Moghe on 2/10/2016.
 */
public class MyGcmListenerService extends FirebaseMessagingService {

    public MyGcmListenerService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        Map data = message.getData();
    }
}
