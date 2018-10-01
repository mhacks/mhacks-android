package com.mhacks.app.ui.qrscan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.mhacks.app.data.models.Feedback
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.qrscan.usecase.GetCameraSettingsUseCase
import com.mhacks.app.ui.qrscan.usecase.UpdateCameraSettingsUseCase
import com.mhacks.app.ui.qrscan.usecase.VerifyTicketUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for QRScan feature
 */
class QRScanViewModel @Inject constructor(
        private val verifyTicketUseCase: VerifyTicketUseCase,
        private val getCameraSettingsUseCase: GetCameraSettingsUseCase,
        private val updateCameraSettingsUseCase: UpdateCameraSettingsUseCase): ViewModel() {

    private val verifyTicketResult = verifyTicketUseCase.observe()

    private val _verifyTicket = MediatorLiveData<List<Feedback>>()

    val verifyTicket
        get() = _verifyTicket

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackBarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _cameraSettings = MediatorLiveData<Pair<Boolean, Boolean>>()

    private val getCameraSettingsResult = getCameraSettingsUseCase.observe()

    private val putCameraSettingsResult = updateCameraSettingsUseCase.observe()

    val cameraSettings get() = _cameraSettings

    init {
        _verifyTicket.addSource(verifyTicketResult) {
            if (it is Result.Success) {
                it.let { mapResult ->
                    verifyTicket.value = mapResult.data
                }
            } else if (it is Result.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            val textRes = when (retrofitException.code) {
                                400 -> R.string.unauthorized_error
                                else -> R.string.unknown_error
                            }
                            _snackBarMessage.value =
                                    TextMessage(
                                            textRes,
                                            null)
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.no_internet,
                                            null)

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.unknown_error,
                                            null)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.unauthorized_error,
                                            null)
                        }
                    }
                }
            }
        }

        _cameraSettings.addSource(getCameraSettingsResult, ::updateCameraSettings)

        _cameraSettings.addSource(putCameraSettingsResult, ::updateCameraSettings)
    }


    private fun updateCameraSettings(result: Result<Pair<Boolean, Boolean>>?) {
        if (result is Result.Success) {
            result.let { settings ->
                Timber.d("Updated camera settings")
                _cameraSettings.value = settings.data
            }
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

        var cameraSettings = _cameraSettings.value ?: Pair(false, false)

        with(cameraSettings) {
            cameraSettings = when (settingChanged) {
                AUTO_FOCUS -> Pair(first, !second)
                FLASH -> Pair(!first, second)
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