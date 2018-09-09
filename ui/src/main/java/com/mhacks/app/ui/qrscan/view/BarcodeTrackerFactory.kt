package com.mhacks.app.ui.qrscan.view

import android.content.Context
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.mhacks.app.ui.qrscan.view.camera.GraphicOverlay

/**
 * Created by jeffreychang on 2/21/18.
 */

internal class BarcodeTrackerFactory(private val mGraphicOverlay: GraphicOverlay<BarcodeGraphic>,
                                     private val mContext: Context) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode): Tracker<Barcode> {
        val graphic = BarcodeGraphic(mGraphicOverlay)
        return BarcodeGraphicTracker(mGraphicOverlay, graphic, mContext)
    }

}
