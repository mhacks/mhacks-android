package org.mhacks.app.qrscan.usecase

import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.qrscan.CameraRepository
import javax.inject.Inject

/**
 * Takes current flash and auto focus use case and returns the opposite value.
 *
 * Toggles the camera settings.
 **/
class UpdateCameraSettingsUseCase @Inject constructor(
        private val cameraRepository: CameraRepository) :
        SingleUseCase<Pair<Boolean, Boolean>, Pair<Boolean, Boolean>>() {

    override fun getSingle(parameters:
                           Pair<Boolean, Boolean>) =
            cameraRepository.putCameraSettings(parameters)

}