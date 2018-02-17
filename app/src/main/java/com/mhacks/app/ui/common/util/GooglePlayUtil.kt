package com.mhacks.app.ui.common.util

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import timber.log.Timber

/**
 * Created by jeffreychang on 9/19/17.
 */


/**
 * Check the device to make sure it has the Google Play Services APK. If
 * it doesn't, display a dialog that allows users to download the APK from
 * the Google Play Store or enable it in the device's system settings.
 */

private const val PLAY_SERVICES_RESOLUTION_REQUEST = 1337

class GooglePlayUtil {
     companion object {
         fun checkPlayServices(activity: Activity): Boolean {
             val apiAvailability = GoogleApiAvailability.getInstance()
             val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)
             if (resultCode != ConnectionResult.SUCCESS) {
                 if (apiAvailability.isUserResolvableError(resultCode)) {
                     apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                             .show()
                 } else {
                     Timber.i("This device is not supported.")
                     activity.finish()
                 }
                 return false
             }
             return true
         }
     }
}

