package org.mhacks.app.qrscan

import android.content.SharedPreferences
import io.reactivex.Single


private const val AUTO_FOCUS_ENABLED_KEY = "AUTO_FOCUS_ENABLED_KEY"
private const val FLASH_ENABLED_KEY = "FLASH_ENABLED_KEY"

data class CameraSetting(
        val autoFocusEnabled: Boolean,
        val flashEnabled: Boolean
)

/**
 * Defines the stored settings within the camera
 */
class QRScanPrefProvider(private val sharedPreferences: SharedPreferences) {

    var cameraSetting: CameraSetting
        get() {
            return CameraSetting(
                    sharedPreferences.getBoolean(AUTO_FOCUS_ENABLED_KEY, false),
                    sharedPreferences.getBoolean(FLASH_ENABLED_KEY, false)
            )
        }
    set(value) {
        sharedPreferences.edit()
                .putBoolean(AUTO_FOCUS_ENABLED_KEY, value.autoFocusEnabled)
                .putBoolean(FLASH_ENABLED_KEY, value.flashEnabled)
                .apply()
    }

    fun getCameraSettingsRx() =
            getRxSingle(cameraSetting)

    private fun <T> getRxSingle(source: T) = Single.create<T> {
        it.onSuccess(source)
    }

}