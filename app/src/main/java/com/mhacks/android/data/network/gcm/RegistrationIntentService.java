package com.mhacks.android.data.network.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.mhacks.android.R;

import java.io.IOException;

/**
 * Created by Omkar Moghe on 2/10/2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to title the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                                               GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.d(TAG, token);
        } catch (IOException ioe) {
            Log.e(TAG, "onHandleIntent: shit", ioe);
        }
    }
}
