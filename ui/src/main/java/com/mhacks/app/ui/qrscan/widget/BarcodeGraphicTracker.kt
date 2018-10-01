package com.mhacks.app.ui.qrscan.widget

import android.content.Context
import com.google.android.gms.vision.barcode.Barcode
import androidx.annotation.UiThread
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.mhacks.app.ui.qrscan.widget.camera.GraphicOverlay


/**
 * Created by jeffreychang on 2/21/18.
 */

class BarcodeGraphicTracker internal constructor(private val mOverlay: GraphicOverlay<BarcodeGraphic>, private val mGraphic: BarcodeGraphic,
                                                 context: Context) : Tracker<Barcode>() {

    private var mBarcodeUpdateListener: BarcodeUpdateListener? = null

    /**
     * Consume the item instance detected from an Activity or Fragment level by implementing the
     * BarcodeUpdateListener interface method onBarcodeDetected.
     */
    interface BarcodeUpdateListener {
        @UiThread
        fun onBarcodeDetected(barcode: Barcode)
    }

    init {
        if (context is BarcodeUpdateListener) {
            this.mBarcodeUpdateListener = context
        } else {
            throw RuntimeException("Hosting activity must implement BarcodeUpdateListener")
        }
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    override fun onNewItem(id: Int, item: Barcode) {
        mGraphic.id = id
        mBarcodeUpdateListener!!.onBarcodeDetected(item)
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    override fun onUpdate(detectionResults: Detector.Detections<Barcode>, item: Barcode) {
        mOverlay.add(mGraphic)
        mGraphic.updateItem(item)
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    override fun onMissing(detectionResults: Detector.Detections<Barcode>) {
        mOverlay.remove(mGraphic)
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    override fun onDone() {
        mOverlay.remove(mGraphic)
    }
}
