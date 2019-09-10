package org.mhacks.app.qrscan.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.qrscan.CameraRepository
import org.mhacks.app.qrscan.CameraSetting
import javax.inject.Inject

class GetCameraSettingsUseCase @Inject constructor(
        private val cameraRepository: CameraRepository) :
        SingleUseCase<Unit, CameraSetting>() {

    override fun getSingle(parameters: Unit) =
            Single.just(cameraRepository.getCameraSettings())

}