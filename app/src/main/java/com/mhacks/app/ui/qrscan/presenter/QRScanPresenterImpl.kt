package com.mhacks.app.ui.qrscan.presenter

import android.content.SharedPreferences
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.qrscan.view.QRScanView
import javax.inject.Inject


/**
 * Implementation of QRScanPresenter.
 */
class QRScanPresenterImpl @Inject constructor(
        private val qrScanView: QRScanView,
        private val mHacksService: MHacksService,
        private val sharedPreferences: SharedPreferences)
    : QRScanPresenter, BasePresenterImpl() {


    override fun getCameraSettings() {

        qrScanView.onGetCameraSettings(Pair(
                sharedPreferences.getBoolean(AUTO_FOCUS_ENABLED_KEY, false),
                sharedPreferences.getBoolean(FLASH_ENABLED_KEY, false)
        ))
    }

    override fun postQRScanEvent() {

    }

    override fun updateCameraSettings(isAutoFocusEnabled: Boolean,
                                      isFlashEnabled: Boolean) {
        sharedPreferences.edit()
                .putBoolean(AUTO_FOCUS_ENABLED_KEY, isAutoFocusEnabled)
                .putBoolean(FLASH_ENABLED_KEY, isFlashEnabled)
                .apply()
        qrScanView.onUpdateCameraSettings(Pair(
                isAutoFocusEnabled, isFlashEnabled)
        )
    }

    companion object {

        private const val AUTO_FOCUS_ENABLED_KEY = "AUTO_FOCUS_ENABLED_KEY"

        private const val FLASH_ENABLED_KEY = "FLASH_ENABLED_KEY"
    }
}