@file:Suppress("DEPRECATION")

package org.mhacks.app.qrscan.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.hardware.Camera
import android.os.Bundle
import android.util.Patterns
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.snackbar.Snackbar
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.BaseActivity
import org.mhacks.app.qrscan.QRScanViewModel
import org.mhacks.app.qrscan.R
import org.mhacks.app.qrscan.databinding.ActivityQrScanBinding
import org.mhacks.app.qrscan.inject

import org.mhacks.app.qrscan.ui.widget.BarcodeGraphic
import org.mhacks.app.qrscan.ui.widget.BarcodeGraphicTracker
import org.mhacks.app.qrscan.ui.widget.camera.CameraSource
import org.mhacks.app.qrscan.ui.widget.camera.CameraSourcePreview
import org.mhacks.app.qrscan.ui.widget.camera.GraphicOverlay
import org.mhacks.app.qrscan.ui.widget.BarcodeTrackerFactory
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

import org.mhacks.app.core.R as coreR

/**
 * Activity for the multi-tracker app.  This app detects barcodes and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and ID of each barcode.
 */
class QRScanActivity : BaseActivity(), BarcodeGraphicTracker.BarcodeUpdateListener {

    private lateinit var binding: ActivityQrScanBinding

    private var camera: Camera? = null

    private var cameraSource: CameraSource? = null

    private var preview: CameraSourcePreview? = null

    private var graphicOverlay: GraphicOverlay<BarcodeGraphic>? = null

    private var scaleGestureDetector: ScaleGestureDetector? = null

    private var gestureDetector: GestureDetector? = null

    @Inject lateinit var qrScanViewModel: QRScanViewModel

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_qr_scan
        )
        subscribeUi()

        preview = findViewById(R.id.activity_camera_source_preview)
        graphicOverlay = findViewById(R.id.activity_barcode_graphic_overlay)

        val rc = ActivityCompat.checkSelfPermission(this@QRScanActivity, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
            qrScanViewModel.getCameraSettings()
            binding.activityCameraSourceFlashIcon.setOnClickListener {
                qrScanViewModel.changeCameraSettings(QRScanViewModel.FLASH)

            }
            binding.activityCameraSourceAutofocusIcon.setOnClickListener {
                qrScanViewModel.changeCameraSettings(QRScanViewModel.AUTO_FOCUS)
            }
        } else {
            requestCameraPermission()
        }

        gestureDetector = GestureDetector(this, CaptureGestureListener())
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        showToast(R.string.barcode_hint)
    }

    private fun subscribeUi() {
        qrScanViewModel.verifyTicket.observe(this, Observer {
            var response = ""
            for (feedback in it) {
                response += feedback.label + ": " + feedback.value + "\n"
            }

            AlertDialog.Builder(this)
                    .setTitle("QR Response")
                    .setMessage(response)
                    .setPositiveButton("Ok") { _, _ -> Timber.i(response) }
                    .show()
        })

        qrScanViewModel.snackBarMessage.observe(this, Observer {
            binding.root.showSnackBar(it)

        })
        qrScanViewModel.cameraSettings.observe(this, Observer {
            it?.let { cameraSettings ->
                val (isAutoFocus, isFlash) = cameraSettings
                updateCameraSettings(isAutoFocus, isFlash)
            }
        })
    }

    private fun requestCameraPermission() {
        Timber.w("Camera permission is not granted. Requesting permission")

        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        val thisActivity = this

        val listener = View.OnClickListener {
            ActivityCompat.requestPermissions(thisActivity, permissions,
                    RC_HANDLE_CAMERA_PERM)
        }

        findViewById<View>(R.id.topLayout).setOnClickListener(listener)

        Snackbar.make(graphicOverlay!!, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val b = scaleGestureDetector!!.onTouchEvent(e)

        val c = gestureDetector!!.onTouchEvent(e)

        return b || c || super.onTouchEvent(e)
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private fun createCameraSource() {
        val context = applicationContext

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        val barcodeDetector = BarcodeDetector.Builder(context).build()
        val barcodeFactory = BarcodeTrackerFactory(graphicOverlay!!, this)
        barcodeDetector.setProcessor(
                MultiProcessor.Builder(barcodeFactory).build())

        if (!barcodeDetector.isOperational) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Timber.w("Detector dependencies are not yet available.")

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                showToast(R.string.low_storage_error)
                Timber.w(getString(R.string.low_storage_error))
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        val builder: CameraSource.Builder = CameraSource.Builder(applicationContext, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f)

        cameraSource = builder
                .build()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        preview?.release()
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     * @see .requestPermissions
     */
    @SuppressLint("BinaryOperationInTimber")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Timber.w("Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.w("Camera permission granted - initialize the camera source")
            createCameraSource()
            qrScanViewModel.getCameraSettings()
            return
        }

        Timber.w("Permission not granted: results len = " + grantResults.size +
                " Result code = " + if (grantResults.isNotEmpty()) grantResults[0] else "(empty)")

        val listener = DialogInterface.OnClickListener { _, _ -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    @SuppressLint("MissingPermission")
    @Throws(SecurityException::class)
    private fun startCameraSource() {
        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg.show()
        }

        if (cameraSource != null) {
            try {
                preview?.start(cameraSource!!, graphicOverlay!!)
            } catch (e: IOException) {
                Timber.e("Unable to start camera source.")
                cameraSource?.release()
                cameraSource = null
            }

        }
    }

    /**
     * onTap returns the tapped barcode result to the calling Activity.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    private fun onTap(rawX: Float, rawY: Float): Boolean {
        // Find tap point in preview frame coordinates.
        val location = IntArray(2)
        graphicOverlay?.getLocationOnScreen(location)
        val x = (rawX - location[0]) / graphicOverlay!!.widthScaleFactor
        val y = (rawY - location[1]) / graphicOverlay!!.heightScaleFactor

        // Find the barcode whose center is closest to the tapped point.
        var best: Barcode? = null
        var bestDistance = java.lang.Float.MAX_VALUE
        for (graphic in graphicOverlay!!.graphics) {
            val barcode = graphic.barcode
            if (barcode?.boundingBox?.contains(x.toInt(), y.toInt())!!) {
                // Exact hit, no need to keep looking.
                best = barcode
                break
            }
            val dx = x - barcode.boundingBox.centerX()
            val dy = y - barcode.boundingBox.centerY()
            val distance = dx * dx + dy * dy  // actually squared distance
            if (distance < bestDistance) {
                best = barcode
                bestDistance = distance
            }
        }
        best?.let {
            if (Patterns.EMAIL_ADDRESS.matcher(best.displayValue).matches()) {
                binding.activityCameraSourceIdTextView.text = best.displayValue
                qrScanViewModel.verifyTicket(best.displayValue)
            } else showToast(R.string.not_valid_email)
            return true
        }
        return false
    }

    private inner class CaptureGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the insertFavoriteEvent - use this to
         * retrieve extended info about insertFavoriteEvent state.
         * @return Whether or not the detector should consider this insertFavoriteEvent
         * as handled. If an insertFavoriteEvent was not handled, the detector
         * will continue to accumulate movement until an insertFavoriteEvent is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            return false
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the insertFavoriteEvent - use this to
         * retrieve extended info about insertFavoriteEvent state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         *
         *
         * Once a scale has ended, [ScaleGestureDetector.getFocusX]
         * and [ScaleGestureDetector.getFocusY] will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the insertFavoriteEvent - use this to
         * retrieve extended info about insertFavoriteEvent state.
         */
        override fun onScaleEnd(detector: ScaleGestureDetector) {
            cameraSource?.doZoom(detector.scaleFactor)
        }
    }

    override fun onBarcodeDetected(barcode: Barcode) {
        Timber.d("%s Detected", barcode.displayValue)
    }

    private fun updateCameraSettings(hasFlash: Boolean, hasAutoFocus: Boolean) {
        camera = cameraSource?.let {
            getCamera(it)
        }
        if (camera != null) {
            try {
                val param = camera?.parameters
                param?.flashMode =
                        if (hasFlash) Camera.Parameters.FLASH_MODE_TORCH
                        else Camera.Parameters.FLASH_MODE_OFF
                param?.focusMode =
                        if (hasAutoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                        else Camera.Parameters.FOCUS_MODE_FIXED
                camera?.parameters = param
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        val colorAccent =
                ColorStateList.valueOf(ContextCompat.getColor(this, coreR.color.colorAccent))

        if (hasAutoFocus)
            ImageViewCompat.setImageTintList(
                    binding.activityCameraSourceAutofocusIcon,
                    colorAccent)
        else ImageViewCompat.setImageTintList(
                binding.activityCameraSourceAutofocusIcon,
                ColorStateList.valueOf(Color.WHITE))

        if (hasFlash)
            ImageViewCompat.setImageTintList(
                    binding.activityCameraSourceFlashIcon,
                    colorAccent)
        else ImageViewCompat.setImageTintList(
                binding.activityCameraSourceFlashIcon,
                ColorStateList.valueOf(Color.WHITE)
        )
    }

    private fun getCamera(cameraSource: CameraSource): Camera? {
        val declaredFields = CameraSource::class.java.declaredFields
        for (field in declaredFields) {
            if (field.type === Camera::class.java) {
                field.isAccessible = true
                try {
                    return field.get(cameraSource) as Camera?
                } catch (e: IllegalAccessException) {
                    Timber.e(e)
                }
                break
            }
        }
        return null
    }

    private fun showToast(stringRes: Int) {
        Toast.makeText(this,
                stringRes,
                Toast.LENGTH_LONG).show()
    }

    companion object {

        private const val RC_HANDLE_GMS = 9001

        private const val RC_HANDLE_CAMERA_PERM = 2
    }
}