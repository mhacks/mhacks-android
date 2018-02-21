package com.mhacks.app.ui.qrscan

import android.os.Bundle
import com.mhacks.app.R
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.qrscan.view.QRScanView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.widget.CompoundButton
import android.widget.TextView
import android.app.Activity
import android.view.View

class QRScanActivity: BaseActivity(), QRScanView,
        View.OnClickListener {
    override fun onPostQREventSuccess() {

    }

    override fun onPostQREventFailure() {
    }

    // use a compound button so either checkbox or switch widgets work.
    private lateinit var autoFocus: CompoundButton
    private lateinit var useFlash: CompoundButton
    private lateinit var statusMessage: TextView
    private lateinit var barcodeValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        statusMessage = findViewById(R.id.status_message)
        barcodeValue = findViewById(R.id.barcode_value)

        autoFocus = findViewById(R.id.auto_focus)
        useFlash = findViewById(R.id.use_flash)

        findViewById<View>(R.id.read_barcode).setOnClickListener(this)
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        if (v.id == R.id.read_barcode) {
            // launch barcode activity.
            val intent = Intent(this, BarcodeCaptureActivity::class.java)
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus!!.isChecked)
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash!!.isChecked)

            startActivityForResult(intent, RC_BARCODE_CAPTURE)
        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * [.RESULT_CANCELED] if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     *
     *
     *
     * You will receive this call immediately before onResume() when your
     * activity is re-starting.
     *
     *
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode  The integer result code returned by the child activity
     * through its setResult().
     * @param data        An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     * @see .startActivityForResult
     *
     * @see .createPendingResult
     *
     * @see .setResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    statusMessage!!.setText(R.string.barcode_success)
                    barcodeValue!!.text = barcode.displayValue
                    Log.d(TAG, "Barcode read: " + barcode.displayValue)
                } else {
                    statusMessage!!.setText(R.string.barcode_failure)
                    Log.d(TAG, "No barcode captured, intent data is null")
                }
            } else {
                statusMessage!!.text = String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {

        private val RC_BARCODE_CAPTURE = 9001
        private val TAG = "BarcodeMain"
    }
}
