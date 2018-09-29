package com.mhacks.app.ui.info.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

data class Info(
        val type: TYPE,
        @StringRes val header: Int,
        @StringRes val subHeader: Int,
        @StringRes val description: Int,
        @DrawableRes val icon: Int
) {
    enum class TYPE {
        WIFI, ADDRESS, SLACK, EMAIL
    }
}