@file:Suppress("DEPRECATION")

package com.mhacks.app.ui.qrscan

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
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.app.AlertDialog
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.mhacks.app.R
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.qrscan.presenter.QRScanPresenter
import com.mhacks.app.ui.qrscan.view.BarcodeGraphic
import com.mhacks.app.ui.qrscan.view.BarcodeGraphicTracker
import com.mhacks.app.ui.qrscan.view.BarcodeTrackerFactory
import com.mhacks.app.ui.qrscan.view.QRScanView
import com.mhacks.app.ui.qrscan.view.camera.CameraSource
import com.mhacks.app.ui.qrscan.view.camera.CameraSourcePreview
import com.mhacks.app.ui.qrscan.view.camera.GraphicOverlay
import kotlinx.android.synthetic.main.activity_barcode_capture.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Activity for the multi-tracker app.  This app detects barcodes and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and ID of each barcode.
 */
class QRScanActivity: BaseActivity(), QRScanView, BarcodeGraphicTracker.BarcodeUpdateListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay<BarcodeGraphic>? = null

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    @Inject lateinit var qrScanPresenter: QRScanPresenter

    private var hasAutoFocus = false

    private var useFlash = false

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_barcode_capture)

        preview = findViewById<View>(R.id.activity_camera_source_preview) as CameraSourcePreview
        graphicOverlay = findViewById(R.id.activity_barcode_graphic_overlay)


        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            qrScanPresenter.getCameraSettings()
            activity_camera_source_flash_icon.setOnClickListener {
                useFlash = !useFlash
                qrScanPresenter.updateCameraSettings(hasAutoFocus, useFlash)

            }
            activity_camera_source_autofocus_icon.setOnClickListener {
                hasAutoFocus = !hasAutoFocus
                qrScanPresenter.updateCameraSettings(hasAutoFocus, useFlash)
            }
        } else {
            requestCameraPermission()
        }

        gestureDetector = GestureDetector(this, CaptureGestureListener())
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        activity_camera_source_autofocus_icon
        Toast.makeText(this,"Tap to capture. Pinch/Stretch to zoom",
                Toast.LENGTH_LONG).show()
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
    private fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
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
                Toast.makeText(this, R.string.low_storage_error,
                        Toast.LENGTH_LONG).show()
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
                .setFocusMode(if (this.hasAutoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE else null)
                .setFlashMode(if (this.useFlash) Camera.Parameters.FLASH_MODE_TORCH else null)
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
            Timber.w("Got unexpected permission result: " + requestCode)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.w("Camera permission granted - initialize the camera source")
            // we have permission, so create the camerasource
            qrScanPresenter.getCameraSettings()
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
            activity_camera_source_id_text_view.text = best.displayValue
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
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
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
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
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
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         */
        override fun onScaleEnd(detector: ScaleGestureDetector) {
            cameraSource?.doZoom(detector.scaleFactor)
        }
    }

    override fun onBarcodeDetected(barcode: Barcode) {

    }

    override fun onGetCameraSettings(settings: Pair<Boolean, Boolean>) {
        hasAutoFocus = settings.first
        useFlash = settings.second

        val colorAccent =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent))

        if (hasAutoFocus) ImageViewCompat.setImageTintList(activity_camera_source_autofocus_icon, colorAccent)
        else ImageViewCompat.setImageTintList(activity_camera_source_autofocus_icon,
                ColorStateList.valueOf(Color.WHITE))

        if (useFlash) ImageViewCompat.setImageTintList(activity_camera_source_flash_icon, colorAccent)
        else ImageViewCompat.setImageTintList(activity_camera_source_flash_icon,
                ColorStateList.valueOf(Color.WHITE))
        createCameraSource(hasAutoFocus, useFlash)
    }

    override fun onUpdateCameraSettings(settings: Pair<Boolean, Boolean>) {
        val colorAccent =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent))

        if (hasAutoFocus) ImageViewCompat.setImageTintList(activity_camera_source_autofocus_icon, colorAccent)
        else ImageViewCompat.setImageTintList(activity_camera_source_autofocus_icon,
                ColorStateList.valueOf(Color.WHITE))

        if (useFlash)
            ImageViewCompat.setImageTintList(activity_camera_source_flash_icon, colorAccent)
        else ImageViewCompat.setImageTintList(activity_camera_source_flash_icon,
                ColorStateList.valueOf(Color.WHITE))
        updateCameraSettings(useFlash, hasAutoFocus)
    }

    private var camera: Camera? = null
    private fun updateCameraSettings(hasFlash: Boolean, hasAutoFocus: Boolean) {
        camera = getCamera(cameraSource!!)
        if (camera != null) {
            try {
                val param = camera!!.parameters
                param.flashMode = if (hasFlash) Camera.Parameters.FLASH_MODE_TORCH else Camera.Parameters.FLASH_MODE_OFF
                param.focusMode = if (hasAutoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE else Camera.Parameters.FOCUS_MODE_AUTO
                camera?.parameters = param
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun getCamera(@NonNull cameraSource: CameraSource): Camera? {
        val declaredFields = CameraSource::class.java.declaredFields
        for (field in declaredFields) {
            if (field.type === Camera::class.java) {
                field.isAccessible = true
                try {
                    return field.get(cameraSource) as Camera
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return null
    }

    override fun onPostQREventSuccess() {

    }

    override fun onPostQREventFailure() {

    }

    companion object {

        private const val RC_HANDLE_GMS = 9001

        private const val RC_HANDLE_CAMERA_PERM = 2
    }
}