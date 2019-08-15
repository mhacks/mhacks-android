package org.mhacks.app.qrscan.usecase

import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.qrscan.CameraRepository
import javax.inject.Inject

class GetCameraSettingsUseCase @Inject constructor(
        private val cameraRepository: CameraRepository) :
        SingleUseCase<Unit, Pair<Boolean, Boolean>>() {

    override fun getSingle(parameters: Unit) =
            cameraRepository.getCameraSettings()

}