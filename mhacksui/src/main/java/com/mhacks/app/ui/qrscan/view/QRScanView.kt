package com.mhacks.app.ui.qrscan.view

import com.mhacks.app.data.models.Feedback

/**
 * View contract for the QRScan Activity.
 */
interface QRScanView {

    fun onVerifyTicketSuccess(user: List<Feedback>)

    fun onVerifyTicketFailure(error: Throwable)

    fun onGetCameraSettings(settings: Pair<Boolean, Boolean>)

    fun onUpdateCameraSettings(settings: Pair<Boolean, Boolean>)
}