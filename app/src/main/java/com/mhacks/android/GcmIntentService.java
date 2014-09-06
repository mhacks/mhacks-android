package com.mhacks.android;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.iv.android.R;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/6/14.
 */
public class GcmIntentService extends IntentService {
  public static final String TAG = "GcmIntentService";

  public static final int NOTIFICATION_ID = 1;
  private NotificationManager mNotificationManager;
  NotificationCompat.Builder builder;

  public GcmIntentService() {
    super("GcmIntentService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Bundle extras = intent.getExtras();
    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

    String messageType = gcm.getMessageType(intent);

    if (!extras.isEmpty()) switch (messageType) {  // has effect of unparcelling Bundle

      case GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR:
        break;

      case GoogleCloudMessaging.MESSAGE_TYPE_DELETED:
        break;

      case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:
        Log.d(TAG, "Message received: " + extras.toString());

    }
    // Release the wake lock provided by the WakefulBroadcastReceiver.
    GcmBroadcastReceiver.completeWakefulIntent(intent);
  }

  private void sendNotification(String msg) {
    mNotificationManager = (NotificationManager)
      this.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
      new Intent(this, MainActivity.class), 0);

    NotificationCompat.Builder mBuilder =
      new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
          .bigText(msg))
        .setContentText(msg);

    mBuilder.setContentIntent(contentIntent);
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
  }
}