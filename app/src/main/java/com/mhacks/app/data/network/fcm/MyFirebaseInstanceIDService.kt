package com.mhacks.app.data.network.fcm

import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Service for handling token registration needed to receive push notifications.
 */
class MyFirebaseInstanceIDService: FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
    }
}