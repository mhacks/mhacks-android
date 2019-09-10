package org.mhacks.app.qrscan

import javax.inject.Inject

class CameraRepository @Inject constructor(
        private val QRScanPrefProvider: QRScanPrefProvider) {

    fun getCameraSettings() = QRScanPrefProvider.cameraSetting

    fun putCameraSettings(cameraSetting: CameraSetting): CameraSetting {
        QRScanPrefProvider.cameraSetting = cameraSetting
        return cameraSetting
    }

}