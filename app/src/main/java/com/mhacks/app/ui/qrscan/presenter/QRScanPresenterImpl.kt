package com.mhacks.app.ui.qrscan.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.qrscan.view.QRScanView

/**
 * Implementation of QRScanPresenter.
 */
class QRScanPresenterImpl(private val qrScanView: QRScanView,
                          private val mHacksService: MHacksService)

    : QRScanPresenter, BasePresenterImpl() {

    override fun postQRScanEvent() {

    }

}
