//package com.mhacks.android.data.network.gcm;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.mhacks.android.dagger.component.DaggerHackathonComponent;
//import com.mhacks.android.dagger.component.DaggerNetComponent;
//import com.mhacks.android.dagger.component.HackathonComponent;
//import com.mhacks.android.dagger.component.NetComponent;
//import com.mhacks.android.dagger.module.RetrofitModule;
//import com.mhacks.android.data.network.services.HackathonApiService;
//
//import org.mhacks.android.R;
//
//import java.io.IOException;
//
//import javax.inject.Inject;
//
///**
// * Created by Omkar Moghe on 2/10/2016.
// */
//public class RegistrationIntentService extends IntentService {
//
//    private HackathonComponent hackathonComponent;
//    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
//    public static final String FCM_TOKEN = "FCMToken";
//    private static final String TAG = "RegIntentService";
//
//    @Inject HackathonApiService hackathonApiService;
//
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to title the worker thread, important only for debugging.
//     */
//    public RegistrationIntentService() {
//        super("RegIntentService");
//        Log.d(TAG, "HELLO WORLD");
//        NetComponent netComponent = DaggerNetComponent.builder()
//                .appModule(null)
//                .authModule(null)
//                .retrofitModule(new RetrofitModule("https://staging.mhacks.org/v1/"))
//                .build();
//        DaggerHackathonComponent.builder().netComponent(netComponent);
//        hackathonComponent = DaggerHackathonComponent.builder()
//                .netComponent(netComponent)
//                .build();
//        hackathonComponent.inject(this);
//
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
//        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
//        String token = instanceID.getToken();
//
//        try {
//            sharedPreferences.edit().putString(FCM_TOKEN, token).apply();
//            sendRegistrationToServer(token);
//        } catch (IOException e) {
//            Log.d(TAG, "Failed to complete token refresh", e);
//            // If an exception happens while fetching the new token or updating our registration data
//            // on a third-party server, this ensures that we'll attempt the update at a later time.
//            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
//        }
//
//
//
//
//        sharedPreferences.edit().putString(FCM_TOKEN, token).apply();
//        Log.d(TAG, "FCM Registration Token: " + token);
//
//
////            String senderId = getResources().getString(R.string.gcm_defaultSenderId);
////
////                    instanceID.getToken(getString(R.string.gcm_defaultSenderId),
////                                               GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
////
////        } catch (IOException ioe) {
////            Log.e(TAG, "onHandleIntent: shit", ioe);
////        }
//    }
//
//    private void sendRegistrationToServer(String token) throws IOException {
//        L
//
//
//        throw new IOException("GCM Refresh failed");
//        // send network request
//
//        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
////        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
////        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
//    }
//}
