package org.mhacks.app.qrscan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.core.domain.user.data.Feedback
import org.mhacks.app.qrscan.usecase.GetCameraSettingsUseCase
import org.mhacks.app.qrscan.usecase.UpdateCameraSettingsUseCase
import org.mhacks.app.qrscan.usecase.VerifyTicketUseCase
import timber.log.Timber
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

/**
 * ViewModel for QRScan feature
 */
class QRScanViewModel @Inject constructor(
        private val verifyTicketUseCase: VerifyTicketUseCase,
        private val getCameraSettingsUseCase: GetCameraSettingsUseCase,
        private val updateCameraSettingsUseCase: UpdateCameraSettingsUseCase) : ViewModel() {

    private val verifyTicketResult = verifyTicketUseCase.observe()

    private val _verifyTicket = MediatorLiveData<List<Feedback>>()

    val verifyTicket
        get() = _verifyTicket

    private val _snackBarMessage = MediatorLiveData<Text>()

    val snackBarMessage: LiveData<Text>
        get() = _snackBarMessage

    private val _cameraSettings = MediatorLiveData<CameraSetting>()

    private val getCameraSettingsResult = getCameraSettingsUseCase.observe()

    private val putCameraSettingsResult = updateCameraSettingsUseCase.observe()

    val cameraSettings get() = _cameraSettings

    init {
        _verifyTicket.addSource(verifyTicketResult) {
            if (it is Outcome.Success) {
                it.let { mapResult ->
                    verifyTicket.value = mapResult.data
                }
            } else if (it is Outcome.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            val textRes = when (retrofitException.code) {
                                400 -> coreR.string.unauthorized_error
                                else -> coreR.string.unknown_error
                            }
                            _snackBarMessage.value = Text.Res(textRes)
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value = Text.Res(coreR.string.no_internet)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value = Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value = Text.Res(coreR.string.unauthorized_error)
                        }
                    }
                }
            }
        }

        _cameraSettings.addSource(getCameraSettingsResult, ::updateCameraSettings)

        _cameraSettings.addSource(putCameraSettingsResult, ::updateCameraSettings)
    }

    private fun updateCameraSettings(result: Outcome<CameraSetting>?) {
        if (result is Outcome.Success) {
            Timber.d("Updated camera settings")
            _cameraSettings.value = result.data
        }
    }

    fun verifyTicket(email: String) {
        verifyTicketUseCase.execute(email)
    }

    fun getCameraSettings() {
        getCameraSettingsUseCase.execute(Unit)
    }

    fun changeCameraSettings(settingChanged: Int) {
        // Pair is <AUTO_FOCUSED_ENABLED, FLASH_ENABLED>

        var cameraSettings = _cameraSettings.value
                ?: CameraSetting(autoFocusEnabled = false, flashEnabled = false)

        with(cameraSettings) {
            cameraSettings = when (settingChanged) {
                AUTO_FOCUS -> CameraSetting(autoFocusEnabled, !flashEnabled)
                FLASH -> CameraSetting(!autoFocusEnabled, flashEnabled)
                else -> {
                    Timber.e("Use constants in Companion to update camera settings")
                    return
                }
            }
        }

        updateCameraSettingsUseCase.execute(cameraSettings)
    }

    override fun onCleared() {
        super.onCleared()
        verifyTicketUseCase.onCleared()
    }

    companion object {

        const val AUTO_FOCUS = 0

        const val FLASH = 1
    }
}