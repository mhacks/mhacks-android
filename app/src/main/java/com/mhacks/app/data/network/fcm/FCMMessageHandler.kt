package com.mhacks.app.data.network.fcm

import android.app.Notification
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.mhacks.x.R

/**
 * Created by jeffreychang on 9/20/17.
 */

class FCMMessageHandler : FirebaseMessagingService() {

    private val mhacksGroup = "MHacks Group"
    private val MESSAGE_NOTIFICATION_ID = 435345
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data: Map<String, String> = remoteMessage.data
        val from = remoteMessage.from

        val notification = remoteMessage.notification
        createNotification(notification);
    }

    private fun createNotification(notification: RemoteMessage.Notification) {
        val builder = NotificationCompat.Builder(baseContext, mhacksGroup)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.launcher_icon)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

        val notificationManager = NotificationManagerCompat.from(baseContext)
        notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build())
    }
}