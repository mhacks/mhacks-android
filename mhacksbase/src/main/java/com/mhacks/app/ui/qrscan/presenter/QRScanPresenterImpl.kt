package com.mhacks.app.ui.qrscan.presenter

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.qrscan.view.QRScanView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Implementation of QRScanPresenter.
 */
class QRScanPresenterImpl @Inject constructor(
        private val qrScanView: QRScanView,
        private val mHacksService: MHacksService,
        private val mHacksDatabase: MHacksDatabase,
        private val sharedPreferencesManager: SharedPreferencesManager,
        private val authInterceptor: AuthModule.AuthInterceptor)
    : QRScanPresenter, BasePresenterImpl() {


    override fun getCameraSettings()
            = qrScanView.onGetCameraSettings(sharedPreferencesManager.getCameraSettings())

    override fun verifyTicket(email: String) {
        mHacksDatabase.loginDao().getLogin()
                .flatMap { authInterceptor.token = it.token
                    mHacksService.verifyUserTicket(email) }
                .map { it.feedback }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { qrScanView.onVerifyTicketSuccess(it) },
                        { qrScanView.onVerifyTicketFailure(it) }
                )
    }

    override fun updateCameraSettings(isAutoFocusEnabled: Boolean,
                                      isFlashEnabled: Boolean) {
        sharedPreferencesManager.putCameraSettings(isAutoFocusEnabled, isFlashEnabled)
        qrScanView.onUpdateCameraSettings(Pair(
                isAutoFocusEnabled, isFlashEnabled)
        )
    }
}