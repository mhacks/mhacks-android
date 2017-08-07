package com.mhacks.android.util

import android.content.Context
import android.support.annotation.DimenRes
import android.support.v4.content.ContextCompat

/**
 * Created by jeffreychang on 8/6/17.
 */
class ResourceUtil() {
    companion object {
        fun convertDpToPixel(context: Context, @DimenRes res: Int): Int {
            return context.resources.getDimensionPixelSize(res)
        }
    }
}