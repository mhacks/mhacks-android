package com.mhacks.app.data.repository

import com.mhacks.app.data.SharedPreferencesManager
import javax.inject.Inject

class CameraRepository @Inject constructor(
        private val sharedPreferencesManager: SharedPreferencesManager) {

    fun getCameraSettings() =
            sharedPreferencesManager.getCameraSettingsRx()

    fun putCameraSettings(settings: Pair<Boolean, Boolean>) =
            sharedPreferencesManager.putCameraSettingsRx(settings)
}