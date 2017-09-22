package com.mhacks.android.ui.common.util

import android.content.Context
import android.support.annotation.DimenRes
import android.util.TypedValue

/**
 * Created by jeffreychang on 8/6/17.
 */
class ResourceUtil() {
    companion object {
        fun convertDpResToPixel(context: Context, @DimenRes res: Int): Int {
            return context.resources.getDimensionPixelSize(res)
        }
        fun convertDpToPixel(context: Context, dim: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    (dim.toFloat()),
                    context.resources.getDisplayMetrics())
                    .toInt()
        }
        fun getStatusBarHeight(context: Context): Int {
            val resourceId = context.getResources().getIdentifier(
                    "status_bar_height",
                    "dimen",
                    "android")
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId)
            }
            return 0
        }
    }
}