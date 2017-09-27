package com.mhacks.app.ui.qrscan

import android.os.Bundle
import com.mhacks.app.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_qr_scan.*
import org.mhacks.x.R

class QRScanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)

        setContentView(R.layout.activity_qr_scan)
        setSupportActionBar(qr_scan_toolbar)
        setTitle("Scan QR")
    }

}
