package com.mhacks.android.data.network.gcm;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by Omkar Moghe on 2/10/2016.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    public MyInstanceIDListenerService() {
        super();
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
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);
//        super.onTokenRefresh();
    }

    private void sendRegistrationToServer(String refreshedToken) {
    }


}
