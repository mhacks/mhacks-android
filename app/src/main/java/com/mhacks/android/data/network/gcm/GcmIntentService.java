package com.mhacks.android.data.network.gcm;

/**
 * Created by Riyu on 2/13/16.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.data.network.gcm.GcmBroadcastReceiver;
import com.mhacks.android.ui.announcements.AnnouncementsFragment;

import org.mhacks.android.R;


public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    private Handler handler;
    String mes;
    public GcmIntentService() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("message");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notif_icon)
                        .setContentTitle("MHacks")
                        .setContentText(mes);

        Intent clickable_intent = new Intent(this, MainActivity.class);
        clickable_intent.putExtra("notif_link", "Announcements");
        clickable_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickable_intent, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        Log.i("GCMInfo",  "IN INTENT message:" + mes);
        //Log.i("GCMtitle",  mes);

        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }
}
