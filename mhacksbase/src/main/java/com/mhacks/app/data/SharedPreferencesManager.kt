package com.mhacks.app.data

import android.content.SharedPreferences

/**
 * Defines the interactions with SharedPreferences within the app.
 */
class SharedPreferencesManager(private val sharedPreferences: SharedPreferences) {

    fun putIsAdmin(isAdmin: Boolean)
            = sharedPreferences.edit().putBoolean(IS_ADMIN_KEY, isAdmin).apply()

    fun getIsAdmin() = sharedPreferences.getBoolean(IS_ADMIN_KEY, false)

    fun putCameraSettings(isAutoFocusEnabled: Boolean, isFlashEnabled: Boolean) =
        sharedPreferences.edit()
                .putBoolean(AUTO_FOCUS_ENABLED_KEY, isAutoFocusEnabled)
                .putBoolean(FLASH_ENABLED_KEY, isFlashEnabled)
                .apply()

    fun getCameraSettings() =
            Pair(
                sharedPreferences.getBoolean(AUTO_FOCUS_ENABLED_KEY, false),
                sharedPreferences.getBoolean(FLASH_ENABLED_KEY, false))


    companion object {

        private const val AUTO_FOCUS_ENABLED_KEY = "AUTO_FOCUS_ENABLED_KEY"

        private const val FLASH_ENABLED_KEY = "FLASH_ENABLED_KEY"

        private const val IS_ADMIN_KEY = "IS_ADMIN_KEY"
    }
}