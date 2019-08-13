package org.mhacks.app.core.ktx

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes

fun Context.convertDpResToPixel(@DimenRes res: Int) = resources.getDimensionPixelSize(res)

fun Context.convertDpToPixel(dim: Int) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (dim.toFloat()),
                resources.displayMetrics)
                .toInt()
