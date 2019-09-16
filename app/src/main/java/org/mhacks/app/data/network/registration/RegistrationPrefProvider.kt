package org.mhacks.app.data.network.registration

import android.content.SharedPreferences
import org.mhacks.app.core.Constants.FIREBASE_AUTH_TOKEN
import javax.inject.Inject

private const val SENT_TOKEN_TO_SERVER = "sentTokenToServer"

/**
 * Defines the auth settings stored within the app
 */
class RegistrationPrefProvider @Inject constructor(
        private val sharedPreferences: SharedPreferences
) {

    var authToken: String
        get() = sharedPreferences.getString(FIREBASE_AUTH_TOKEN, "") ?: ""
        set(value) {
            sharedPreferences.edit()
                    .putString(FIREBASE_AUTH_TOKEN, value)
                    .apply()
        }

    var sentTokenToServer: Boolean
        get() = sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false)
        set(value) {
            sharedPreferences.edit()
                    .putBoolean(SENT_TOKEN_TO_SERVER, value)
                    .apply()
        }

}