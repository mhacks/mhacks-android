package com.mhacks.app.data

import android.content.SharedPreferences
import io.reactivex.Single

/**
 * Defines the interactions with SharedPreferences within the app.
 */
class SharedPreferencesManager(private val sharedPreferences: SharedPreferences) {

    private fun putCameraSettings(settings: Pair<Boolean, Boolean>): Pair<Boolean, Boolean> {
        sharedPreferences.edit()
                .putBoolean(AUTO_FOCUS_ENABLED_KEY, settings.first)
                .putBoolean(FLASH_ENABLED_KEY, settings.second)
                .apply()
        return settings
    }

    fun putCameraSettingsRx(settings: Pair<Boolean, Boolean>) =
            getRxSingle(putCameraSettings(settings))

    private fun getCameraSettings() =
            Pair(
                sharedPreferences.getBoolean(AUTO_FOCUS_ENABLED_KEY, false),
                sharedPreferences.getBoolean(FLASH_ENABLED_KEY, false))

    fun getCameraSettingsRx() =
            getRxSingle(getCameraSettings())

    private fun <T> getRxSingle(source: T) = Single.create<T> {
        it.onSuccess(source)
    }

    companion object {

        private const val AUTO_FOCUS_ENABLED_KEY = "AUTO_FOCUS_ENABLED_KEY"

        private const val FLASH_ENABLED_KEY = "FLASH_ENABLED_KEY"

    }
}