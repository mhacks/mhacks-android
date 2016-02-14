package com.mhacks.android.data.network.gcm;

import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Omkar Moghe on 2/10/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    public MyInstanceIDListenerService() {
        super();
    }

    @Override
    public void zzp(Intent intent) {
        super.zzp(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /*@Override
    public void zzah(boolean b) {
        super.zzah(b);
    }*/

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
//        super.onTokenRefresh();
    }


}
