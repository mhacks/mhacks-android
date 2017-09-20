package com.mhacks.android.data.network.fcm

import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.mhacks.android.R

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
                .setSmallIcon(R.mipmap.launcher_icon)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
        val notificationManager = NotificationManagerCompat.from(baseContext)
        notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build())
    }
}