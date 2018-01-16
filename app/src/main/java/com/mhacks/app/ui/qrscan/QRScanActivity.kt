package com.mhacks.app.ui.qrscan

import android.app.Activity
import android.os.Bundle
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.mhacks.x.Manifest
import org.mhacks.x.R
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions



@RuntimePermissions
class QRScanActivity : Activity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null

//    @NeedsPermission(Manifest.permission.CAMERA)
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
        // Do something with the result here
//        Log.v(TAG, rawResult.text) // Prints scan results
//        Log.v(TAG, rawResult.barcodeFormat.toString()) // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView!!.resumeCameraPreview(this)
    }
}
