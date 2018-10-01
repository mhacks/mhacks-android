package com.mhacks.app.ui.info.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

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