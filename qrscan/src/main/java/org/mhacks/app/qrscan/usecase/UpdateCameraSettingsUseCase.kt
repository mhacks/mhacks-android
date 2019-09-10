package org.mhacks.app.qrscan.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.qrscan.CameraRepository
import org.mhacks.app.qrscan.CameraSetting
import javax.inject.Inject

/**
 * Takes current flash and auto focus use case and returns the opposite value.
 *
 * Toggles the camera settings.
 **/
class UpdateCameraSettingsUseCase @Inject constructor(
        private val cameraRepository: CameraRepository)
    : SingleUseCase<CameraSetting, CameraSetting>() {

    override fun getSingle(parameters: CameraSetting) =
            Single.just(cameraRepository.putCameraSettings(parameters))

}
