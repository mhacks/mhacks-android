package com.mhacks.app.ui.qrscan.presenter

import com.mhacks.app.ui.common.BasePresenter

/**
 * Presenter contract for QRScanPresenter
 */
interface QRScanPresenter: BasePresenter {

    fun verifyTicket(email: String)

    fun getCameraSettings()

    fun updateCameraSettings(isAutoFocusEnabled: Boolean,
                             isFlashEnabled: Boolean)
}
