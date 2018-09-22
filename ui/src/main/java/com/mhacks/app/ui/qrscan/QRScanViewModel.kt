package com.mhacks.app.ui.qrscan

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Feedback
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.qrscan.usecase.VerifyTicketUseCase
import org.mhacks.mhacksui.R
import javax.inject.Inject

/**
 * ViewModel for QRScan feature
 */

class QRScanViewModel @Inject constructor(
        private val verifyTicketUseCase: VerifyTicketUseCase): ViewModel() {

    private val _verifyTicketResult = verifyTicketUseCase.observe()

    private val _verifyTicket = MediatorLiveData<List<Feedback>>()

    val verifyTicket
        get() = _verifyTicket

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackBarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    init {
        _verifyTicket.addSource(_verifyTicketResult) {
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
    }

    fun verifyTicket(email: String) {
        verifyTicketUseCase.execute(email)
    }

    override fun onCleared() {
        super.onCleared()
        verifyTicketUseCase.onCleared()
    }
}
//class QRScanPresenterImpl @Inject constructor(
//        private val qrScanView: QRScanView,
//        private val mHacksService: MHacksService,
//        private val mHacksDatabase: MHacksDatabase,
//        private val sharedPreferencesManager: SharedPreferencesManager,
//        private val authInterceptor: AuthModule.AuthInterceptor)
//    : QRScanPresenter, BasePresenterImpl() {


//    override fun getCameraSettings()
//            = qrScanView.onGetCameraSettings(sharedPreferencesManager.getCameraSettings())
//
//    override fun verifyTicket(email: String) {
//        mHacksDatabase.loginDao().getLogin()
//                .flatMap { authInterceptor.token = it.token
//                    mHacksService.verifyUserTicket(email) }
//                .map { it.feedback }
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { qrScanView.onVerifyTicketSuccess(it) },
//                        { qrScanView.onVerifyTicketFailure(it) }
//                )
//    }
//
//    override fun updateCameraSettings(isAutoFocusEnabled: Boolean,
//                                      isFlashEnabled: Boolean) {
//        sharedPreferencesManager.putCameraSettings(isAutoFocusEnabled, isFlashEnabled)
//        qrScanView.onUpdateCameraSettings(Pair(
//                isAutoFocusEnabled, isFlashEnabled)
//        )
//    }
//}