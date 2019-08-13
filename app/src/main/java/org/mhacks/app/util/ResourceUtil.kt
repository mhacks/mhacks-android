package org.mhacks.app.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes

/**
 * Created by jeffreychang on 8/6/17.
 */
object ResourceUtil{


    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android")
        if (resourceId > 0) return context.resources.getDimensionPixelSize(resourceId)
        return 0
    }
}