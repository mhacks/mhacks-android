package com.mhacks.app.util

import android.content.Context
import android.support.annotation.DimenRes
import android.util.TypedValue

/**
 * Created by jeffreychang on 8/6/17.
 */
object ResourceUtil{
    fun convertDpResToPixel(context: Context, @DimenRes res: Int): Int {
        return context.resources.getDimensionPixelSize(res)
    }
    fun convertDpToPixel(context: Context, dim: Int): Int =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (dim.toFloat()),
                context.resources.displayMetrics)
                .toInt()

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android")
        if (resourceId > 0) return context.resources.getDimensionPixelSize(resourceId)
        return 0
    }
}