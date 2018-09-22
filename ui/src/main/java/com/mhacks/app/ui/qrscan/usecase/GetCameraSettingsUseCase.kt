package com.mhacks.app.ui.qrscan.usecase

import com.mhacks.app.data.repository.CameraRepository
import com.mhacks.app.mvvm.SingleUseCase
import javax.inject.Inject

class GetCameraSettingsUseCase @Inject constructor(
        private val cameraRepository: CameraRepository): SingleUseCase<
        Unit,
        Pair<Boolean, Boolean>>() {

    override fun getSingle(parameters: Unit) =
            cameraRepository.getCameraSettings()

}