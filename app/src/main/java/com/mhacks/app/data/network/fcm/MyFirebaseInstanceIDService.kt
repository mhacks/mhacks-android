package com.mhacks.app.data.network.fcm

import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by jeffreychang on 9/20/17.
 */

class MyFirebaseInstanceIDService: FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // Fetch updated Instance ID token and notify of changes
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
    }
}