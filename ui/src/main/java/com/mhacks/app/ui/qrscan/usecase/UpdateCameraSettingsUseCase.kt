package com.mhacks.app.ui.qrscan.usecase

import com.mhacks.app.data.repository.CameraRepository
import com.mhacks.app.mvvm.SingleUseCase
import javax.inject.Inject


/**
 * Takes current flash and auto focus use case and returns the opposite value.
 *
 * Toggles the camera settings.
**/
class UpdateCameraSettingsUseCase @Inject constructor(
        private val cameraRepository: CameraRepository): SingleUseCase<
        Pair<Boolean, Boolean>,
        Pair<Boolean, Boolean>>() {

    override fun getSingle(parameters:
                           Pair<Boolean, Boolean>) =
            cameraRepository.putCameraSettings(parameters)

}