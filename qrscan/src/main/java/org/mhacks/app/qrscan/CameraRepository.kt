package org.mhacks.app.qrscan

import javax.inject.Inject

class CameraRepository @Inject constructor(
        private val cameraPreferencesManager: CameraPreferencesManager) {

    fun getCameraSettings() =
            cameraPreferencesManager.getCameraSettingsRx()

    fun putCameraSettings(settings: Pair<Boolean, Boolean>) =
            cameraPreferencesManager.putCameraSettingsRx(settings)
}