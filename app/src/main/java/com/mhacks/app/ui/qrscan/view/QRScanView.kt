package com.mhacks.app.ui.qrscan.view

/**
 * View contract for the QRScan Activity.
 */
interface QRScanView {

    fun onPostQREventSuccess()

    fun onPostQREventFailure()

    fun onGetCameraSettings(settings: Pair<Boolean, Boolean>)

    fun onUpdateCameraSettings(settings: Pair<Boolean, Boolean>)
}