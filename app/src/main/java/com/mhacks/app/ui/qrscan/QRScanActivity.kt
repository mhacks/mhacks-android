package com.mhacks.app.ui.qrscan

import android.app.Activity
import android.os.Bundle
import com.google.zxing.Result
import com.mhacks.app.R
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRScanActivity : Activity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setTheme(R.style.MHacksTheme)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)                // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        mScannerView!!.resumeCameraPreview(this)
    }
}
