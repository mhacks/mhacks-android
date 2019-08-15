package org.mhacks.app.qrscan

import org.mhacks.app.coreComponent
import org.mhacks.app.qrscan.di.DaggerQRScanComponent
import org.mhacks.app.qrscan.ui.QRScanActivity

fun QRScanActivity.inject() {
    DaggerQRScanComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}