package org.mhacks.app.core.ktx

import android.os.Build

fun isAtLeastQ(): Boolean {
    return Build.VERSION.SDK_INT >= 29
}